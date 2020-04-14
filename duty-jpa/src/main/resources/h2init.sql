drop table if exists DAY cascade;

create table if not exists DAY
(
    DATE              DATE    not null,
    ID_DUTY_TYPE      INTEGER ,
    DAYS_FROM_WEEKEND INTEGER,
    DAYS_TO_WEEKEND   INTEGER,
    NEXT              DATE,
    WEEKEND           BOOLEAN,
    CREATED_DATE      TIMESTAMP,
    primary key (DATE),
    constraint NEXT_DAY_FK foreign key (DATE) references DAY (DATE)
);

drop table if exists DAY_PERIOD cascade;

create table if not exists DAY_PERIOD
(
    DATE     DATE    not null,
    START    TIME    NOT NULL,
    DURATION INTEGER NOT NULL,
    primary key (DATE, START),
    constraint DAY_PERIOD_FK foreign key (DATE) REFERENCES DAY (DATE) on delete cascade on update cascade
);

drop table if exists DUTY_TYPE cascade;


create table if not exists DUTY_TYPE
(
    ID_DUTY_TYPE INTEGER not null primary key,
    DUTY_TYPE    VARCHAR_IGNORECASE(45),
    FA_ICON      VARCHAR(255),
    HTML_CLASS   VARCHAR(255),
    PLAIN_TEXT   VARCHAR(3),
    CREATED_DATE TIMESTAMP
);

CREATE SEQUENCE IF NOT EXISTS DUTY_TYPE_SEQ START WITH 100;

drop table if exists DUTY_TYPE_PERIOD cascade;


create table if not exists DUTY_TYPE_PERIOD
(
    ID_DUTY_TYPE INTEGER not null,
    START        TIME    not null,
    DURATION     INTEGER not null,
    constraint DUTY_TYPE_PERIOD_FQ
        foreign key (ID_DUTY_TYPE) references DUTY_TYPE (ID_DUTY_TYPE)
            on update cascade on delete cascade
);

drop table if exists DUTY_TYPE_DAYS_FROM_WEEKEND cascade;

create table if not exists DUTY_TYPE_DAYS_FROM_WEEKEND
(
    ID_DUTY_TYPE      INTEGER not null,
    DAYS_FROM_WEEKEND INTEGER,
    constraint DAYS_FROM_WEEKEND_DUTY_TYPE_FK
        foreign key (ID_DUTY_TYPE) references DUTY_TYPE (ID_DUTY_TYPE)
);

drop table if exists DUTY_TYPE_DAYS_TO_WEEKEND cascade;

create table if not exists DUTY_TYPE_DAYS_TO_WEEKEND
(
    ID_DUTY_TYPE    INTEGER not null,
    DAYS_TO_WEEKEND INTEGER,
    constraint DAYS_TO_WEEKEND_DUTY_TYPE_FK
        foreign key (ID_DUTY_TYPE) references DUTY_TYPE (ID_DUTY_TYPE)
);


INSERT INTO DUTY_TYPE (ID_DUTY_TYPE, DUTY_TYPE, FA_ICON, HTML_CLASS, PLAIN_TEXT, CREATED_DATE)
VALUES (0, 'Выходной', NULL, NULL, NULL, CURRENT_TIMESTAMP()),
       (1, 'Обычный рабочий день', NULL, NULL, NULL, CURRENT_TIMESTAMP()),
       (2, 'Рабочий день перед выходным', NULL, NULL, NULL, CURRENT_TIMESTAMP()),
       (3, 'ПОДКНО', NULL, NULL, 'X', CURRENT_TIMESTAMP());

INSERT INTO DUTY_TYPE_PERIOD (ID_DUTY_TYPE, START, DURATION)
VALUES (1, '9:00', 240),
       (1, '13:00', 255),
       (2, '9:00', 240),
       (2, '13:00', 240),
       (3, '9:00', 1440)
;

