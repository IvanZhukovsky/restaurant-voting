INSERT INTO MENU (RESTAURANT_ID, CREATE_DATE)
VALUES (1, NOW());
INSERT INTO MENU (RESTAURANT_ID, CREATE_DATE)
VALUES (2, NOW());

INSERT INTO DISH (RESTAURANT_ID, NAME, PRICE)
VALUES (2, 'компот из сухофруктов', 50.0),
       (2, 'пельмени из говядины', 380.0),
       (2, 'пельмени из говядины и свинины', 350.0),
       (2, 'соус томатный', 20.0);

INSERT INTO MENU_DISH (MENU_ID, DISH_ID)
VALUES (3, 1),
       (3, 2),
       (4, 7),
       (4, 8);