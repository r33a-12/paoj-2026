-- =============================================
-- Platforma E-Learning — Schema Baza de Date
-- =============================================
-- Rulează acest script pentru a crea/recrea tabelele.

DROP TABLE IF EXISTS scoruri;
DROP TABLE IF EXISTS materiale;
DROP TABLE IF EXISTS cursuri;
DROP TABLE IF EXISTS utilizatori;

-- Tabel unic pentru ierarhia Utilizator -> MembruPlatforma -> Student/Instructor
-- (Single Table Inheritance — coloana 'rol' diferentiază tipul)
CREATE TABLE utilizatori (
    id              INT PRIMARY KEY,
    nume            VARCHAR(100) NOT NULL,
    rol             VARCHAR(20)  NOT NULL,
    data_inscriere  VARCHAR(30),
    specializare    VARCHAR(100)
);

CREATE TABLE cursuri (
    id    INT PRIMARY KEY,
    titlu VARCHAR(200) NOT NULL
);

-- Materiale: tipul ('VIDEO' / 'QUIZ') determină ce câmpuri sunt populate
CREATE TABLE materiale (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    curs_id         INT NOT NULL,
    tip             VARCHAR(20)  NOT NULL,
    titlu           VARCHAR(200) NOT NULL,
    durata_minute   INT,
    nr_intrebari    INT,
    FOREIGN KEY (curs_id) REFERENCES cursuri(id) ON DELETE CASCADE
);

-- Scoruri asociate cursurilor
CREATE TABLE scoruri (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    curs_id         INT NOT NULL,
    id_referinta    VARCHAR(100) NOT NULL,
    punctaj         INT NOT NULL,
    FOREIGN KEY (curs_id) REFERENCES cursuri(id) ON DELETE CASCADE
);
