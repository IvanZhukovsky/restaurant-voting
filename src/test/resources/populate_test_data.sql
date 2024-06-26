DELETE from VOTE;

DELETE from USER_ROLE;

DELETE from USERS;
ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;

INSERT INTO USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD)
VALUES ('user@gmail.com', 'User_First', 'User_Last', '$2a$10$5OU8ISWS4OrOatYozhXeCuihssYs/ZBsQ9sgUtma6CMBd.h7SXcD.'),
       ('admin@javaops.ru', 'Admin_First', 'Admin_last', '$2a$10$MTMrh7X8UslRu2Dq5srro.d7oBjJQDz7b66xjViVyxsRwh4y0YCb.');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);


DELETE FROM RESTAURANT;
ALTER TABLE RESTAURANT ALTER COLUMN ID RESTART WITH 1;

INSERT INTO RESTAURANT (NAME) VALUES ('Test restaurant 1');
INSERT INTO RESTAURANT (NAME) VALUES ('Test restaurant 2');

DELETE from DISH;
ALTER TABLE DISH ALTER COLUMN ID RESTART WITH 1;

INSERT INTO DISH (RESTAURANT_ID, NAME, PRICE)
VALUES (1, 'черный чай с сахаром', 10.5),
       (1, 'суп гороховый', 70.0),
       (1, 'салат мимоза', 30.3),
       (1, 'чай с травами', 60.5);

DELETE from MENU;
ALTER TABLE MENU ALTER COLUMN ID RESTART WITH 1;

INSERT INTO MENU (RESTAURANT_ID, CREATE_DATE)
VALUES (1, '2024-04-16 10:00:00');
INSERT INTO MENU (RESTAURANT_ID, CREATE_DATE)
VALUES (1, '2024-04-21 10:00:00');

DELETE from MENU_DISH;

INSERT INTO MENU_DISH (MENU_ID, DISH_ID)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 3),
       (2, 3),
       (2, 4);
