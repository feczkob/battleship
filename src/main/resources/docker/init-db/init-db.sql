CREATE DATABASE  IF NOT EXISTS `battleship` CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE USER 'user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON `battleship`.* TO `user`@`localhost`;

USE `battleship`;

CREATE TABLE IF NOT EXISTS `user` (
    `id` varchar(100) NOT NULL,
    `games_played_vs_ai` int(11) NOT NULL default '0',
    `games_played_vs_user` int(11) NOT NULL default '0',
    `games_won_vs_ai` int(11) NOT NULL default '0',
    `games_won_vs_user` int(11) NOT NULL default '0',
    `name` varchar(100) NOT NULL default 'Player1',
    PRIMARY KEY  (`id`)
    );

CREATE TABLE IF NOT EXISTS `room` (
    `id` int NOT NULL,
    `user_id` varchar(100) NOT NULL,
    PRIMARY KEY  (`id`)
    );


INSERT INTO user (id, games_played_vs_ai, games_played_vs_user, games_won_vs_ai, games_won_vs_user, name)
SELECT * FROM (SELECT 'robot' AS id, 0 AS games_played_vs_ai, 0 AS games_played_vs_user, 0 AS games_won_vs_ai, 0 AS games_won_vs_user, 'Robot' AS name) AS tmp
WHERE NOT EXISTS (
        SELECT ID FROM user WHERE id='robot'
    ) LIMIT 1;