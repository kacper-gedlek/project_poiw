CREATE TABLE IF NOT EXISTS cards (
    id SERIAL PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL UNIQUE,
    name VARCHAR(100),
    surname VARCHAR(100),
    access_level INT DEFAULT 1,
    status VARCHAR(20) DEFAULT 'ACTIVE',
);
