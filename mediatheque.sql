-- Création de la base de données
CREATE DATABASE mediatheque;

-- Utilisation de la base de données
USE mediatheque;

-- Détruire les tables;
DROP TABLE Abonne, DVD, Livre;

-- Création de la table Abonne
CREATE TABLE Abonne (
    idAbonne INT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50),
    dateNaissance DATE NOT NULL
);

-- Création de la table DVD
CREATE TABLE DVD (
    idDvd INT PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    adulte BOOLEAN,
    emprunteur INT,
    reserveur INT,
    FOREIGN KEY (emprunteur) REFERENCES abonne(idAbonne),
    FOREIGN KEY (reserveur) REFERENCES abonne(idAbonne)
);

-- Création de la table Live
CREATE TABLE Livre (
    idLiv INT PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    emprunteur INT,
    reserveur INT,
    FOREIGN KEY (emprunteur) REFERENCES abonne(idAbonne),
    FOREIGN KEY (reserveur) REFERENCES abonne(idAbonne)
);

-- Insertion dans les tables
INSERT INTO Abonne VALUES
    (1, 'Durand', 'Marie', '1995-02-15'),
    (2, 'Dubois', 'Pierre', '1988-06-20'),
    (3, 'Lefebvre', 'Sophie', '2001-12-03'),
    (4, 'Martin', 'Antoine', '1976-09-28');

INSERT INTO DVD (idDvd, titre, adulte, emprunteur, reserveur) VALUES
    (1, 'Le Seigneur des Anneaux : La Communauté de l Anneau', 0, NULL, NULL),
    (2, 'Pulp Fiction', 1, 1, NULL),
    (3, 'Fight Club', 1, NULL, 3),
    (4, 'La La Land', 0, NULL, 4),
    (5, 'Le Roi Lion', 0, NULL, 1),
    (6, 'Matrix', 1, 2, NULL),
    (7, 'Star Wars : L Empire contre-attaque', 0, NULL, NULL),
    (8, 'Forrest Gump', 0, 2, NULL),
    (9, 'Le Silence des Agneaux', 1, NULL, NULL),
    (10, 'Les Évadés', 0, NULL, NULL);

INSERT INTO Livre (idLiv, titre, emprunteur, reserveur) VALUES
    (1, 'Le Petit Prince', 3, NULL),
    (2, 'Harry Potter à l école des sorciers', NULL, NULL),
    (3, 'Les Misérables', 2, NULL),
    (4, '1984', NULL, 3),
    (5, 'Le Hobbit', NULL, 4),
    (6, 'L Alchimiste', NULL, NULL),
    (7, 'Le Comte de Monte-Cristo', 2, NULL),
    (8, 'La Nuit des temps', NULL, 1),
    (9, 'Le Cycle de Dune', 2, NULL),
    (10, 'Le Cycle Fondation', NULL, NULL);
-- ajout contrainte
ALTER TABLE DVD ADD CONSTRAINT emprunt_reserve CHECK (
    (emprunteur IS NULL OR reserveur IS NULL) AND
    (emprunteur IS NOT NULL AND reserveur IS NULL) OR
    (emprunteur IS NULL AND reserveur IS NOT NULL)
);

ALTER TABLE Livre ADD CONSTRAINT emprunt_reserve CHECK (
    (emprunteur IS NULL OR reserveur IS NULL) AND
    (emprunteur IS NOT NULL AND reserveur IS NULL) OR
    (emprunteur IS NULL AND reserveur IS NOT NULL)
);
-- COMMIT
COMMIT;
