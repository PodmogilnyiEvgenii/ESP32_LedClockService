CREATE SCHEMA IF NOT EXISTS clock;

CREATE TABLE IF NOT EXISTS clock.rawdata
(
    dataid                  SERIAL PRIMARY KEY,
    deviceid                VARCHAR(10) NOT NULL,
	name                    VARCHAR(30) NOT NULL,
    year                    INTEGER,
    month                   INTEGER,
    day                     INTEGER,
    hour                    INTEGER,
    minute                  INTEGER,
    second                  INTEGER,
    longitude               REAL,
    latitude                REAL,
    descriptionweather      VARCHAR(30),
    temperature             REAL,
    pressure                INTEGER,
    humidity                INTEGER,
    windspeed               INTEGER,
    lasttimegetweather      INTEGER,
    savedatastatus          VARCHAR(10),
    lastmessage             VARCHAR(1000)
);

create table if not exists clock.devices
(
    id                      SERIAL PRIMARY KEY,
    deviceid                VARCHAR(10) NOT NULL,
    name                    VARCHAR(30) NOT NULL,
	lastdataid              INTEGER
);
/*
INSERT INTO clock.devices VALUES (1, 'TEST00ID', 'testname00', 1);
INSERT INTO clock.devices VALUES (2, 'TEST01ID', 'testname01', 2);
INSERT INTO clock.rawdata VALUES (1, 'TEST00ID', 'testname00', 2022, 4, 30, 11, 22, 33, 30.44, 59.92, 'scattered clouds', 8.5, 759, 59, 7, 1193, 'good', '{"id":"TEST00ID","name":"testname00","type":"data","year":2022,"month":4,"day":30,"hour":11,"minute":22,"second":33,"lon":"30.44","lat":"59.92","weather":"scattered clouds","temp":8.5,"pressure":759,"humidity":59,"wind speed":7,"lastTimeGetWeather":1193,"saveDataStatus":"good"}');
INSERT INTO clock.rawdata VALUES (2, 'TEST01ID', 'testname01', 2023, 4, 30, 11, 22, 33, 30.44, 59.92, 'scattered clouds', 8.5, 759, 59, 7, 1193, 'good', '{"id":"TEST01ID","name":"testname01","type":"data","year":2023,"month":4,"day":30,"hour":11,"minute":22,"second":33,"lon":"30.44","lat":"59.92","weather":"scattered clouds","temp":8.5,"pressure":759,"humidity":59,"wind speed":7,"lastTimeGetWeather":1193,"saveDataStatus":"good"}');
*/