CREATE TABLE Student
(
  cip                   CHAR(8)       NOT NULL,
  name                  VARCHAR(32)   NOT NULL,
  surname               VARCHAR(32)   NOT NULL,
  ical_key              VARCHAR(128),
  preparation_time      INT,
  notification_time     INT,
  notification_enable   CHAR(5),
  dark_mode             CHAR(5),
  transport             VARCHAR(16),

  PRIMARY KEY (cip),
  UNIQUE (ical_key)
);

CREATE TABLE Clan
(
  clan_id               SERIAL      NOT NULL,
  name                  VARCHAR(64) NOT NULL,

  PRIMARY KEY (clan_id)
);

CREATE TABLE Localisation
(
  place_id              VARCHAR(256) NOT NULL,

  PRIMARY KEY (place_id)
);

CREATE TABLE Clan_Member
(
  role                  INT,
  cip                   CHAR(8)      NOT NULL,
  clan_id               SERIAL       NOT NULL,

  PRIMARY KEY (cip, clan_id),
  FOREIGN KEY (cip) REFERENCES Student(cip),
  FOREIGN KEY (clan_id) REFERENCES Clan(clan_id)
);

CREATE TABLE Address
(
  cip                   CHAR(8)      NOT NULL,
  place_id              VARCHAR(256) NOT NULL,

  PRIMARY KEY (cip, place_id),
  FOREIGN KEY (cip) REFERENCES Student(cip),
  FOREIGN KEY (place_id) REFERENCES Localisation(place_id)
);

CREATE TABLE Event
(
  event_id              SERIAL        NOT NULL,
  time_start            TIMESTAMP     NOT NULL,
  time_end              TIMESTAMP     NOT NULL,
  name                  VARCHAR(64)   NOT NULL,
  summary               VARCHAR(128),
  description           VARCHAR(128),
  place_id              VARCHAR(256),
  ical_id               INT,

  PRIMARY KEY (event_id),
  FOREIGN KEY (place_id) REFERENCES Localisation(place_id)
);

CREATE TABLE Traject
(
  cip                   CHAR(8)       NOT NULL,
  event_id              SERIAL        NOT NULL,
  coords_start          VARCHAR(256)  NOT NULL,
  coords_end            VARCHAR(256)  NOT NULL,
  arrival_time          TIMESTAMP     NOT NULL,
  transport             VARCHAR(16),
  begin_time            TIMESTAMP,
  preparation_time      INT,

  PRIMARY KEY (cip, event_id),
  FOREIGN KEY (coords_end) REFERENCES Localisation(place_id),
  FOREIGN KEY (coords_end) REFERENCES Localisation(place_id),
  FOREIGN KEY (event_id) REFERENCES Event(event_id)
);

CREATE TABLE Attendance
(
  cip                   CHAR(8) NOT NULL,
  event_id              SERIAL  NOT NULL,

  PRIMARY KEY (cip, event_id),
  FOREIGN KEY (cip) REFERENCES Student(cip),
  FOREIGN KEY (event_id) REFERENCES Event(event_id)
);
