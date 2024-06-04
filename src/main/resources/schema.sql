CREATE TABLE IF NOT EXISTS users
(
    id
    UUID
    PRIMARY
    KEY,
    name
    VARCHAR
(
    255
) NOT NULL,
    username VARCHAR
(
    255
) NOT NULL UNIQUE,
    age INT NOT NULL,
    email VARCHAR
(
    255
) NOT NULL UNIQUE,
    password VARCHAR
(
    255
) NOT NULL
    );