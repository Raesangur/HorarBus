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
  name VARCHAR(128) NOT NULL,
  description VARCHAR(512) NOT NULL,
  summary VARCHAR(256) NOT NULL,
  color VARCHAR(64) NOT NULL DEFAULT '#FFFFFF',
  place_id VARCHAR(64) NOT NULL,
  PRIMARY KEY (event_id),
  FOREIGN KEY (place_id) REFERENCES Localisation(place_id)
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

INSERT INTO Localisation VALUES ('ChIJywfUkEyzt0wRPYYdc8CzfbU','2500 Bd de lUniversité, Sherbrooke','Faculté de génie');

DROP VIEW IF EXISTS StudentData;
CREATE VIEW StudentData AS
SELECT student.cip, name, surname, ical_key, preparation_time, notification_time, dark_mode, transport_name
FROM Student
LEFT JOIN Preferences ON Preferences.cip = Student.cip;

CREATE OR REPLACE FUNCTION onStudentDataChange() RETURNS TRIGGER AS
$$
BEGIN
UPDATE student SET name=NEW.name, surname=NEW.surname WHERE cip=OLD.cip;

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
UPDATE event SET name=NEW.name, summary=NEW.summary, description=NEW.description, start_time=NEW.start_time, end_time=NEW.end_time, place_id=NEW.place_id WHERE event_id=NEW.event_id;
RETURN NULL;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER eventBeforeInsertTrigger BEFORE INSERT ON event
FOR EACH ROW
EXECUTE PROCEDURE beforeEventInsert();