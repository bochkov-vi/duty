drop table if exists SHIFT_TYPE cascade;


create table if not exists SHIFT_TYPE
(
    ID_SHIFT_TYPE INTEGER not null primary key,
    SHIFT_TYPE    VARCHAR_IGNORECASE(45),
    FA_ICON       VARCHAR(255),
    HTML_CLASS    VARCHAR(255),
    PLAIN_TEXT    VARCHAR(3),
    CREATED_DATE  TIMESTAMP
);

CREATE SEQUENCE IF NOT EXISTS SHIFT_TYPE_SEQ START WITH 100;

drop table if exists DAY cascade;

create table if not exists DAY
(
    DATE                  DATE                  not null,
    ID_SHIFT_TYPE_DEFAULT INTEGER,
    DAYS_FROM_WEEKEND     INTEGER,
    DAYS_TO_WEEKEND       INTEGER,
    NEXT                  DATE,
    WEEKEND               BOOLEAN DEFAULT false NOT NULL,
    SHORTENED             BOOLEAN DEFAULT false NOT NULL,
    CREATED_DATE          TIMESTAMP,
    constraint DAY_PRIMARY_KEY primary key (DATE),
    constraint NEXT_DAY_FK foreign key (DATE) references DAY (DATE),
    constraint DAY_DEFAULT_SHIFT_TYPE_FK foreign key (ID_SHIFT_TYPE_DEFAULT) references SHIFT_TYPE (ID_SHIFT_TYPE)
);

drop table if exists DAY_SHIFT_TYPE_COUNT_PER_PAGE cascade;
/*create table if not exists DAY_SHIFT_TYPE_COUNT_PER_PAGE (
    DATE              DATE    not null,
    ID_SHIFT_TYPE     INTEGER not null,
    MAX_COUNT_PER_DAY INTEGER default 1,
    MIN_COUNT_PER_DAY INTEGER default 1,
    constraint DAY_SHIFT_TYPE_COUNT_PER_PAGE_PRIMARY_KEY primary key (DATE, ID_SHIFT_TYPE),
    constraint DAY_SHIFT_TYPE_COUNT_PER_PAGE_FK foreign key (DATE) references DAY(DATE) on update cascade on delete cascade,
    constraint DAY_SHIFT_TYPE_COUNT_PER_PAGE_SHIFT_TYPE_FK foreign key (ID_SHIFT_TYPE) references SHIFT_TYPE(ID_SHIFT_TYPE) on update cascade on delete cascade
);*/

drop table if exists DAY_PERIOD cascade;

create table if not exists DAY_PERIOD
(
    DATE     DATE    not null,
    START    TIME    NOT NULL,
    DURATION INTEGER NOT NULL,
    constraint DAY_PERIOD_PK primary key (DATE, START),
    constraint DAY_PERIOD_FK foreign key (DATE) REFERENCES DAY (DATE) on delete cascade on update cascade
);



drop table if exists SHIFT_TYPE_PERIOD cascade;


create table if not exists SHIFT_TYPE_PERIOD
(
    ID_SHIFT_TYPE INTEGER not null,
    START         TIME    not null,
    DURATION      INTEGER not null,
    constraint SHIFT_TYPE_PERIOD_PK primary key (ID_SHIFT_TYPE, START),
    constraint SHIFT_TYPE_PERIOD_FK foreign key (ID_SHIFT_TYPE) references SHIFT_TYPE (ID_SHIFT_TYPE) on update cascade on delete cascade
);

drop table if exists SHIFT_TYPE_DAYS_FROM_WEEKEND cascade;

create table if not exists SHIFT_TYPE_DAYS_FROM_WEEKEND
(
    ID_SHIFT_TYPE     INTEGER not null,
    DAYS_FROM_WEEKEND INTEGER,
    constraint DAYS_FROM_WEEKEND_SHIFT_TYPE_PK primary key (ID_SHIFT_TYPE, DAYS_FROM_WEEKEND),
    constraint DAYS_FROM_WEEKEND_SHIFT_TYPE_FK foreign key (ID_SHIFT_TYPE) references SHIFT_TYPE (ID_SHIFT_TYPE)
);

