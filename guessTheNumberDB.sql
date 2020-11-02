DROP DATABASE IF EXISTS guessTheNumberDB;
CREATE DATABASE guessTheNumberDB;
USE guessTheNumberDB;

CREATE TABLE game (
    game_id INT PRIMARY KEY AUTO_INCREMENT,
    answer char(4),
    finished BOOLEAN DEFAULT false
    );

CREATE TABLE round (
    round_id INT NOT NULL,
    game_id INT NOT NULL,
    guess_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    guess CHAR(4),
    result CHAR(7),
    CONSTRAINT pk_round PRIMARY KEY (round_id,game_id),
    FOREIGN KEY fk_gameid (game_id) REFERENCES game(game_id)
    );
    