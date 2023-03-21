-- Création de la base de données
CREATE DATABASE mediatheque;

-- Utilisation de la base de données
USE mediatheque;

-- Détruire les tables;
DROP TABLE abonne, dvd;

-- Création de la table abonne
CREATE TABLE abonne (
    idAbonne INT PRIMARY KEY,
    nom VARCHAR(255),
    dateNaissance DATE
);

-- Création de la table DVD
CREATE TABLE dvd (
    idDvd INT PRIMARY KEY,
    titre VARCHAR(255),
    adulte BOOLEAN,
    emprunteur INT,
    reserveur INT,
    FOREIGN KEY (emprunteur) REFERENCES abonne(idAbonne),
    FOREIGN KEY (reserveur) REFERENCES abonne(idAbonne)
);


-- COMMIT
COMMIT;