drop table if exists SHIFT_TYPE_DAYS_TO_WEEKEND cascade;

create table if not exists SHIFT_TYPE_DAYS_TO_WEEKEND
(
    ID_SHIFT_TYPE   INTEGER not null,
    DAYS_TO_WEEKEND INTEGER,
    constraint DAYS_TO_WEEKEND_SHIFT_TYPE_PK primary key (ID_SHIFT_TYPE, DAYS_TO_WEEKEND),
    constraint DAYS_TO_WEEKEND_SHIFT_TYPE_FK foreign key (ID_SHIFT_TYPE) references SHIFT_TYPE (ID_SHIFT_TYPE)
);


drop table if exists RANG cascade;

create table if not exists RANG
(
    ID_RANG      SMALLINT not null,
    RANG         VARCHAR_IGNORECASE(15),
    FULL_NAME    VARCHAR_IGNORECASE(45),
    CREATED_DATE TIMESTAMP(26, 6),
    constraint RANG_PK primary key (ID_RANG)
);

create sequence if not exists RANG_SEQ start with 50;

INSERT INTO PUBLIC.RANG (ID_RANG, CREATED_DATE, FULL_NAME, RANG)
VALUES (11, '2018-11-18 18:32:09.727000', 'Мичман', 'м-н');
INSERT INTO PUBLIC.RANG (ID_RANG, CREATED_DATE, FULL_NAME, RANG)
VALUES (12, '2018-11-18 18:32:09.749000', 'Прапорщик', 'пр-к');
INSERT INTO PUBLIC.RANG (ID_RANG, CREATED_DATE, FULL_NAME, RANG)
VALUES (13, '2018-11-18 18:32:09.751000', 'Старший Мичман', 'с.м-н');
INSERT INTO PUBLIC.RANG (ID_RANG, CREATED_DATE, FULL_NAME, RANG)
VALUES (14, '2018-11-18 18:32:09.753000', 'Старший Прапорщик', 'с.п-к');
INSERT INTO PUBLIC.RANG (ID_RANG, CREATED_DATE, FULL_NAME, RANG)
VALUES (15, '2018-11-18 18:32:09.755000', 'Младший лейтенант', 'м.л-т');
INSERT INTO PUBLIC.RANG (ID_RANG, CREATED_DATE, FULL_NAME, RANG)
VALUES (17, '2018-11-18 18:32:09.756000', 'Лейтенант', 'л-т');
INSERT INTO PUBLIC.RANG (ID_RANG, CREATED_DATE, FULL_NAME, RANG)
VALUES (19, '2018-11-18 18:32:09.758000', 'Старший лейтенант', 'с.л-т');
INSERT INTO PUBLIC.RANG (ID_RANG, CREATED_DATE, FULL_NAME, RANG)
VALUES (21, '2018-11-18 18:32:09.759000', 'Капитан лейтенант', 'к.л-т');
INSERT INTO PUBLIC.RANG (ID_RANG, CREATED_DATE, FULL_NAME, RANG)
VALUES (22, '2018-11-18 18:32:09.761000', 'Капитан', 'к-н');
INSERT INTO PUBLIC.RANG (ID_RANG, CREATED_DATE, FULL_NAME, RANG)
VALUES (23, '2018-11-18 18:32:09.762000', 'Капитан 3-го ранга', 'к.3р');
INSERT INTO PUBLIC.RANG (ID_RANG, CREATED_DATE, FULL_NAME, RANG)
VALUES (24, '2018-11-18 18:32:09.764000', 'Майор', 'м-р');
INSERT INTO PUBLIC.RANG (ID_RANG, CREATED_DATE, FULL_NAME, RANG)
VALUES (25, '2018-11-18 18:32:09.765000', 'Капитан 2-го ранга', 'к.2р');
INSERT INTO PUBLIC.RANG (ID_RANG, CREATED_DATE, FULL_NAME, RANG)
VALUES (26, '2018-11-18 18:32:09.767000', 'Подполковник', 'п.п-к');
INSERT INTO PUBLIC.RANG (ID_RANG, CREATED_DATE, FULL_NAME, RANG)
VALUES (27, '2018-11-18 18:32:09.769000', 'Капитан 1-го ранга', 'к.1р');
INSERT INTO PUBLIC.RANG (ID_RANG, CREATED_DATE, FULL_NAME, RANG)
VALUES (28, '2018-11-18 18:32:09.770000', 'Полковник', 'п-к');

