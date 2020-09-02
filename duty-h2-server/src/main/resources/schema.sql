create table if not exists HOLIDAY
(
    YEAR         INTEGER,
    ID_HOLIDAY   INTEGER,
    TITLE        varchar_ignorecase(255) not null,
    CREATED_DATE TIMESTAMP,
    primary key (YEAR, ID_HOLIDAY)
);

create table if not exists HOLIDAY_DAY
(
    DATE         DATE primary key,
    YEAR         INTEGER,
    ID_HOLIDAY   INTEGER,
    HOLIDAY_TYPE varchar(15) not null,
    CREATED_DATE TIMESTAMP,
    constraint HOLIDAY_DATE_FK foreign key (YEAR, ID_HOLIDAY) references HOLIDAY (YEAR, ID_HOLIDAY)
);

create table IF NOT EXISTS EMPLOYEE_GROUP
(
    ID_EMPLOYEE_GROUP INTEGER                 not null primary key,
    EMPLOYEE_GROUP    VARCHAR_IGNORECASE(255) not null,
    CREATED_DATE      TIMESTAMP,
    constraint UNIQUE_EMPLOEE_GROUP_NAME unique (EMPLOYEE_GROUP)
);

create table IF NOT EXISTS RANG
(
    ID_RANG      SMALLINT                not null primary key,
    FULL_NAME    VARCHAR_IGNORECASE(255),
    RANG         VARCHAR_IGNORECASE(255) not null,
    CREATED_DATE TIMESTAMP,
    constraint UNIQUE_RANG_FULL_NAME unique (FULL_NAME),
    constraint UNIQUE_RANG_NAME unique (RANG)
);

create table IF NOT EXISTS EMPLOYEE
(
    ID_EMPLOYEE       VARCHAR(15) not null primary key,
    ID_RANG           SMALLINT    not null,
    FIRST_NAME        VARCHAR_IGNORECASE(255),
    LAST_NAME         VARCHAR_IGNORECASE(255),
    MIDDLE_NAME       VARCHAR_IGNORECASE(255),
    POST              VARCHAR_IGNORECASE(255),
    ROAD_TO_HOME_TIME INTEGER,
    ID_EMPLOYEE_GROUP INTEGER,
    CREATED_DATE      TIMESTAMP,
    constraint EMPLOYEE_EMPLOYEE_GROUP_FK foreign key (ID_EMPLOYEE_GROUP) references EMPLOYEE_GROUP (ID_EMPLOYEE_GROUP),
    constraint EMPLOYEE_RANG_FK foreign key (ID_RANG) references RANG (ID_RANG)
);

create table IF NOT EXISTS ROSTER
(
    ID_ROSTER                INTEGER                 not null primary key,
    DATE                     DATE                    not null,
    DATE_FROM                DATE                    not null,
    DATE_TITLE               VARCHAR(255)            not null,
    DATE_TO                  DATE                    not null,
    GENITIVE_DEPARTMENT_NAME VARCHAR_IGNORECASE(255) not null,
    ROSTER_TITLE             VARCHAR_IGNORECASE(255) not null,
    CHIEF                    VARCHAR_IGNORECASE(15)  not null,
    EXECUTOR                 VARCHAR_IGNORECASE(15)  not null,
    CREATED_BY               VARCHAR(15),
    CREATED_DATE             TIMESTAMP,
    LAST_MODIFIED_BY         VARCHAR(15),
    LAST_MODIFIED_DATE       TIMESTAMP,
    constraint ROSTER_CHIEF_FK foreign key (CHIEF) references EMPLOYEE (ID_EMPLOYEE),
    constraint ROSTER_EXECUTOR_FK foreign key (EXECUTOR) references EMPLOYEE (ID_EMPLOYEE)
);

create table IF NOT EXISTS ROSTER_EMPLOYEE
(
    ID_ROSTER   INTEGER     not null,
    ID_EMPLOYEE VARCHAR(15) not null,
    primary key (ID_ROSTER, ID_EMPLOYEE),
    constraint ROSTER_EMPLOYEE_ID_EMPLOYEE_FK
        foreign key (ID_EMPLOYEE) references EMPLOYEE (ID_EMPLOYEE),
    constraint ROSTER_EMPLOYEE_ID_ROSTER_FK foreign key (ID_ROSTER) references ROSTER (ID_ROSTER)
);

