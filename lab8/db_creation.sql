



CREATE TABLE users (
    login VARCHAR(50) PRIMARY KEY,
    salt VARCHAR(32) NOT NULL,
    password BYTEA   NOT NULL
);

CREATE TYPE dragon_color AS ENUM ('BLACK', 'YELLOW', 'ORANGE', 'BROWN');
CREATE TYPE dragon_type AS ENUM ('WATER', 'UNDERGROUND', 'AIR', 'FIRE');
CREATE TYPE dragon_character AS ENUM ('EVIL', 'CHAOTIC_EVIL', 'FICKLE');

CREATE TABLE dragons (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL CHECK (name <> ''),
    x DOUBLE PRECISION NOT NULL,
    y BIGINT NOT NULL CHECK (y <= 984),
    creation_date DATE NOT NULL DEFAULT CURRENT_DATE,
    age INTEGER NOT NULL CHECK (age > 0),
    color dragon_color NOT NULL,
    type dragon_type NOT NULL,
    character dragon_character,
    depth INTEGER,
    number_of_treasures DOUBLE PRECISION CHECK (number_of_treasures > 0),
    login VARCHAR(50) NOT NULL REFERENCES users(login),

    CHECK (
        (depth IS NULL AND number_of_treasures IS NULL) OR
        (depth IS NOT NULL AND number_of_treasures IS NOT NULL)
    )
);
