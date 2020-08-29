MERGE INTO PUBLIC.RANG (ID_RANG, CREATED_DATE, FULL_NAME, RANG)
    VALUES (11, '2018-11-18 18:32:09.727000', 'Мичман', 'м-н'),
           (12, '2018-11-18 18:32:09.749000', 'Прапорщик', 'пр-к'),
           (13, '2018-11-18 18:32:09.751000', 'Старший Мичман', 'с.м-н'),
           (14, '2018-11-18 18:32:09.753000', 'Старший Прапорщик', 'с.п-к'),
           (15, '2018-11-18 18:32:09.755000', 'Младший лейтенант', 'м.л-т'),
           (17, '2018-11-18 18:32:09.756000', 'Лейтенант', 'л-т'),
           (19, '2018-11-18 18:32:09.758000', 'Старший лейтенант', 'с.л-т'),
           (21, '2018-11-18 18:32:09.759000', 'Капитан лейтенант', 'к.л-т'),
           (22, '2018-11-18 18:32:09.761000', 'Капитан', 'к-н'),
           (23, '2018-11-18 18:32:09.762000', 'Капитан 3-го ранга', 'к.3р'),
           (24, '2018-11-18 18:32:09.764000', 'Майор', 'м-р'),
           (25, '2018-11-18 18:32:09.765000', 'Капитан 2-го ранга', 'к.2р'),
           (26, '2018-11-18 18:32:09.767000', 'Подполковник', 'п.п-к'),
           (27, '2018-11-18 18:32:09.769000', 'Капитан 1-го ранга', 'к.1р'),
           (28, '2018-11-18 18:32:09.770000', 'Полковник', 'п-к');


MERGE INTO SHIFT_TYPE (ID_SHIFT_TYPE, SHIFT_TYPE, FA_ICON, HTML_CLASS, PLAIN_TEXT, CREATED_DATE)
    KEY (ID_SHIFT_TYPE)
    VALUES (0, 'Выходной', NULL, NULL, NULL, CURRENT_TIMESTAMP()),
           (1, 'Обычный рабочий день', NULL, NULL, NULL, CURRENT_TIMESTAMP()),
           (2, 'Рабочий день перед выходным', NULL, NULL, NULL, CURRENT_TIMESTAMP()),
           (3, 'ПОДКНО', NULL, NULL, 'X', CURRENT_TIMESTAMP()),
           (4, '30%', NULL, NULL, '30%', CURRENT_TIMESTAMP()),
           (5, 'ОД', NULL, NULL, 'ОД', CURRENT_TIMESTAMP())
;

MERGE INTO SHIFT_TYPE_PERIOD (ID_SHIFT_TYPE, START, DURATION)
    KEY (ID_SHIFT_TYPE, START)
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