create table IF NOT EXISTS SHIFT_TYPE
(
    ID_SHIFT_TYPE INTEGER not null primary key,
    SHIFT_TYPE    VARCHAR_IGNORECASE(255),
    FA_ICON       VARCHAR(255),
    HTML_CLASS    VARCHAR(255),
    PLAIN_TEXT    VARCHAR(3),
    CREATED_DATE  TIMESTAMP,
    constraint UNIQUE_SHIFT_TYPE_NAME unique (SHIFT_TYPE)
);

create table IF NOT EXISTS DAY
(
    DATE                  DATE not null,
    DAYS_FROM_WEEKEND     INTEGER,
    DAYS_TO_WEEKEND       INTEGER,
    WEEKEND               BOOLEAN,
    NEXT                  DATE,
    ID_SHIFT_TYPE_DEFAULT INTEGER,
    CREATED_DATE          TIMESTAMP,
    primary key (DATE),
    constraint DAY_DEFAULT_SHIT_TYPE_ID_FK foreign key (ID_SHIFT_TYPE_DEFAULT) references SHIFT_TYPE (ID_SHIFT_TYPE),
    constraint NEXT_DAY_FK foreign key (NEXT) references DAY (DATE)
);

create table IF NOT EXISTS DAY_PERIOD
(
    DATE     DATE not null,
    DURATION INTEGER,
    START    TIME not null,
    constraint DAY_TIME_USAGE_FK foreign key (DATE) references DAY (DATE)
);

create table IF NOT EXISTS EMPLOYEE_SHIFT_TYPE
(
    ID_EMPLOYEE   VARCHAR(15) not null,
    ID_SHIFT_TYPE INTEGER     not null,
    primary key (ID_EMPLOYEE, ID_SHIFT_TYPE),
    constraint EMPLOYEE_SHIFT_TYPE_EMPLOYEE_FK foreign key (ID_EMPLOYEE) references EMPLOYEE (ID_EMPLOYEE),
    constraint EMPLOYEE_SHIFT_TYPE_SHIFT_TYPE_FK foreign key (ID_SHIFT_TYPE) references SHIFT_TYPE (ID_SHIFT_TYPE)
);

create table if not exists EMPLOYEE_SHIFT_TYPE_LIMIT
(
    ID_EMPLOYEE_SHIFT_TYPE_LIMIT BIGINT      not null primary key,
    ID_EMPLOYEE                  VARCHAR(15) not null,
    ID_SHIFT_TYPE                INTEGER     not null,
    DAYS_COUNT                   INTEGER,
    MAX                          INTEGER,
    MIN                          INTEGER,
    CREATED_DATE                 TIMESTAMP,
    constraint EMPLOYEE_SHIFT_TYPE_LIMIT_EMPLOYEE_ID_FK foreign key (ID_EMPLOYEE) references EMPLOYEE (ID_EMPLOYEE),
    constraint EMPLOYEE_SHIFT_TYPE_LIMUT_SHIFT_TYPE_ID_FK foreign key (ID_SHIFT_TYPE) references SHIFT_TYPE (ID_SHIFT_TYPE)
);

create table IF NOT EXISTS ROSTER_SHIFT_TYPE
(
    ID_ROSTER     INTEGER not null,
    ID_SHIFT_TYPE INTEGER not null,
    primary key (ID_ROSTER, ID_SHIFT_TYPE),
    constraint ROSTER_SHIFT_TYPE_SHIFT_TYPE_ID_FK foreign key (ID_SHIFT_TYPE) references SHIFT_TYPE (ID_SHIFT_TYPE),
    constraint ROSTER_SHIFT_TYPE_ROSTER_ID_FK foreign key (ID_ROSTER) references ROSTER (ID_ROSTER)
);