drop table if exists EMPLOYEE_GROUP cascade;

create table if not exists EMPLOYEE_GROUP
(
    ID_EMPLOYEE_GROUP INTEGER                not null,
    EMPLOYEE_GROUP    VARCHAR_IGNORECASE(45) not null,
    CREATED_DATE      TIMESTAMP,
    CONSTRAINT EMPLOYEE_GROUP_PK primary key (ID_EMPLOYEE_GROUP),
    CONSTRAINT EMPLOYEE_GROUP_UNQ UNIQUE (EMPLOYEE_GROUP)
);

drop table if exists EMPLOYEE_GROUP_SHIFT_TYPE;
create table if not exists EMPLOYEE_GROUP_SHIFT_TYPE
(
    ID_EMPLOYEE_GROUP INTEGER not null,
    ID_SHIFT_TYPE     INTEGER not null,
    constraint EMPLOYEE_GROUP_SHIFT_TYPE_PK primary key (ID_EMPLOYEE_GROUP, ID_SHIFT_TYPE),
    constraint EMPLOYEE_GROUP_SHIFT_TYPE_EMPLOYEE_GROUP_FK foreign key (ID_EMPLOYEE_GROUP) references EMPLOYEE_GROUP (ID_EMPLOYEE_GROUP) on update cascade on delete cascade,
    constraint EMPLOYEE_GROUP_SHIFT_TYPE_SHIFT_TYPE_FK foreign key (ID_SHIFT_TYPE) references SHIFT_TYPE (ID_SHIFT_TYPE) on update cascade on delete cascade
);

insert into EMPLOYEE_GROUP (ID_EMPLOYEE_GROUP, EMPLOYEE_GROUP, CREATED_DATE)
VALUES (1, '1 группа СО', CURRENT_TIMESTAMP()),
       (2, '2 отделение СО', CURRENT_TIMESTAMP()),
       (101, 'ПСОИ-1', CURRENT_TIMESTAMP());
create sequence if not exists EMPLOYEE_GROUP_SEQ start with 1000;

drop table if exists EMPLOYEE cascade;

create table if not exists EMPLOYEE
(
    ID_EMPLOYEE       INT      not null primary key,
    FIRST_NAME        VARCHAR_IGNORECASE(255),
    LAST_NAME         VARCHAR_IGNORECASE(255),
    MIDDLE_NAME       VARCHAR_IGNORECASE(255),
    POST              VARCHAR_IGNORECASE(255),
    ID_EMPLOYEE_GROUP INTEGER,
    ID_RANG           SMALLINT not null,
    ROAD_TO_HOME_TIME INTEGER,
    CREATED_DATE      TIMESTAMP,
    constraint EMPLOYEE_EMPLOYEE_GROUP_FK foreign key (ID_EMPLOYEE_GROUP) references EMPLOYEE_GROUP (ID_EMPLOYEE_GROUP),
    constraint EMPLOYEE_RANG_FK foreign key (ID_RANG) references RANG (ID_RANG)
);

