/*insert into "genre" ("genre_nm") values ('Комедия');
insert into "genre" ("genre_nm") values ('Драма');
insert into "genre" ("genre_nm") values ('Мультфильм');
insert into "genre" ("genre_nm") values ('Триллер');
insert into "genre" ("genre_nm") values ('Документальный');
insert into "genre" ("genre_nm") values ('Боевик');

insert into "rating" ("rating_cd") values ('G');
insert into "rating" ("rating_cd") values ('PG');
insert into "rating" ("rating_cd") values ('PG-13');
insert into "rating" ("rating_cd") values ('R');
insert into "rating" ("rating_cd") values ('NC-17');

insert into "film" ("film_nm", "film_desc", "release_dt", "duration_min", "rating_id")
values ('1 film', '1 film desc', '2022-01-01', 180, 1);
insert into "film" ("film_nm", "film_desc", "release_dt", "duration_min", "rating_id")
values ('2 film', '2 film desc', '2021-01-01', 120, 2);
insert into "film" ("film_nm", "film_desc", "release_dt", "duration_min", "rating_id")
values ('3 film', '3 film desc', '2020-01-01', 150, 3);

insert into "film_x_genre" ("film_id", "genre_id")
values (1, 3);
insert into "film_x_genre" ("film_id", "genre_id")
values (2, 1);
insert into "film_x_genre" ("film_id", "genre_id")
values (3, 2);
insert into "film_x_genre" ("film_id", "genre_id")
values (3, 4);
insert into "film_x_genre" ("film_id", "genre_id")
values (3, 5);

insert into "user" ("email_txt", "login_nm", "user_nm", "birth_dt")
values ('user1@yandex.ru', 'user1', 'first user', '2000-01-01');
insert into "user" ("email_txt", "login_nm", "user_nm", "birth_dt")
values ('user2@yandex.ru', 'user2', 'second user', '2001-01-01');
insert into "user" ("email_txt", "login_nm", "user_nm", "birth_dt")
values ('user3@yandex.ru', 'user3', 'third user', '2002-01-01');
insert into "user" ("email_txt", "login_nm", "user_nm", "birth_dt")
values ('user4@yandex.ru', 'user4', 'forth user', '2003-01-01');

insert into "friend" ("user_id", "friend_user_id", "status_cd")
values (1,2,'accepted');
insert into "friend" ("user_id", "friend_user_id", "status_cd")
values (2,1,'accepted');
insert into "friend" ("user_id", "friend_user_id", "status_cd")
values (1,3,'requested');
insert into "friend" ("user_id", "friend_user_id", "status_cd")
values (2,3,'accepted');
insert into "friend" ("user_id", "friend_user_id", "status_cd")
values (3,2,'accepted');

insert into "like" ("film_id", "user_id")
values (1,1);
insert into "like" ("film_id", "user_id")
values (1,2);
insert into "like" ("film_id", "user_id")
values (1,3);
insert into "like" ("film_id", "user_id")
values (1,4);
insert into "like" ("film_id", "user_id")
values (2,1);
insert into "like" ("film_id", "user_id")
values (2,2);
insert into "like" ("film_id", "user_id")
values (2,4);
insert into "like" ("film_id", "user_id")
values (3,3);
*/