DROP DATABASE IF EXISTS userapi;;    

CREATE DATABASE userapi;    

\c userapi;        

CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    name varchar NOT NULL,
    email varchar(320) NOT NULL UNIQUE,
    createdAt varchar NOT NULL,
    updatedAt varchar,
    token varchar
);