drop table if exists EMPLOYEE_SHIFT_TYPE;
create table if not exists EMPLOYEE_SHIFT_TYPE
(
    ID_EMPLOYEE   INT     not null,
    ID_SHIFT_TYPE INTEGER not null,
    constraint EMPLOYEE_SHIFT_TYPE_PK primary key (ID_EMPLOYEE, ID_SHIFT_TYPE),
    constraint EMPLOYEE_SHIFT_TYPE_EMPLOYEE_FK foreign key (ID_EMPLOYEE) references EMPLOYEE (ID_EMPLOYEE) on update cascade on delete cascade,
    constraint EMPLOYEE_SHIFT_TYPE_SHIFT_TYPE_FK foreign key (ID_SHIFT_TYPE) references SHIFT_TYPE (ID_SHIFT_TYPE) on update cascade on delete cascade
);

drop table if exists SHIFT_PERIOD;
drop table if exists SHIFT_ASSIGNMENT cascade;
drop table if exists SHIFT cascade;

create table if not exists SHIFT
(
    ID_SHIFT           INTEGER not null,
    DATE               DATE    not null,
    ID_SHIFT_TYPE      INTEGER not null,
    CREATED_DATE       TIMESTAMP,
    CREATED_BY         VARCHAR(255),
    LAST_MODIFIED_BY   VARCHAR(255),
    LAST_MODIFIED_DATE TIMESTAMP,
    constraint SHIFT_PK primary key (ID_SHIFT),
    constraint SHIFT_SHIFT_TYPE_FK foreign key (ID_SHIFT_TYPE) references SHIFT_TYPE (ID_SHIFT_TYPE)
);

create table if not exists SHIFT_ASSIGNMENT
(
    ID_SHIFT_ASSIGNMENT INTEGER not null,
    ID_SHIFT            INTEGER not null,
    ID_EMPLOYEE         INTEGER not null,
    CREATED_DATE        TIMESTAMP,
    constraint SHIFT_ASSIGNMENT_PK primary key (ID_SHIFT_ASSIGNMENT),
    constraint SHIFT_ASSIGNMENT_SHIFT_FK foreign key (ID_SHIFT) references SHIFT (ID_SHIFT) on delete cascade on update cascade,
    constraint SHIFT_ASSIGNMENT_EMPLOYEE_FK foreign key (ID_EMPLOYEE) references EMPLOYEE (ID_EMPLOYEE)

);
CREATE SEQUENCE IF NOT EXISTS SHIFT_ASSIGNMENT_SEQ start with 1000;


create table if not exists SHIFT_PERIOD
(
    ID_SHIFT INTEGER not null,
    START    TIME    NOT NULL,
    DURATION INTEGER NOT NULL,
    constraint SHIFT_PERIOD_PK primary key (ID_SHIFT, START),
    constraint SHIFT_PERIOD_FK foreign key (ID_SHIFT) REFERENCES SHIFT (ID_SHIFT) on delete cascade on update cascade
);



drop table if exists ROSTER_EMPLOYEE;
drop table if exists ROSTER_SHIFT_TYPE;

drop table if exists ROSTER;

create table if not exists ROSTER
(
    ID_ROSTER                INTEGER      not null primary key,
    DATE_FROM                DATE         not null,
    DATE_TITLE               VARCHAR(255) not null,
    DATE_TO                  DATE         not null,
    GENITIVE_DEPARTMENT_NAME VARCHAR(255) not null,
    ROSTER_TITLE             VARCHAR(255) not null,
    CHIEF                    VARCHAR(15)  not null,
    EXECUTOR                 VARCHAR(15)  not null,
    CREATED_DATE             TIMESTAMP,
    CREATED_BY               VARCHAR(255),
    LAST_MODIFIED_BY         VARCHAR(255),
    LAST_MODIFIED_DATE       TIMESTAMP,
    DATE                     DATE         not null,
    constraint ROSTER_EXECUTOR_FK foreign key (EXECUTOR) references EMPLOYEE (ID_EMPLOYEE),
    constraint ROSTER_CHIEF_FK foreign key (CHIEF) references EMPLOYEE (ID_EMPLOYEE)
);
create sequence if not exists ROSTER_SEQ start with 100;

