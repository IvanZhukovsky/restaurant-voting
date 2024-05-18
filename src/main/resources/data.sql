INSERT INTO USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD)
VALUES ('user@gmail.com', 'User_First', 'User_Last',
        '$2a$10$TG8ZznZ1srJZa6Vegvq/XuKdcdrslMHALOxDzY20wbpWOvl.YReoy'),
       ('admin@javaops.ru', 'Admin_First', 'Admin_Last',
        '$2a$10$MTMrh7X8UslRu2Dq5srro.d7oBjJQDz7b66xjViVyxsRwh4y0YCb.'),
    ('guest@mail.ru', 'Guest_First', 'Guest_Last',
    '$2a$10$MTMrh7X8UslRu2Dq5srro.d7oBjJQDz7b66xjViVyxsRwh4y0YCb.');
INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2),
       ('USER', 3);

INSERT INTO RESTAURANT (NAME)
VALUES ('Щепка');

INSERT INTO MENU (RESTAURANT_ID, AVAILABLE_DATE)
VALUES (1, '2024-07-10');

INSERT INTO MENU (RESTAURANT_ID, AVAILABLE_DATE)
VALUES (1, '2024-07-11');

INSERT INTO MENU_DISH (MENU_ID, NAME, PRICE)
VALUES (1, 'черный чай с сахаром', 10.5),
       (1, 'суп гороховый', 70.0),
--         (1, 'суп гороховый', 7),
       (1, 'салат мимоза', 30.30),
       (1, 'чай с травами', 60.5);

INSERT INTO MENU_DISH (MENU_ID, NAME, PRICE)
VALUES (2, 'чай с молоком', 30.1),
       (2, 'борщ', 15.4),
       (2, 'горчица', 10.0),
       (2, 'пирожное', 7.58);


INSERT INTO RESTAURANT (NAME)
VALUES ('Хинкальная');

INSERT INTO MENU (RESTAURANT_ID, AVAILABLE_DATE)
VALUES (2, '2024-07-10');

INSERT INTO MENU (RESTAURANT_ID, AVAILABLE_DATE)
VALUES (2, '2024-07-11');


INSERT INTO MENU_DISH (MENU_ID, NAME, PRICE)
VALUES (3, 'хинкали с бараниной', 300.8),
       (3, 'соус сацебели', 50.5),
       (3, 'хачапури', 100.0),
       (3, 'капучино', 100.25),
       (3, 'торт', 78.55);

INSERT INTO MENU_DISH (MENU_ID, NAME, PRICE)
VALUES (4, 'хинкали со свининой', 250.5),
       (4, 'соус томатный', 46.4),
       (4, 'латте', 150.25),
       (4, 'конфета', 72.5);

INSERT INTO MENU (RESTAURANT_ID, AVAILABLE_DATE)
VALUES (1, NOW());

INSERT INTO MENU_DISH (MENU_ID, NAME, PRICE)
VALUES (5, 'чай с молоком', 30.1),
       (5, 'борщ', 15.4),
       (5, 'горчица', 10.0),
       (5, 'пирожное', 7.58);

INSERT INTO MENU (RESTAURANT_ID, AVAILABLE_DATE)
VALUES (2, NOW());

INSERT INTO MENU_DISH (MENU_ID, NAME, PRICE)
VALUES (6, 'хинкали со свининой', 250.5),
       (6, 'соус томатный', 46.4),
       (6, 'латте', 150.25),
       (6, 'конфета', 72.5);

INSERT INTO VOTE (CREATED_AT, RESTAURANT_ID, USER_ID)
VALUES (NOW(),1,3);

-- INSERT INTO VOTE (CREATED_AT, RESTAURANT_ID, USER_ID)
-- VALUES (NOW(),1,2)



-- INSERT INTO MENU (RESTAURANT_ID, CREATE_DATE)
-- VALUES (2, NOW());
--
-- INSERT INTO MENU_DISH (MENU_ID, DISH_ID)
-- VALUES (2, 5),
--        (2, 6),
--        (2, 8);
--
-- INSERT INTO RESTAURANT (NAME)
-- VALUES ('KFC');
--
-- INSERT INTO DISH (RESTAURANT_ID, NAME, PRICE)
-- VALUES (3, 'острые крылья', 170.5),
--        (3, 'картофель фри', 120.0),
--        (3, 'апельсиновый сок', 70.0),
--        (3, 'гамбургер с сыром', 130.0),
--        (3, 'картофель по деревенски', 120.0),
--        (3, 'сырный соус', 88.0);
--
-- INSERT INTO MENU (RESTAURANT_ID, CREATE_DATE)
-- VALUES (3, NOW());
--
-- INSERT INTO MENU_DISH (MENU_ID, DISH_ID)
-- VALUES (3, 10),
--        (3, 11),
--        (3, 12);
--
-- INSERT INTO RESTAURANT (NAME)
-- VALUES ('Панорама');
--
-- INSERT INTO DISH (RESTAURANT_ID, NAME, PRICE)
-- VALUES (4, 'виски', 100.0),
--        (4, 'стек рибай', 1000.0),
--        (4, 'перечный соус', 250.0),
--        (4, 'овощи гриль', 450.0),
--        (4, 'острый соус', 88.0);
--
-- INSERT INTO MENU (RESTAURANT_ID, CREATE_DATE)
-- VALUES (4, NOW());
--
-- INSERT INTO MENU_DISH (MENU_ID, DISH_ID)
-- VALUES (4, 17),
--        (4, 18),
--        (4, 16);
--
--
-- INSERT INTO RESTAURANT (NAME)
-- VALUES ('Пельменная');
--
-- INSERT INTO DISH (RESTAURANT_ID, NAME, PRICE)
-- VALUES (5, 'компот из сухофруктов', 50.0),
--        (5, 'пельмени из говядины', 380.0),
--        (5, 'пельмени из говядины и свинины', 350.0),
--        (5, 'соус томатный', 20.0);
--
--
-- INSERT INTO MENU (RESTAURANT_ID, CREATE_DATE)
-- VALUES (5, NOW());
--
-- INSERT INTO MENU_DISH (MENU_ID, DISH_ID)
-- VALUES (5, 21),
--        (5, 22),
--        (5, 24);




