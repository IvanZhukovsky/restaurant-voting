INSERT INTO USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD)
VALUES ('user@gmail.com', 'User_First', 'User_Last', 'password'),
       ('admin@javaops.ru', 'Admin_First', 'Admin_last',
        '$2a$10$MTMrh7X8UslRu2Dq5srro.d7oBjJQDz7b66xjViVyxsRwh4y0YCb.');
INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (NAME)
VALUES ('Щепка');

INSERT INTO DISH (RESTAURANT_ID, NAME, PRICE)
VALUES (1, 'черный чай с сахаром', 10.5),
       (1, 'суп гороховый', 70.0),
       (1, 'салат мимоза', 30.3),
       (1, 'чай с травами', 60.5);

INSERT INTO MENU (RESTAURANT_ID, CREATE_DATE)
VALUES (1, NOW());
INSERT INTO MENU (RESTAURANT_ID, CREATE_DATE)
VALUES (1, '2024-04-16 10:00:00');
INSERT INTO MENU (RESTAURANT_ID, CREATE_DATE)
VALUES (1, NOW());



INSERT INTO MENU (RESTAURANT_ID, CREATE_DATE)
VALUES (1, NOW());

INSERT INTO MENU_DISH (MENU_ID, DISH_ID)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (2, 4);



INSERT INTO RESTAURANT (NAME)
VALUES ('Хинкальная №1');

INSERT INTO DISH (RESTAURANT_ID, NAME, PRICE)
VALUES (2, 'хинкали с бараниной', 300.8),
       (2, 'соус сацебели', 50.5),
       (2, 'соус хачапури', 100.0),
       (2, 'капучино', 100.25),
       (2, 'торт', 78.55);

INSERT INTO RESTAURANT (NAME)
VALUES ('KFC');

INSERT INTO DISH (RESTAURANT_ID, NAME, PRICE)
VALUES (3, 'острые крылья', 170.5),
       (3, 'картофель фри', 120.0),
       (3, 'апельсиновый сок', 70.0),
       (3, 'гамбургер с сыром', 130.0),
       (3, 'картофель по деревенски', 120.0),
       (3, 'сырный соус', 88.0);

INSERT INTO RESTAURANT (NAME)
VALUES ('Панорама');

INSERT INTO DISH (RESTAURANT_ID, NAME, PRICE)
VALUES (4, 'виски', 100.0),
       (4, 'стек рибай', 1000.0),
       (4, 'перечный соус', 250.0),
       (4, 'овощи гриль', 450.0),
       (4, 'острый соус', 88.0);

INSERT INTO RESTAURANT (NAME)
VALUES ('Пельменная');

INSERT INTO DISH (RESTAURANT_ID, NAME, PRICE)
VALUES (5, 'компот из сухофруктов', 50.0),
       (5, 'пельмени из говядины', 380.0),
       (5, 'пельмени из говядины и свинины', 350.0),
       (5, 'соус томатный', 20.0);


INSERT INTO MENU (RESTAURANT_ID, CREATE_DATE)
VALUES (1, NOW());
INSERT INTO MENU (RESTAURANT_ID, CREATE_DATE)
VALUES (2, NOW());
INSERT INTO MENU (RESTAURANT_ID, CREATE_DATE)
VALUES (3, NOW());