create table ROSTER_SHIFT_TYPE
(
    ID_ROSTER     INTEGER not null,
    ID_SHIFT_TYPE INTEGER not null,
    constraint ROSTER_SHIFT_TYPE_PK primary key (ID_ROSTER, ID_SHIFT_TYPE),
    constraint ROSTER_SHIFT_TYPE_ROSTER_FK foreign key (ID_ROSTER) references ROSTER (ID_ROSTER) on update cascade on delete cascade,
    constraint ROSTER_SHIFT_TYPE_SHIFT_TYPE_FK foreign key (ID_SHIFT_TYPE) references SHIFT_TYPE (ID_SHIFT_TYPE) on update cascade on delete cascade
);
create table ROSTER_EMPLOYEE
(
    ID_ROSTER   INTEGER not null,
    ID_EMPLOYEE INT     NOT NULL,
    primary key (ID_ROSTER, ID_EMPLOYEE),
    constraint ROSTER_EMPLOYEE_ROSTER_FK foreign key (ID_ROSTER) references ROSTER (ID_ROSTER) on update cascade on delete cascade,
    constraint ROSTER_EMPLOYEE_EMPLOYEE_FK foreign key (ID_EMPLOYEE) references EMPLOYEE (ID_EMPLOYEE) on update cascade on delete cascade
);


ALTER SEQUENCE SHIFT_TYPE_SEQ RESTART WITH (SELECT COALESCE(MAX (ID_SHIFT_TYPE)+1,1) FROM PUBLIC.SHIFT_TYPE);
ALTER SEQUENCE RANG_SEQ RESTART WITH (SELECT COALESCE(MAX (ID_RANG)+1,1) FROM PUBLIC.RANG);
ALTER SEQUENCE EMPLOYEE_GROUP_SEQ RESTART WITH (SELECT COALESCE(MAX (ID_EMPLOYEE_GROUP)+1,1) FROM EMPLOYEE_GROUP);
ALTER SEQUENCE ROSTER_SEQ RESTART WITH (SELECT COALESCE(MAX (ID_ROSTER)+1,100) FROM ROSTER);


-- ===================       ROSTER       =========================

drop table if exists SHIFT_ROSTERING_DAY;
drop table if exists SHIFT_ROSTERING_EMPLOYEE;
drop table if exists SHIFT_ROSTERING_SHIFT;
drop table if exists SHIFT_ROSTERING_SHIFT_TYPE;

drop table if exists SHIFT_ROSTERING;

CREATE SEQUENCE IF NOT EXISTS SHIFT_ROSTERING_SEQ;

create table if not exists SHIFT_ROSTERING
(
    ID_SHIFT_ROSTERING INTEGER not null,
    CREATED_DATE       TIMESTAMP,
    INITSCORE          INTEGER,
    HARDSCORE          INTEGER,
    MEDIUMSCORE        INTEGER,
    SOFTSCORE          INTEGER,
    constraint SHIFT_ROSTERING_PK primary key (ID_SHIFT_ROSTERING)
);

create table if not exists SHIFT_ROSTERING_DAY
(
    ID_SHIFT_ROSTERING INTEGER not null,
    DATE               DATE    not null,
    constraint SHIFT_ROSTERING_DAY_DAY_FK foreign key (DATE) references DAY (DATE),
    constraint SHIFT_ROSTERING_DAY_SHIFT_ROSTERING_FK foreign key (ID_SHIFT_ROSTERING) references SHIFT_ROSTERING (ID_SHIFT_ROSTERING)
);

create table if not exists SHIFT_ROSTERING_EMPLOYEE
(
    ID_SHIFT_ROSTERING INTEGER not null,
    ID_EMPLOYEE        INT     NOT NULL,
    constraint SHIFT_ROSTERING_EMPLOYEE_PK primary key (ID_SHIFT_ROSTERING, ID_EMPLOYEE),
    constraint SHIFT_ROSTERING_EMPLOYEE_SHIFT_ROSTERING foreign key (ID_SHIFT_ROSTERING) references SHIFT_ROSTERING (ID_SHIFT_ROSTERING),
    constraint SHIFT_ROSTERING_EMPLOYEE_EMPLOYEE foreign key (ID_EMPLOYEE) references EMPLOYEE (ID_EMPLOYEE)
);

