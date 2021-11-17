# battleship
Project for the Integration of Information Systems course

#The game <br />
Battleship (also Battleships or Sea Battle) is a strategy type guessing game for two players. 
It is played on ruled grids (paper or board) on which each player's fleet of ships (including battleships) are marked. 
The locations of the fleets are concealed from the other player. Players alternate turns calling "shots" at the other player's ships, 
and the objective of the game is to destroy the opposing player's fleet. <br />

When the game starts the player is given an initial state of their ships: 
- 2 sized: 5 pcs
- 3 sized: 4 pcs
- 4 sized: 2 pcs
- 5 sized: 1 pcs <br/>

They can ask for new positions via a call to the corresponding API endpoint.

#Features
1. Create new user if not exist
2. Find user by Id
3. Get all users
4. Change username
5. Create room: other player can join for a multiplayer game or the owner can leave
6. Get all rooms
7. Start a single player game vs. a naive robot
8. Start a multiplayer game vs. a real user
9. Shoot at a specific field: multiple shots at different fields from one player are disabled

#Commands related to the app
To run the app:
  1. ```docker-compose up``` in the ```\battleship\src\main\resources\docker``` folder.
  2. ```mvn spring-boot:run``` in the ```battleship``` folder.

To enter the MySQL container:  ```docker exec -it database mysql -uroot -ppassword``` in the ```battleship``` folder. <br />
```mysql> use battleship;``` <br />
```mysql> select * from room;``` or ```mysql> select * from user;``` <br />
For other possible commands click [here](http://g2pc1.bu.edu/~qzpeng/manual/MySQL%20Commands.htm). <br />

#Useful links
[Battleship](https://en.wikipedia.org/wiki/Battleship_(game)) <br/>
[Docker for Windows](https://docs.docker.com/desktop/windows/install/) <br />
If Docker Engine does not start, try [this](https://docs.microsoft.com/hu-hu/windows/wsl/install-manual#step-4---download-the-linux-kernel-update-package). <br />
[MySQL in Docker](https://www.javainuse.com/devOps/docker/docker-mysql) <br />
The repository for the frontend app can be found [here](https://github.com/boit2009/Battleship). <br />