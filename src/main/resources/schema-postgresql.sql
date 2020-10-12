CREATE DATABASE userapi;

DROP TABLE [IF EXISTS] users;
 
CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    name varchar NOT NULL,
    email varchar(320) NOT NULL,
    createdAt varchar NOT NULL,
    updatedAt varchar,
    token varchar
);