create table if not exists SHIFT_ROSTERING_SHIFT
(
    ID_SHIFT_ROSTERING INTEGER not null,
    ID_SHIFT           INTEGER not null,
    constraint SHIFT_ROSTERING_SHIFT_PK primary key (ID_SHIFT_ROSTERING, ID_SHIFT),
    constraint SHIFT_ROSTERING_SHIFT_SHIFT_FK foreign key (ID_SHIFT) references SHIFT (ID_SHIFT),
    constraint SHIFT_ROSTERING_SHIFT_SHIFT_ROSTERING_FK foreign key (ID_SHIFT_ROSTERING) references SHIFT_ROSTERING (ID_SHIFT_ROSTERING)
);

create table if not exists SHIFT_ROSTERING_SHIFT_TYPE
(
    ID_SHIFT_ROSTERING INTEGER not null,
    ID_SHIFT_TYPE      INTEGER not null,
    constraint SHIFT_ROSTERING_SHIFT_TYPE_PK primary key (ID_SHIFT_ROSTERING, ID_SHIFT_TYPE),
    constraint SHIFT_ROSTERING_SHIFT_TYPE_SHIFT_TYPE_FK foreign key (ID_SHIFT_TYPE) references SHIFT_TYPE (ID_SHIFT_TYPE),
    constraint SHIFT_ROSTERING_SHIFT_TYPE_SHIFT_ROSTERING_FK foreign key (ID_SHIFT_ROSTERING) references SHIFT_ROSTERING (ID_SHIFT_ROSTERING)
);

drop table if exists EMPLOYEE_SHIFT_TYPE_LIMIT;
create table if not exists EMPLOYEE_DUTY_TYPE_LIMIT
(
    ID_SHIFT_ROSTERING INTEGER not null,
    ID_EMPLOYEE        INTEGER not null,
    ID_SHIFT_TYPE      INTEGER not null,
    MIN                INTEGER,
    MAX                INTEGER,
    CREATED_DATE       TIMESTAMP,
    constraint EMPLOYEE_DUTY_TYPE_LIMIT_PK primary key (ID_SHIFT_ROSTERING, ID_EMPLOYEE, ID_SHIFT_TYPE),
    constraint EMPLOYEE_DUTY_TYPE_LIMIT_SHIFT_TYPE_FK foreign key (ID_SHIFT_TYPE) references SHIFT_TYPE (ID_SHIFT_TYPE),
    constraint EMPLOYEE_DUTY_TYPE_LIMIT_EMPLOYEE_FK foreign key (ID_EMPLOYEE) references EMPLOYEE (ID_EMPLOYEE),
    constraint EMPLOYEE_DUTY_TYPE_LIMIT_SHIFT_ROSTERING_FK foreign key (ID_SHIFT_ROSTERING) references SHIFT_ROSTERING (ID_SHIFT_ROSTERING)
);

drop table if exists VACATION_PART;
drop table if exists VACATION;

create table if not exists VACATION
(
    ID_EMPLOYEE  INT     NOT NULL,
    YEAR         INTEGER not null,
    CREATED_DATE TIMESTAMP,
    constraint VACATION_PK primary key (ID_EMPLOYEE, YEAR),
    constraint VACATION_EMPLOYEE_FK foreign key (ID_EMPLOYEE) references EMPLOYEE (ID_EMPLOYEE)
);

create table VACATION_PART
(
    ID_EMPLOYEE  INT     NOT NULL,
    YEAR         INTEGER not null,
    PART_NUMBER  INTEGER not null,
    START        DATE    not null,
    END          DATE    not null,
    CREATED_DATE TIMESTAMP,
    constraint VACATION_PART_PK primary key (ID_EMPLOYEE, YEAR, START),
    constraint VACATION_PART_FK foreign key (ID_EMPLOYEE, YEAR) references VACATION (ID_EMPLOYEE, YEAR)
);







