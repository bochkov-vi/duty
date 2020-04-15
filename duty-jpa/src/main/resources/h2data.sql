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


insert into PERSON_GROUP (ID_PERSON_GROUP, PERSON_GROUP, CREATED_DATE)
VALUES (1, '1 группа СО', CURRENT_TIMESTAMP()),
       (2, '2 отделение СО', CURRENT_TIMESTAMP()),
       (101, 'ПСОИ-1', CURRENT_TIMESTAMP());


merge into PERSON (ID_PERSON, FIRST_NAME, LAST_NAME, MIDDLE_NAME, POST, ID_PERSON_GROUP, ID_RANG, CREATED_DATE)
    key (ID_PERSON)
    values ('bochkov', 'Виктор', 'Бочков', 'Иванович', 'офицер', 2, 25, CURRENT_TIMESTAMP()),
           ('pisarenko', 'Иван', 'Писаренко', 'Иванович', 'офицер', 2, 22, CURRENT_TIMESTAMP()),
           ('skabina', 'Дмитрий', 'Скабина', 'Васильевич', 'офицер', 2, 23, CURRENT_TIMESTAMP()),
           ('konstantinov', 'Егор', 'Константинов', 'Леонидович', 'офицер', 2, 23, CURRENT_TIMESTAMP());

MERGE INTO PUBLIC.PERSON (ID_PERSON, FIRST_NAME, LAST_NAME, MIDDLE_NAME, POST, ID_PERSON_GROUP, ID_RANG,
                          CREATED_DATE) key (ID_PERSON)
    VALUES ('bochkov', 'Борис', 'Бочков', 'Борисович', 'офицер', 2, 25, '2020-04-14 03:43:01.002906'),
           ('afanasyev', 'Агафон', 'Афанасьев', 'Абрамович', 'офицер', 2, 23, '2020-04-14 03:43:01.002906'),
           ('vasukov', 'Владимир', 'Васюков', 'Викторович', 'офицер', 2, 23, '2020-04-14 03:43:01.002906'),
           ('gashporenko', 'Григорий', 'Гашпоренко', 'Геннадьевич', 'офицер', 2, 23, '2020-04-14 03:43:01.002906'),
           ('demidov', 'Денис', 'Демидов', 'Дмитриевич', 'офицер', 2, 23, '2020-04-14 03:43:01.002906');

merge into PERSON_DUTY_TYPE (ID_PERSON, ID_DUTY_TYPE) key (ID_PERSON, ID_DUTY_TYPE)
    VALUES ('bochkov', 0),
           ('bochkov', 1),
           ('bochkov', 2),
           ('bochkov', 3),
           ('afanasyev', 0),
           ('afanasyev', 1),
           ('afanasyev', 2),
           ('afanasyev', 3),
           ('vasukov', 0),
           ('vasukov', 1),
           ('vasukov', 2),
           ('vasukov', 3),
           ('gashporenko', 0),
           ('gashporenko', 1),
           ('gashporenko', 2),
           ('gashporenko', 3),
           ('demidov', 0),
           ('demidov', 1),
           ('demidov', 2),
           ('demidov', 3);
