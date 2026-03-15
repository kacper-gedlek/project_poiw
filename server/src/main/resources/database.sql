CREATE TABLE IF NOT EXISTS cards (
    id SERIAL PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL UNIQUE,
    card_owner VARCHAR(200),
    access_level INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS readers (
    id SERIAL PRIMARY KEY,
    reader_number VARCHAR(16) NOT NULL UNIQUE,
    reader_name VARCHAR(100),
    required_access_level INT NOT NULL DEFAULT 0
);
