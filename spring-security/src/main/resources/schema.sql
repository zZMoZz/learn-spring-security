CREATE DATABASE security_db;

CREATE TABLE Person (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(200) NOT NULL,
);

CREATE TABLE Authorities (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Person (id)
);