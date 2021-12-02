DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

CREATE TABLE Localisation
(
  place_id VARCHAR(64) NOT NULL,
  coords VARCHAR(64) NOT NULL,
  address VARCHAR(512) NOT NULL,
  PRIMARY KEY (place_id)
);

CREATE TABLE Student
(
  cip CHAR(8) NOT NULL,
  name VARCHAR(64) NOT NULL,
  surname VARCHAR(64) NOT NULL,
  ical_key VARCHAR(128) NOT NULL,
  PRIMARY KEY (cip)
);

CREATE TABLE Transport
(
  transport_name VARCHAR(12) NOT NULL,
  PRIMARY KEY (transport_name)
);

CREATE TABLE Event
(
  event_id INT NOT NULL,
  start_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP NOT NULL,
  description VARCHAR(512) NOT NULL,
  summary VARCHAR(256) NOT NULL,
  color VARCHAR(64) NOT NULL DEFAULT '#FFFFFF',
  local VARCHAR(32),
  PRIMARY KEY (event_id)
);

CREATE TABLE Preferences
(
  preparation_time INT NOT NULL DEFAULT 15,
  notification_time INT NOT NULL DEFAULT 15,
  dark_mode CHAR(5) NOT NULL DEFAULT 'FALSE',
  transport_name VARCHAR(12) NOT NULL DEFAULT 'TRANSIT',
  home_place_id VARCHAR(64) NOT NULL DEFAULT 'ChIJR7e5TUezt0wR89e8h3CL7XI',
  cip CHAR(8) NOT NULL,
  PRIMARY KEY (cip),
  FOREIGN KEY (transport_name) REFERENCES Transport(transport_name),
  FOREIGN KEY (cip) REFERENCES Student(cip)
);

CREATE TABLE LocalizedEvent
(
  place_id VARCHAR(64) NOT NULL,
  event_id INT NOT NULL,
  PRIMARY KEY (place_id, event_id),
  FOREIGN KEY (place_id) REFERENCES Localisation(place_id),
  FOREIGN KEY (event_id) REFERENCES Event(event_id)
);

CREATE TABLE Traject
(
  begin_time TIME NOT NULL,
  end_time TIME NOT NULL,
  transport_name VARCHAR(12) NOT NULL,
  start_place_id VARCHAR(64) NOT NULL,
  end_place_id VARCHAR(64) NOT NULL,
  PRIMARY KEY (begin_time, end_time, transport_name, start_place_id, end_place_id),
  FOREIGN KEY (transport_name) REFERENCES Transport(transport_name),
  FOREIGN KEY (start_place_id) REFERENCES Localisation(place_id),
  FOREIGN KEY (end_place_id) REFERENCES Localisation(place_id)
);

CREATE TABLE Attendance
(
  cip CHAR(8) NOT NULL,
  event_id INT NOT NULL,
  PRIMARY KEY (cip, event_id),
  FOREIGN KEY (cip) REFERENCES Student(cip),
  FOREIGN KEY (event_id) REFERENCES Event(event_id)
);

DROP VIEW IF EXISTS StudentData;
CREATE VIEW StudentData AS
SELECT student.cip, name, surname, ical_key, preparation_time, notification_time, dark_mode, transport_name, home_place_id
FROM Student
LEFT JOIN Preferences ON Preferences.cip = Student.cip;

CREATE OR REPLACE FUNCTION onStudentDataChange() RETURNS TRIGGER AS
$$
BEGIN
IF NOT NEW.ical_key IS NULL THEN
UPDATE student SET ical_key=NEW.ical_key WHERE cip=OLD.cip;
END IF;


IF NOT EXISTS (SELECT cip FROM preferences WHERE cip=OLD.cip) THEN
INSERT INTO preferences (cip) VALUES (OLD.cip);
END IF;

IF NOT NEW.home_place_id IS NULL THEN
UPDATE preferences SET home_place_id=NEW.home_place_id WHERE cip=OLD.cip;
END IF;

IF NOT NEW.preparation_time IS NULL THEN
UPDATE preferences SET preparation_time=NEW.preparation_time WHERE cip=OLD.cip;
END IF;

IF NOT NEW.notification_time IS NULL THEN
UPDATE preferences SET notification_time=NEW.notification_time WHERE cip=OLD.cip;
END IF;

IF NOT NEW.dark_mode IS NULL THEN
UPDATE preferences SET dark_mode=NEW.dark_mode WHERE cip=OLD.cip;
END IF;

IF NOT NEW.transport_name IS NULL THEN
UPDATE preferences SET transport_name=NEW.transport_name WHERE cip=OLD.cip;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER studentDataTrigger INSTEAD OF UPDATE ON StudentData
FOR EACH ROW
EXECUTE PROCEDURE onStudentDataChange();

