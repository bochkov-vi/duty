create table DAY
(
    DATE              DATE not null,
    CREATED_DATE      TIMESTAMP,
    DAYS_FROM_WEEKEND INTEGER,
    DAYS_TO_WEEKEND   INTEGER,
    WEEKEND           BOOLEAN,
    NEXT              DATE,
    primary key (),
    constraint NEXT_DAY_FK
        foreign key () references DAY (DATE)
);

create table DAY_PERIOD
(
    DATE     DATE not null,
    DURATION INTEGER,
    START    TIME not null,
    constraint DAY_TIME_USAGE_FK
        foreign key () references DAY (DATE)
);

create table DUTY
(
    DATE               DATE        not null,
    ID_PERSON          VARCHAR(15) not null
        primary key,
    CREATED_DATE       TIMESTAMP,
    CREATED_BY         VARCHAR(255),
    LAST_MODIFIED_BY   VARCHAR(255),
    LAST_MODIFIED_DATE TIMESTAMP,
    ID_DUTY_TYPE       VARCHAR(15) not null,
    constraint DUTY_DAY_FK
        foreign key () references DAY (DATE)
);

create table DUTY_NEXT
(
    NEXT           DATE,
    ID_PERSON_NEXT VARCHAR(15),
    DATE           DATE        not null,
    ID_PERSON      VARCHAR(15) not null
        primary key,
    constraint FKCLDCVN8NW9DOMVJS7YVQJXGRK
        foreign key (ID_PERSON_NEXT) references DUTY (DATE, ID_PERSON),
    constraint FKRLL23ADAY0QNNHD2OENL5A0ND
        foreign key (ID_PERSON) references DUTY (DATE, ID_PERSON)
);

create table DUTY_PERIOD
(
    DATE      DATE        not null,
    ID_PERSON VARCHAR(15) not null,
    DURATION  INTEGER,
    START     TIME        not null,
    constraint DUTY_PERIOD_FK
        foreign key (ID_PERSON) references DUTY (ID_PERSON, DATE)
            on update cascade on delete cascade
);

create table DUTY_TYPE
(
    ID_DUTY_TYPE VARCHAR(15) not null
        primary key,
    CREATED_DATE TIMESTAMP,
    FA_ICON      VARCHAR(255),
    HTML_CLASS   VARCHAR(255),
    PLAIN_TEXT   VARCHAR(3)
);

create table DAYS_FROM_WEEKEND
(
    DUTYTYPE_ID_DUTY_TYPE VARCHAR(15) not null,
    DAYS_FROM_WEEKEND     INTEGER,
    constraint FK45V40OF2RNSLN8SPPTY223L2C
        foreign key (DUTYTYPE_ID_DUTY_TYPE) references DUTY_TYPE (ID_DUTY_TYPE)
);

create table DAYS_TO_WEEKEND
(
    DUTYTYPE_ID_DUTY_TYPE VARCHAR(15) not null,
    DAYS_TO_WEEKEND       INTEGER,
    constraint FKDPA4VRGEX15RVPGSTACC5N1U1
        foreign key (DUTYTYPE_ID_DUTY_TYPE) references DUTY_TYPE (ID_DUTY_TYPE)
);

create table DUTY_TYPE_PERIOD
(
    ID_DUTY_TYPE VARCHAR(15) not null,
    DURATION     INTEGER,
    START        TIME        not null,
    constraint DUTY_TYPE_PERIOD_FQ
        foreign key (ID_DUTY_TYPE) references DUTY_TYPE (ID_DUTY_TYPE)
            on update cascade on delete cascade
);

create table PERSON_GROUP
(
    ID_PERSON_GROUP INTEGER      not null
        primary key,
    CREATED_DATE    TIMESTAMP,
    PERSON_GROUP    VARCHAR(255) not null
);

create table RANG
(
    ID_RANG      SMALLINT     not null
        primary key,
    CREATED_DATE TIMESTAMP,
    FULL_NAME    VARCHAR(255),
    RANG         VARCHAR(255) not null
);

create table REPORT
(
    ID_REPORT                INTEGER      not null
        primary key,
    CREATED_DATE             TIMESTAMP,
    CREATED_BY               VARCHAR(255),
    LAST_MODIFIED_BY         VARCHAR(255),
    LAST_MODIFIED_DATE       TIMESTAMP,
    DATE                     DATE         not null,
    DATE_FROM                DATE         not null,
    DATE_TITLE               VARCHAR(255) not null,
    DATE_TO                  DATE         not null,
    GENITIVE_DEPARTMENT_NAME VARCHAR(255) not null,
    REPORT_TITLE             VARCHAR(255) not null,
    CHIEF                    VARCHAR(15)  not null,
    ID_DUTY_TYPE             VARCHAR(15)  not null,
    EXECUTOR                 VARCHAR(15)  not null,
    constraint FKN2EJG69KNUGWCCRYY1OTFJFD8
        foreign key (ID_DUTY_TYPE) references DUTY_TYPE (ID_DUTY_TYPE)
);

create table REPORT_PERSON
(
    ID_REPORT INTEGER     not null,
    ID_PERSON VARCHAR(15) not null,
    primary key (ID_REPORT, ID_PERSON),
    constraint FK4U93IGMI3PHCUQMTOJNRDMBBE
        foreign key (ID_REPORT) references REPORT (ID_REPORT)
);

