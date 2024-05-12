-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    birthdate DATE,
    password VARCHAR(100) NOT NULL,
    image VARCHAR(255),
    updated_at TIMESTAMP DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des films
CREATE TABLE IF NOT EXISTS movies (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    director VARCHAR(100),
    release_date DATE,
    synopsis TEXT,
    poster_url VARCHAR(255),
    updated_at TIMESTAMP DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des lieux
CREATE TABLE IF NOT EXISTS places (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    image_Url VARCHAR(255),
    opening_hours TEXT,
    updated_at TIMESTAMP DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Table des avis
CREATE TABLE IF NOT EXISTS reviews (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    full_name VARCHAR(255),
    email VARCHAR(255),
    entity_id INT NOT NULL,
    entity_type VARCHAR(50) NOT NULL, -- Peut être 'movie' ou 'place'
    rating INT NOT NULL,
    summary TEXT,
    review TEXT,
    image VARCHAR(255),
    updated_at TIMESTAMP DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);



-- Ajouter une contrainte d'unicité pour s'assurer qu'un utilisateur ne peut laisser 
-- qu'un seul avis par entité
ALTER TABLE reviews ADD CONSTRAINT unique_user_entity UNIQUE (user_id, entity_id, entity_type);
