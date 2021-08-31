DROP TABLE IF EXISTS JUGADOR;
DROP TABLE IF EXISTS TIRADA;

CREATE TABLE JUGADOR(
	ID INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(255),
	DATA_ENTRADA DATE,
	ENCERTS DOUBLE
	);
CREATE TABLE TIRADA(
	ID INT PRIMARY KEY AUTO_INCREMENT,
	GUANYA BOOLEAN,
	TIRADA1 INT(2),
	TIRADA2 INT(2),
	JUGADOR_ID INT NOT NULL,
	FOREIGN KEY (JUGADOR_ID) REFERENCES JUGADOR(ID)
	);