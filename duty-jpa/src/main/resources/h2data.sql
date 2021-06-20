MERGE INTO SHIFT_TYPE (ID_SHIFT_TYPE, SHIFT_TYPE, ICON, HTML_CLASS, LABEL, CREATED_DATE)
    KEY (ID_SHIFT_TYPE)
    VALUES (0, 'Выходной', NULL, NULL, NULL, CURRENT_TIMESTAMP()),
           (1, 'Обычный рабочий день', NULL, NULL, NULL, CURRENT_TIMESTAMP()),
           (2, 'Рабочий день перед выходным', NULL, NULL, NULL, CURRENT_TIMESTAMP()),
           (3, 'ПОДКНО', NULL, NULL, 'X', CURRENT_TIMESTAMP()),
           (4, '30%', NULL, NULL, '30%', CURRENT_TIMESTAMP()),
           (5, 'ОД', NULL, NULL, 'ОД', CURRENT_TIMESTAMP())
;

MERGE INTO SHIFT_TYPE_PERIOD (ID_SHIFT_TYPE, START, DURATION)
    VALUES (1, '9:00', 240),
           (1, '13:00', 255),
           (2, '9:00', 240),
           (2, '13:00', 240),
           (3, '9:00', 1440),
           (4, '9:00', 240),
           (4, '13:00', 255),
           (5, '9:00', 1500)
;

merge into SHIFT_TYPE_DAYS_TO_WEEKEND (ID_SHIFT_TYPE, DAYS_TO_WEEKEND)
    KEY (ID_SHIFT_TYPE, DAYS_TO_WEEKEND)
    VALUES (0, 0),
           (2, 1),
           (1, 2),
           (1, 3),
           (1, 4),
           (1, 5);


merge into EMPLOYEE_GROUP (ID_EMPLOYEE_GROUP, EMPLOYEE_GROUP, CREATED_DATE)
    key (ID_EMPLOYEE_GROUP)
    VALUES (0, 'Другой ОТД', CURRENT_TIMESTAMP()),
           (1, '1 группа СО', CURRENT_TIMESTAMP()),
           (2, '2 отделение СО', CURRENT_TIMESTAMP()),
           (101, 'ПСОИ-1', CURRENT_TIMESTAMP());


merge into EMPLOYEE (ID_EMPLOYEE, FIRST_NAME, LAST_NAME, MIDDLE_NAME, POST, ID_EMPLOYEE_GROUP, ID_RANG, CREATED_DATE,
                   ROAD_TO_HOME_TIME)
    key (ID_EMPLOYEE)
    values ('bochkov', 'Виктор', 'Бочков', 'Иванович', 'офицер', 2, 25, CURRENT_TIMESTAMP(), 15),
           ('pisarenko', 'Иван', 'Писаренко', 'Иванович', 'офицер', 2, 22, CURRENT_TIMESTAMP(), 20),
           ('skabina', 'Дмитрий', 'Скабина', 'Васильевич', 'офицер', 2, 23, CURRENT_TIMESTAMP(), 25),
           ('konstantinov', 'Егор', 'Константинов', 'Леонидович', 'офицер', 2, 23, CURRENT_TIMESTAMP(), 30);

MERGE INTO PUBLIC.EMPLOYEE (ID_EMPLOYEE, FIRST_NAME, LAST_NAME, MIDDLE_NAME, POST, ID_EMPLOYEE_GROUP, ID_RANG,
                          CREATED_DATE, ROAD_TO_HOME_TIME) key (ID_EMPLOYEE)
    VALUES ('bochkov', 'Борис', 'Бочков', 'Борисович', 'офицер', 2, 25, '2020-04-14 03:43:01.002906', 35),
           ('afanasyev', 'Агафон', 'Афанасьев', 'Абрамович', 'офицер', 2, 23, '2020-04-14 03:43:01.002906', 75),
           ('vasukov', 'Владимир', 'Васюков', 'Викторович', 'офицер', 2, 23, '2020-04-14 03:43:01.002906', 90),
           ('gashporenko', 'Григорий', 'Гашпоренко', 'Геннадьевич', 'офицер', 2, 23, '2020-04-14 03:43:01.002906', 120),
           ('demidov', 'Денис', 'Демидов', 'Дмитриевич', 'офицер', 2, 23, '2020-04-14 03:43:01.002906', 125);

