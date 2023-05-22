CREATE SCHEMA IF NOT EXISTS clock;

CREATE TABLE IF NOT EXISTS clock.rawdata
(
    id                      BIGSERIAL PRIMARY KEY,
    device_id               VARCHAR(16) NOT NULL,
	name                    VARCHAR(30) NOT NULL,
    year                    INTEGER,
    month                   INTEGER,
    day                     INTEGER,
    hour                    INTEGER,
    minute                  INTEGER,
    second                  INTEGER,
    latitude                REAL,
    longitude               REAL,
    description_weather     VARCHAR(30),
    temperature             REAL,
    pressure                INTEGER,
    humidity                INTEGER,
    wind_speed              INTEGER,
    last_time_get_weather   INTEGER,
    save_data_status        VARCHAR(10),
    last_message            VARCHAR(1000)
);

create table if not exists clock.devices
(
    id                      BIGSERIAL PRIMARY KEY,
    device_id               VARCHAR(16) NOT NULL,
    name                    VARCHAR(30) NOT NULL,
	last_data_id            INTEGER
);

/*
INSERT INTO clock.devices VALUES (1, 'TEST00ID', 'testname00', 1);
INSERT INTO clock.devices VALUES (2, 'TEST01ID', 'testname01', 2);
INSERT INTO clock.rawdata VALUES (1, 'TEST00ID', 'testname00', 2022, 4, 30, 11, 22, 33, 30.44, 59.92, 'scattered clouds', 8.5, 759, 59, 7, 1193, 'good', '{"id":"TEST00ID","name":"testname00","type":"data","year":2022,"month":4,"day":30,"hour":11,"minute":22,"second":33,"lon":"30.44","lat":"59.92","weather":"scattered clouds","temp":8.5,"pressure":759,"humidity":59,"wind speed":7,"lastTimeGetWeather":1193,"saveDataStatus":"good"}');
INSERT INTO clock.rawdata VALUES (2, 'TEST01ID', 'testname01', 2023, 4, 30, 11, 22, 33, 30.44, 59.92, 'scattered clouds', 8.5, 759, 59, 7, 1193, 'good', '{"id":"TEST01ID","name":"testname01","type":"data","year":2023,"month":4,"day":30,"hour":11,"minute":22,"second":33,"lon":"30.44","lat":"59.92","weather":"scattered clouds","temp":8.5,"pressure":759,"humidity":59,"wind speed":7,"lastTimeGetWeather":1193,"saveDataStatus":"good"}');
*/