CREATE OR REPLACE FUNCTION beforeEventInsert() RETURNS TRIGGER AS
$$
BEGIN
IF EXISTS (SELECT event_id FROM event WHERE event_id = NEW.event_id) THEN
UPDATE event SET summary=NEW.summary, description=NEW.description, start_time=NEW.start_time, end_time=NEW.end_time WHERE event_id=NEW.event_id;
IF NOT NEW.local IS NULL THEN
UPDATE event SET local=NEW.local WHERE event_id=NEW.event_id;
END IF;
RETURN NULL;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER eventBeforeInsertTrigger BEFORE INSERT ON event
FOR EACH ROW
EXECUTE PROCEDURE beforeEventInsert();

DROP VIEW IF EXISTS CalendarEvent;
CREATE VIEW CalendarEvent AS
SELECT event.*, localisation.*
FROM event
LEFT JOIN localizedEvent ON event.event_id = localizedEvent.event_id
LEFT JOIN localisation ON localizedEvent.place_id = localisation.place_id;

DROP VIEW IF EXISTS TrajectEvent;
CREATE VIEW TrajectEvent AS
SELECT event_id, begin_time AS start_time, traject.end_time, start_place_id, end_place_id, transport_name, TRUE AS arrival
FROM CalendarEvent
LEFT JOIN Traject ON place_id=end_place_id
WHERE NOT place_id IS NULL
UNION ALL
SELECT event_id, begin_time AS start_time, traject.end_time, start_place_id, end_place_id, transport_name, FALSE AS arrival
FROM CalendarEvent
LEFT JOIN Traject ON place_id=start_place_id
WHERE NOT place_id IS NULL;

CREATE OR REPLACE FUNCTION beforeCalendarEventInsert() RETURNS TRIGGER AS
$$
BEGIN

IF NEW.place_id IS NULL THEN
  INSERT INTO event (event_id, summary, description, start_time, end_time) VALUES (NEW.event_id, NEW.summary, NEW.description, NEW.start_time, NEW.end_time);
  IF NOT NEW.color IS NULL THEN
    UPDATE event SET color = NEW.color WHERE event_id = NEW.event_id;
  END IF;
  IF NOT NEW.local IS NULL THEN
    UPDATE event SET local = NEW.local WHERE event_id = NEW.event_id;
  END IF;
  RETURN NEW;
ELSE 
  IF NOT EXISTS (SELECT place_id FROM localisation WHERE place_id = NEW.place_id) THEN
    INSERT INTO localisation (place_id, coords, address) VALUES (NEW.place_id, NEW.coords, NEW.address);
  END IF;
END IF;

INSERT INTO event (event_id, summary, description, start_time, end_time) VALUES (NEW.event_id, NEW.summary, NEW.description, NEW.start_time, NEW.end_time);
IF NOT NEW.color IS NULL THEN
  UPDATE event SET color = NEW.color WHERE event_id = NEW.event_id;
END IF;
IF NOT NEW.local IS NULL THEN
  UPDATE event SET local = NEW.local WHERE event_id = NEW.event_id;
END IF;

IF NOT NEW.place_id IS NULL THEN
  INSERT INTO localizedEvent (event_id, place_id) VALUES (NEW.event_id, NEW.place_id);
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER beforeCalendarEventInsertTrigger INSTEAD OF INSERT ON CalendarEvent
FOR EACH ROW
EXECUTE PROCEDURE beforeCalendarEventInsert();

-- DROP VIEW IF EXISTS UserTrajectEvent;
-- CREATE VIEW UserTrajectEvent AS
-- SELECT student.cip, trajectevent.*
-- FROM trajectevent
-- JOIN attendance ON attendance.event_id = trajectevent.event_id
-- JOIN student ON student.cip = attendance.cip;

DROP VIEW IF EXISTS UserTrajectEvent;
CREATE VIEW UserTrajectEvent AS
select student.cip, event.start_time as event_start_time, te.*
from trajectevent AS TE, (SELECT event.event_id, MIN(extract (MILLISECONDS FROM trajectevent.end_time) - extract(MILLISECONDS from event.start_time)) AS earlyAmount
	FROM trajectevent
	JOIN event ON event.event_id = trajectevent.event_id
	GROUP BY event.event_id, trajectevent.event_id, arrival) AS ER,
	attendance, student, event
where attendance.event_id = TE.event_id and student.cip = attendance.cip and event.event_id = attendance.event_id
group by student.cip, te.event_id, te.start_time, te.end_time, te.start_place_id, te.end_place_id, te.transport_name, te.arrival, er.earlyamount, event.start_time
HAVING ER.earlyAmount = MIN(extract (MILLISECONDS FROM TE.end_time) - extract(MILLISECONDS from event.start_time)) OR ER.earlyAmount IS NULL
order by event_id;

DROP VIEW IF EXISTS CalendarAttendance;
CREATE VIEW CalendarAttendance AS
SELECT attendance.cip, calendarevent.*, transport_name AS requestedTransport
FROM calendarevent
JOIN attendance ON attendance.event_id = calendarevent.event_id
JOIN studentdata ON studentdata.cip = attendance.cip;

