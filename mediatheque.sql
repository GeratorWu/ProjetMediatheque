-- Création de la base de données
CREATE DATABASE mediatheque;

-- Utilisation de la base de données
USE mediatheque;

-- Détruire les tables;
DROP TABLE Abonne, DVD;

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

-- Insertion dans les tables
INSERT INTO Abonne VALUES
    (1, 'Wu', 'Patrick', '2003-02-18'),
    (2, 'Petit', 'Pat', '2010-06-20'),
    (3, 'Presque', 'Patoche', '2007-03-29'),
    (4, 'Dubois', 'Pierre', '1988-06-20'),
    (5, 'Lefebvre', 'Sophie', '2001-12-03'),
    (6, 'Martin', 'Antoine', '1976-09-28');

INSERT INTO DVD (idDvd, titre, adulte, emprunteur, reserveur) VALUES
    (1, 'Le Seigneur des Anneaux : La Communauté de l Anneau', 0, NULL, NULL),
    (2, 'Pulp Fiction', 1, 1, NULL),
    (3, 'Fight Club', 1, NULL, NULL),
    (4, 'La La Land', 0, 3, NULL),
    (5, 'Le Roi Lion', 0, NULL, NULL),
    (6, 'Matrix', 1, NULL, NULL),
    (7, 'Star Wars : L Empire contre-attaque', 0, 3, NULL),
    (8, 'Forrest Gump', 0, NULL, NULL),
    (9, 'Le Silence des Agneaux', 1, NULL, NULL),
    (10, 'Les Évadés', 0, NULL, NULL);

-- ajout contrainte
ALTER TABLE DVD ADD CONSTRAINT emprunt_reserve CHECK (
    (emprunteur IS NULL OR reserveur IS NULL) OR
    (emprunteur IS NOT NULL AND reserveur IS NULL) OR
    (emprunteur IS NULL AND reserveur IS NOT NULL)
);

-- COMMIT
COMMIT;