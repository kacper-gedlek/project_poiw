CREATE TABLE IF NOT EXISTS cards (
    id SERIAL PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL UNIQUE,
    name VARCHAR(100),
    surname VARCHAR(100),
    access_level INT NOT NULL DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS readers (
    id SERIAL PRIMARY KEY,
    reader_number VARCHAR(16) NOT NULL UNIQUE,
    required_access_level INT NOT NULL DEFAULT 0
);