DROP VIEW IF EXISTS MissingTraject;
CREATE VIEW MissingTraject AS
SELECT studentdata.cip, calendarattendance.event_id, calendarattendance.start_time, calendarattendance.end_time,
CASE WHEN arrival IS true THEN home_place_id ELSE place_id END AS startPlace,
CASE WHEN NOT arrival IS false THEN place_id ELSE home_place_id END AS targetPlace,
requestedTransport
FROM usertrajectevent
JOIN calendarattendance ON calendarattendance.event_id = usertrajectevent.event_id AND (start_place_id IS NULL OR end_place_id IS NULL OR transport_name <> requestedTransport)
JOIN studentdata ON studentdata.cip = usertrajectevent.cip
WHERE NOT place_id IS NULL;

CREATE OR REPLACE FUNCTION assureStudentPreferences() RETURNS TRIGGER AS
$$
BEGIN
INSERT INTO preferences (cip) VALUES (NEW.cip);
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER afterStudentInsertion AFTER INSERT ON student
FOR EACH ROW
EXECUTE PROCEDURE assureStudentPreferences();

DROP VIEW IF EXISTS eventData;
CREATE VIEW eventDATA AS
SELECT a.cip, e.event_id, e.start_time, e.end_time, transport_name AS requestedTransport
FROM event e
JOIN attendance a ON a.event_id = e.event_id
JOIN studentdata sd ON sd.cip = a.cip;


DROP VIEW IF EXISTS requiredRoutes;
CREATE VIEW requiredRoutes AS
select * from (	
select true as arrival, *
from eventdata
union all
select false as arrival, *
from eventdata	
) as a
order by event_id, cip, arrival desc;

DROP VIEW IF EXISTS routeDetails;s
CREATE VIEW routeDetails AS
select rr.cip, arrival, rr.event_id, requestedtransport,
case when arrival = false then place_id else home_place_Id end as startPlace,
case when arrival = true then place_id else home_place_id end as targetPlace,
case when arrival = true then start_time else null end as timeToArrive,
case when arrival = false then end_time else null end timeToLeave
from requiredRoutes rr, studentdata sd, localizedevent le
where rr.cip = sd.cip and le.event_id = rr.event_id;


DROP VIEW IF EXISTS trajectInfo;
CREATE VIEW trajectInfo AS
select distinct cip, arrival, event_id, requestedtransport as transport, startplace, targetplace, timetoarrive, timetoleave, begin_time, end_time
from routedetails rd
left join traject t on requestedtransport = transport_name;


DROP VIEW IF EXISTS usedTrajects;
CREATE VIEW usedTrajects AS
select cip, arrival, event_id, transport, startplace, targetplace, timetoarrive, timetoleave,
case when transport='TRANSIT' then begin_time else null end as begin_time,
case when transport='TRANSIT' then end_time else null end as end_time
from trajectinfo;


DROP VIEW IF EXISTS MissingTraject;
CREATE VIEW MissingTraject AS
select cip, arrival, event_id, requestedtransport, startplace, targetplace, timetoarrive, timetoleave, begin_time, end_time
from routedetails rd
left join traject t on
t.transport_name = rd.requestedtransport and startplace = start_place_id and targetplace = end_place_id and
((rd.requestedtransport = 'TRANSIT' and ((arrival = true and timetoarrive::time = end_time) or (arrival = false and timetoleave::time = begin_time))) or rd.requestedtransport <> 'TRANSIT')
where (start_place_id is null or end_place_id is null) and (timetoarrive > current_date or timetoleave > current_date)
order by timetoarrive desc, timetoleave desc;


DROP VIEW IF EXISTS UserTrajectEvent;
CREATE VIEW UserTrajectEvent AS
select cip, arrival, event_id, requestedtransport, startplace, targetplace, timetoarrive, timetoleave, begin_time, end_time
from routedetails rd
join traject t on
t.transport_name = rd.requestedtransport and startplace = start_place_id and targetplace = end_place_id and
((rd.requestedtransport = 'TRANSIT' and ((arrival = true and timetoarrive::time = end_time) or (arrival = false and timetoleave::time = begin_time))) or rd.requestedtransport <> 'TRANSIT')
order by timetoarrive desc, timetoleave desc;

INSERT INTO Transport VALUES ('BICYCLING');
INSERT INTO Transport VALUES ('DRIVING');
INSERT INTO Transport VALUES ('TRANSIT');
INSERT INTO Transport VALUES ('WALKING');

INSERT INTO Localisation VALUES ('ChIJywfUkEyzt0wRPYYdc8CzfbU','45.3783275,-71.9284194','Faculté de génie, 2500 Bd de lUniversité, Sherbrooke');
INSERT INTO Localisation VALUES ('ChIJR7e5TUezt0wR89e8h3CL7XI','45.3886837,-71.9165197','Pub Chez Willard, 1600 Rue Denault, Sherbrooke, QC J1H 2R2');