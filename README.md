# _REST API для проекта "Voting for Restaurants"_

### _Язык реализации Java_

В рамках дипломного проекта разработано REST API для приложения Voting for restaurants (англ. «Голосование за рестораны»). Оно
предоставляет
возможность
голосовать за меню дня в каком либо ресторане.

###  _Technical requirement:_

Design and implement a REST API using Hibernate/Spring/SpringMVC (Spring-Boot preferred!) **without frontend**.

The task is:

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote for a restaurant they want to have lunch at today
* Only one vote counted per user
* If user votes again the same day:
  - If it is before 11:00 we assume that he changed his mind.
  - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

As a result, provide a link to github repository. It should contain the code, README.md with API documentation and couple curl commands to test it (**better - link to Swagger**).

-----------------------------
P.S.: Make sure everything works with latest version that is on github :)  
P.P.S.: Assume that your API will be used by a frontend developer to build frontend on top of that.

-----------------------------

### _Приложение представляет собой сервис для голосования:_

- Сервис — имеет следующие возможности.
  - **Публичные** (_доступны для всех пользователей_)
    - Регистрация;
    - Просмотр результатов голосования;
  - **Приватные** (_доступны только для зарегистрированных пользователей_)
    - Управление аккаунтом;
    - Просмотр ресторанов и меню доступных для голосования;
  - **Административные** (_доступны только для зарегистрированных пользователей правами администратора_)
    - Управление пользователями;
    - Управление ресторанами;
    - Управление блюдами доступными в данных ресторанах;
    - Управление меню;

### _[Спецификация REST API swagger](http://localhost:8080/swagger-ui/index.html)_

