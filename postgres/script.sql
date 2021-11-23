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
  start_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP NOT NULL,
  name VARCHAR(128) NOT NULL,
  description VARCHAR(512) NOT NULL,
  summary VARCHAR(256) NOT NULL,
  ical_id VARCHAR(64) NOT NULL,
  event_id INT NOT NULL,
  place_id VARCHAR(64) NOT NULL,
  PRIMARY KEY (event_id),
  FOREIGN KEY (place_id) REFERENCES Localisation(place_id)
);

CREATE TABLE Preferences
(
  preparation_time TIMESTAMP NOT NULL,
  notification_time TIMESTAMP NOT NULL,
  dark_mode INT NOT NULL,
  transport_name VARCHAR(12) NOT NULL,
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