create table IF NOT EXISTS SHIFT
(
    ID_SHIFT           INTEGER not null primary key,
    ID_SHIFT_TYPE      INTEGER not null,
    CREATED_DATE       TIMESTAMP,
    CREATED_BY         VARCHAR(255),
    LAST_MODIFIED_BY   VARCHAR(255),
    LAST_MODIFIED_DATE TIMESTAMP,
    DATE               DATE    not null,
    constraint SHIFT_DAY_FK foreign key (DATE) references DAY (DATE)
);

create table IF NOT EXISTS SHIFT_EMPLOYEE
(
    ID_SHIFT    INTEGER     not null,
    ID_EMPLOYEE VARCHAR(15) not null,
    primary key (ID_SHIFT, ID_EMPLOYEE),
    constraint SHIFT_EMPLOYEE_SHIFT_ID_FK foreign key (ID_SHIFT) references SHIFT (ID_SHIFT),
    constraint SHIFT_EMPLOYEE_EMPLOYEE_ID_FK foreign key (ID_EMPLOYEE) references EMPLOYEE (ID_EMPLOYEE)
);

create table IF NOT EXISTS SHIFT_PERIOD
(
    ID_SHIFT INTEGER not null,
    DURATION INTEGER,
    START    TIME    not null,
    constraint SHIFT_PERIOD_FK foreign key (ID_SHIFT) references SHIFT (ID_SHIFT) on update cascade on delete cascade
);

create table IF NOT EXISTS SHIFT_TYPE_DAYS_FROM_WEEKEND
(
    ID_SHIFT_TYPE     INTEGER not null,
    DAYS_FROM_WEEKEND INTEGER,
    constraint DAYS_FROM_WEEKEND_SHIFT_TYPE_FK foreign key (ID_SHIFT_TYPE) references SHIFT_TYPE (ID_SHIFT_TYPE)
);

create table IF NOT EXISTS SHIFT_TYPE_DAYS_TO_WEEKEND
(
    ID_SHIFT_TYPE   INTEGER not null,
    DAYS_TO_WEEKEND INTEGER,
    constraint DAYS_TO_WEEKEND_SHIFT_TYPE_FK
        foreign key (ID_SHIFT_TYPE) references SHIFT_TYPE (ID_SHIFT_TYPE)
);

create table IF NOT EXISTS SHIFT_TYPE_PERIOD
(
    ID_SHIFT_TYPE INTEGER not null,
    START         TIME    not null,
    DURATION      INTEGER,
    primary key (ID_SHIFT_TYPE, START),
    constraint SHIFT_TYPE_PERIOD_FK foreign key (ID_SHIFT_TYPE) references SHIFT_TYPE (ID_SHIFT_TYPE)
);

create table IF NOT EXISTS VACATION
(
    ID_EMPLOYEE   VARCHAR(15) not null primary key,
    YEAR          INTEGER     not null,
    VACATION_TYPE VARCHAR(15) not null,
    CREATED_DATE  TIMESTAMP,
    constraint VACATION_EMPLOYEE_FK foreign key (ID_EMPLOYEE) references EMPLOYEE (ID_EMPLOYEE)
);

create table IF NOT EXISTS VACATION_PART
(
    ID_EMPLOYEE  VARCHAR(15) not null,
    YEAR         INTEGER     not null,
    START        DATE        not null,
    CREATED_DATE TIMESTAMP,
    END          DATE        not null,
    PART_NUMBER  INTEGER     not null,
    PRIMARY KEY (YEAR, ID_EMPLOYEE, START),
    constraint VACATION_PART foreign key (ID_EMPLOYEE, YEAR) references VACATION (ID_EMPLOYEE, YEAR)
);

create sequence if not exists SHIFT_TYPE_SEQ start with 100;
create sequence if not exists RANG_SEQ start with 50;
create sequence if not exists EMPLOYEE_GROUP_SEQ start with 1000;
create sequence if not exists ROSTER_SEQ start with 100;
create sequence if not exists SHIFT_SEQ start with 100;

create sequence if not exists EMPLOYEE_SHIFT_TYPE_LIMIT_SEQ start with 100;