merge into EMPLOYEE_SHIFT_TYPE (ID_EMPLOYEE, ID_SHIFT_TYPE) key (ID_EMPLOYEE, ID_SHIFT_TYPE)
    VALUES ('bochkov', 0),
           ('bochkov', 1),
           ('bochkov', 2),
           ('bochkov', 3),
           ('bochkov', 4),
           ('afanasyev', 0),
           ('afanasyev', 1),
           ('afanasyev', 2),
           ('afanasyev', 3),
           ('afanasyev', 4),
           ('vasukov', 0),
           ('vasukov', 1),
           ('vasukov', 2),
           ('vasukov', 3),
           ('vasukov', 4),
           ('gashporenko', 0),
           ('gashporenko', 1),
           ('gashporenko', 2),
           ('gashporenko', 3),
           ('gashporenko', 4),
           ('demidov', 0),
           ('demidov', 1),
           ('demidov', 2),
           ('demidov', 3),
           ('demidov', 4),
           ('konstantinov', 4),
           ('pisarenko', 4),
           ('skabina', 4),
           ('konstantinov', 3),
           ('pisarenko', 3),
           ('skabina', 3);




MERGE INTO PUBLIC.EMPLOYEE (ID_EMPLOYEE, FIRST_NAME, LAST_NAME, MIDDLE_NAME, POST, ID_EMPLOYEE_GROUP, ID_RANG,
                          CREATED_DATE, ROAD_TO_HOME_TIME) key (ID_EMPLOYEE)
    VALUES ('od1', '1щв', 'Оперативный деж 1', '1од', 'оперативный дежурный', 1, 23, '2020-04-14 03:43:01.002906', 20)
         , ('od2', '1щв', 'Оперативный деж 2', '1од', 'оперативный дежурный', 1, 23, '2020-04-14 03:43:01.002906', 20)
         , ('od3', '1щв', 'Оперативный деж 3', '1од', 'оперативный дежурный', 1, 23, '2020-04-14 03:43:01.002906', 20)
         , ('od4', '1щв', 'Оперативный деж 4', '1од', 'оперативный дежурный', 1, 23, '2020-04-14 03:43:01.002906', 20);

MERGE INTO PUBLIC.EMPLOYEE (ID_EMPLOYEE, FIRST_NAME, LAST_NAME, MIDDLE_NAME, POST, ID_EMPLOYEE_GROUP, ID_RANG,
                          CREATED_DATE, ROAD_TO_HOME_TIME) key (ID_EMPLOYEE)
    VALUES ('nod1', '-', 'Нешт. од 1', '-', 'нешт. оперативный дежурный', 1, 23, '2020-04-14 03:43:01.002906', 20)
         , ('nod2', '-', 'Нешт. од 2', '-', 'нешт. оперативный дежурный', 1, 23, '2020-04-14 03:43:01.002906', 20)
         , ('nod3', '-', 'Нешт. од 3', '-', 'нешт. оперативный дежурный', 1, 23, '2020-04-14 03:43:01.002906', 20)
         , ('nod4', '-', 'Нешт. од 4', '-', 'нешт. оперативный дежурный', 1, 23, '2020-04-14 03:43:01.002906', 20)
         , ('nod5', '-', 'Нешт. од 5', '-', 'нешт. оперативный дежурный', 1, 23, '2020-04-14 03:43:01.002906', 20);

merge into EMPLOYEE_SHIFT_TYPE (ID_EMPLOYEE, ID_SHIFT_TYPE) key (ID_EMPLOYEE, ID_SHIFT_TYPE)
    VALUES ('od1', 5)
         , ('od2', 5)
         , ('od3', 5)
         , ('od4', 5)
         , ('nod1', 5)
         , ('nod2', 5)
         , ('nod3', 5)
         , ('nod4', 5)
         , ('nod5', 5)
;
ALTER SEQUENCE PUBLIC.SHIFT_TYPE_SEQ RESTART WITH (SELECT COALESCE(MAX (ID_SHIFT_TYPE)+1,1) FROM PUBLIC.SHIFT_TYPE);
ALTER SEQUENCE PUBLIC.RANG_SEQ RESTART WITH (SELECT COALESCE(MAX (ID_RANG)+1,1) FROM PUBLIC.RANG);
ALTER SEQUENCE EMPLOYEE_GROUP_SEQ RESTART WITH (SELECT COALESCE(MAX (ID_EMPLOYEE_GROUP)+1,1) FROM EMPLOYEE_GROUP);
ALTER SEQUENCE ROSTER_SEQ RESTART WITH (SELECT COALESCE(MAX (ID_ROSTER)+1,100) FROM ROSTER);