insert into DUTY_TYPE_DAYS_TO_WEEKEND (ID_DUTY_TYPE, DAYS_TO_WEEKEND)
VALUES (0, 0),
       (2, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5);

drop table if exists RANG cascade;

create table if not exists RANG
(
    ID_RANG      SMALLINT not null primary key,
    RANG         VARCHAR_IGNORECASE(15),
    FULL_NAME    VARCHAR_IGNORECASE(45),
    CREATED_DATE TIMESTAMP(26, 6)
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

drop table if exists PERSON_GROUP cascade;

create table if not exists PERSON_GROUP
(
    ID_PERSON_GROUP INTEGER                not null primary key,
    PERSON_GROUP    VARCHAR_IGNORECASE(45) not null,
    CREATED_DATE    TIMESTAMP,
    CONSTRAINT PERSON_GROUP_UNQ UNIQUE (PERSON_GROUP)
);
insert into PERSON_GROUP (ID_PERSON_GROUP, PERSON_GROUP, CREATED_DATE)
VALUES (1, '1 группа СО', CURRENT_TIMESTAMP()),
       (2, '2 отделение СО', CURRENT_TIMESTAMP()),
       (101, 'ПСОИ-1', CURRENT_TIMESTAMP());
create sequence if not exists PERSON_GROUP_SEQ start with 1000;

drop table if exists PERSON cascade;

create table if not exists PERSON
(
    ID_PERSON       VARCHAR(15) not null primary key,
    FIRST_NAME      VARCHAR_IGNORECASE(255),
    LAST_NAME       VARCHAR_IGNORECASE(255),
    MIDDLE_NAME     VARCHAR_IGNORECASE(255),
    POST            VARCHAR_IGNORECASE(255),
    ID_PERSON_GROUP INTEGER,
    ID_RANG         SMALLINT    not null,
    CREATED_DATE    TIMESTAMP,
    constraint PERSON_PERSON_GROUP_FK foreign key (ID_PERSON_GROUP) references PERSON_GROUP (ID_PERSON_GROUP),
    constraint PERSON_RANG_FQ foreign key (ID_RANG) references RANG (ID_RANG)
);

drop table if exists PERSON_DUTY_TYPE;
create table if not exists PERSON_DUTY_TYPE
(
    ID_PERSON    VARCHAR(15) not null,
    ID_DUTY_TYPE INTEGER     not null,
    primary key (ID_PERSON, ID_DUTY_TYPE),
    constraint PERSON_DUTY_TYPE_PERSON_FK foreign key (ID_PERSON) references PERSON (ID_PERSON) on update cascade on delete cascade,
    constraint PERSON_DUTY_TYPE_DUTY_TYPE_FK foreign key (ID_DUTY_TYPE) references DUTY_TYPE (ID_DUTY_TYPE) on update cascade on delete cascade
);
insert into PERSON (ID_PERSON, FIRST_NAME, LAST_NAME, MIDDLE_NAME, POST, ID_PERSON_GROUP, ID_RANG, CREATED_DATE)
values ('bochkov', 'Виктор', 'Бочков', 'Иванович', 'офицер', 2, 25, CURRENT_TIMESTAMP());
insert into PERSON_DUTY_TYPE (ID_PERSON, ID_DUTY_TYPE)
VALUES ('bochkov', 1),
       ('bochkov', 2),
       ('bochkov', 3);


drop table if exists DUTY_NEXT;
drop table if exists DUTY_PERIOD;
drop table if exists DUTY cascade;

create table if not exists DUTY
(
    ID_PERSON          VARCHAR(15) not null,
    DATE               DATE        not null,
    ID_DUTY_TYPE       INTEGER,
    CREATED_DATE       TIMESTAMP,
    CREATED_BY         VARCHAR(255),
    LAST_MODIFIED_BY   VARCHAR(255),
    LAST_MODIFIED_DATE TIMESTAMP,
    primary key (ID_PERSON, DATE),
    constraint DUTY_PERSON_FK foreign key (ID_PERSON) references PERSON (ID_PERSON),
    constraint DUTY_DUTY_TYPE_FQ foreign key (ID_DUTY_TYPE) references DUTY_TYPE (ID_DUTY_TYPE)
);

create table DUTY_NEXT
(
    ID_PERSON      VARCHAR(15) not null,
    DATE           DATE        not null,
    ID_PERSON_NEXT VARCHAR(15) not null,
    DATE_NEXT      DATE        not null,
    constraint DUTY_NEXT_CURRENT_FK foreign key (ID_PERSON, DATE) references DUTY (ID_PERSON, DATE) on update cascade on delete cascade,
    constraint DUTY_NEXT_FK foreign key (ID_PERSON_NEXT, DATE_NEXT) references DUTY (ID_PERSON, DATE) on update cascade on delete cascade
);

create table if not exists DUTY_PERIOD
(
    ID_PERSON VARCHAR not null,
    DATE      DATE    not null,
    START     TIME    NOT NULL,
    DURATION  INTEGER NOT NULL,
    primary key (ID_PERSON, DATE, START),
    constraint DUTY_PERIOD_FK foreign key (ID_PERSON, DATE) REFERENCES DUTY (ID_PERSON, DATE) on delete cascade on update cascade
);



drop table if exists REPORT cascade;

create table if not exists REPORT
(
    ID_REPORT                INTEGER      not null primary key,
    DATE_FROM                DATE         not null,
    DATE_TITLE               VARCHAR(255) not null,
    DATE_TO                  DATE         not null,
    GENITIVE_DEPARTMENT_NAME VARCHAR(255) not null,
    REPORT_TITLE             VARCHAR(255) not null,
    CHIEF                    VARCHAR(15)  not null,
    ID_DUTY_TYPE             VARCHAR(15)  not null,
    EXECUTOR                 VARCHAR(15)  not null,
    CREATED_DATE             TIMESTAMP,
    CREATED_BY               VARCHAR(255),
    LAST_MODIFIED_BY         VARCHAR(255),
    LAST_MODIFIED_DATE       TIMESTAMP,
    DATE                     DATE         not null,
    constraint REPORT_EXECUTOR_FK foreign key (EXECUTOR) references PERSON (ID_PERSON),
    constraint REPORT_CHIEF_FK foreign key (CHIEF) references PERSON (ID_PERSON),
    constraint REPORT_DUTY_TYPE_FK foreign key (ID_DUTY_TYPE) references DUTY_TYPE (ID_DUTY_TYPE)
);
create sequence if not exists REPORT_SEQ start with 100;
drop table if exists REPORT_PERSON;
create table REPORT_PERSON
(
    ID_REPORT INTEGER     not null,
    ID_PERSON VARCHAR(15) not null,
    primary key (ID_REPORT, ID_PERSON),
    constraint REPORT_PERSON_REPORT_FQ foreign key (ID_REPORT) references REPORT (ID_REPORT) on update cascade on delete cascade,
    constraint REPORT_PERSON_PERSON_FQ foreign key (ID_PERSON) references PERSON (ID_PERSON) on update cascade on delete cascade

);


ALTER SEQUENCE PUBLIC.DUTY_TYPE_SEQ RESTART WITH (SELECT COALESCE(MAX (ID_DUTY_TYPE)+1,1) FROM PUBLIC.DUTY_TYPE);
ALTER SEQUENCE PUBLIC.RANG_SEQ RESTART WITH (SELECT COALESCE(MAX (ID_RANG)+1,1) FROM PUBLIC.RANG);
ALTER SEQUENCE PERSON_GROUP_SEQ RESTART WITH (SELECT COALESCE(MAX (ID_PERSON_GROUP)+1,1) FROM PERSON_GROUP);
ALTER SEQUENCE REPORT_SEQ RESTART WITH (SELECT COALESCE(MAX (ID_REPORT)+1,100) FROM REPORT);


