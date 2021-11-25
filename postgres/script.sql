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
  begin_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP NOT NULL,
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

INSERT INTO Transport VALUES ('BICYCLING');
INSERT INTO Transport VALUES ('DRIVING');
INSERT INTO Transport VALUES ('TRANSIT');
INSERT INTO Transport VALUES ('WALKING');

INSERT INTO student VALUES ('bera1107','Blond','Alexandre','https://www.gel.usherbrooke.ca/horarius/icalendar?key=bcb2d410-7b36-4ff3-bd0c-57c29c489261');
INSERT INTO student VALUES ('lacp3102','Petit','Pascal','https://www.gel.usherbrooke.ca/horarius/icalendar?key=67a822b8-32c3-4f87-b074-b01295f0c665');
INSERT INTO student VALUES ('rouj1615','Six','Julien','https://www.gel.usherbrooke.ca/horarius/icalendar?key=c754875d-2930-4ceb-9827-f90741d44338');
INSERT INTO student VALUES ('stla0801','22','Anthony','https://www.gel.usherbrooke.ca/horarius/icalendar?key=9761bc19-4df0-4db3-bd43-03831e35275a');
INSERT INTO student VALUES ('pera3307','AL','Alisée','');

INSERT INTO Localisation VALUES ('ChIJywfUkEyzt0wRPYYdc8CzfbU','45.3783275,-71.9284194','Faculté de génie, 2500 Bd de lUniversité, Sherbrooke');

DROP VIEW IF EXISTS StudentData;
CREATE VIEW StudentData AS
SELECT student.cip, name, surname, ical_key, preparation_time, notification_time, dark_mode, transport_name
FROM Student
LEFT JOIN Preferences ON Preferences.cip = Student.cip;

CREATE OR REPLACE FUNCTION onStudentDataChange() RETURNS TRIGGER AS
$$
BEGIN
UPDATE student SET surname=NEW.surname WHERE cip=OLD.cip;

IF NOT EXISTS (SELECT cip FROM preferences WHERE cip=OLD.cip) THEN
INSERT INTO preferences (cip) VALUES (OLD.cip);
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
SELECT event_id, begin_time AS start_time, traject.end_time, start_place_id, end_place_id, transport_name
FROM CalendarEvent
LEFT JOIN Traject ON place_id=start_place_id
WHERE NOT place_id IS NULL
UNION ALL
SELECT event_id, begin_time AS start_time, traject.end_time, start_place_id, end_place_id, transport_name
FROM CalendarEvent
LEFT JOIN Traject ON place_id=end_place_id
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

DROP VIEW IF EXISTS CalendarAttendance;
CREATE VIEW CalendarAttendance AS
SELECT cip, calendarevent.*
FROM calendarevent
JOIN attendance ON attendance.event_id = calendarevent.event_id;