CREATE TABLE "film" (
  "film_id" integer PRIMARY KEY,
  "film_nm" varchar,
  "film_desc" varchar,
  "release_dt" date,
  "duration_min" integer,
  "rating_cd" varchar
);

CREATE TABLE "like" (
  "film_id" integer,
  "user_id" integer,
  PRIMARY KEY ("film_id", "user_id")
);

CREATE TABLE "film_x_genre" (
  "film_id" integer,
  "genre_id" integer,
  PRIMARY KEY ("film_id", "genre_id")
);

CREATE TABLE "genre" (
  "genre_id" integer PRIMARY KEY,
  "genre_nm" varchar
);

CREATE TABLE "user" (
  "user_id" integer PRIMARY KEY,
  "email_txt" varchar,
  "login_nm" varchar,
  "user_nm" varchar,
  "birth_dt" date
);

CREATE TABLE "friend" (
  "user_id" integer,
  "friend_user_id" integer,
  "status_cd" varchar,
  PRIMARY KEY ("user_id", "friend_user_id")
);

ALTER TABLE "film_x_genre" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("film_id");

ALTER TABLE "film_x_genre" ADD FOREIGN KEY ("genre_id") REFERENCES "genre" ("genre_id");

ALTER TABLE "like" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("film_id");

ALTER TABLE "like" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id");

ALTER TABLE "friend" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("user_id");

ALTER TABLE "friend" ADD FOREIGN KEY ("friend_user_id") REFERENCES "user" ("user_id");
