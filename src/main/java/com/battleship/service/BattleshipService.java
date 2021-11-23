package com.battleship.service;

import com.battleship.game.Game;
import com.battleship.game.GameField;
import com.battleship.game.Robot;
import com.battleship.model.LeaderboardDTO;
import com.battleship.model.Room;
import com.battleship.model.ShootResponseDTO;
import com.battleship.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service class
 */
@Service
public class BattleshipService {
    private final UserRepository userRepository;

    /**
     * Repository for rooms
     */
    private final RoomRepository roomRepository;

    /**
     * Players with associated games
     * concurrent map
     */
    private final ConcurrentHashMap<String, Game> games = new ConcurrentHashMap<>();

    /**
     * Players with associated threads
     */
    private final ConcurrentHashMap<String, Thread> threads = new ConcurrentHashMap<>();

    /**
     * Players with associated robots in single player mode
     */
    private final ConcurrentHashMap<String, Robot> robots = new ConcurrentHashMap<>();

    /**
     * Injection of repositories
     * @param userRepository repository of users
     * @param roomRepository repository of rooms
     */
    @Autowired
    public BattleshipService(UserRepository userRepository, RoomRepository roomRepository){
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    /**
     * Find user by Id
     * @param Id Id of user
     * @return user
     */
    public User findById(String Id){
        if(userRepository.findById(Id).isPresent()) {
            return userRepository.findById(Id).get();
        } else {
            User userTmp = new User(Id, "Player1");
            userRepository.save(userTmp);
            return userTmp;
        }
    }

    /**
     * Find all users
     * @return list of users
     */
    public List<User> findAll(){
        return userRepository.findAll();
    }

    /**
     * Save user to the repository
     * @param user user to be saved
     * @return user
     */
    public User save(User user){
        return userRepository.save(user);
    }

    /**
     * Change username
     * @param Id Id of user
     * @param newUsername new username
     * @return user
     */
    public User changeUsername(String Id, String newUsername){
        Optional<User> userTmp = userRepository.findById(Id);
        if(userTmp.isPresent()){
            userTmp.get().setName(newUsername);
            userRepository.save(userTmp.get());
            return userTmp.get();
        }
        throw new RuntimeException("user.not.found");
    }

    /**
     * Create a room
     * @param userId owner of room
     * @return initial state of the game field or null if the player leaves
     */
    public GameField createRoom(String userId){
        Optional<User> userTmp = userRepository.findById(userId);
        if (userTmp.isEmpty()) throw new RuntimeException("no.such.user");

        Room room = new Room(userTmp.get().getId());
        roomRepository.save(room);

        threads.put(userId, Thread.currentThread());
        synchronized (Thread.currentThread()){
            try {
                Thread.currentThread().wait();
            } catch (InterruptedException e){
                return null;
            }
        }
        threads.remove(userId);
        return games.get(userId).getGameField(userId);
    }

    /**
     * Get the list of rooms
     * @return list of rooms
     */
    public List<Room> getRooms(){
        return roomRepository.findAll();
    }

    /**
     * Delete a room
     * @param userId owner of room
     */
    @Transactional
    public void leaveRoom(String userId) {
        threads.get(userId).interrupt();
        roomRepository.deleteByUserId(userId);
        threads.remove(userId);
    }

    /**
     * Leave a game
     * @param userId user who leaves
     */
    public void leaveGame(String userId) {
        Game game = games.get(userId);
        threads.remove(userId);
        if(threads.get(game.getOtherPlayer(userId)) != null)    threads.get(game.getOtherPlayer(userId)).interrupt();
        threads.remove(game.getOtherPlayer(userId));
        if(game.getOtherPlayer(userId).equals("robot")) robots.remove(userId);
        game.setLeft(userId);
    }

    /**
     * Start a game
     * @param opponent "user" or "robot"
     * @param roomId Id of the room in case of multiplayer game
     * @param userId user who joins the room or starts a single player game
     * @return initial state of the game field
     */
    public GameField play(String opponent, Long roomId, String userId){
        Game game;
        if(userRepository.findById(userId).isEmpty()) throw new RuntimeException("no.such.user");
        switch (opponent){
            case "user":
                Optional<Room> room = roomRepository.findById(roomId);
                if(room.isEmpty()) throw new RuntimeException("room.not.exist");

                String roomUserId = room.get().getUserId();
                game = new Game(roomUserId, userId);
                games.put(roomUserId, game);
                games.put(userId, game);
                synchronized (threads.get(roomUserId)) {
                    threads.get(roomUserId).notify();
                }
                roomRepository.deleteById(roomId);
                break;
            case "robot":
                game = new Game(userId);
                games.put(userId, game);
                Robot robot = new Robot(game);
                robots.put(userId, robot);
                break;
            default:
                throw new RuntimeException("no.such.game.mode");

        }
        return game.getGameField(userId);
    }

    /**
     * Get newly positioned ships
     * @param userId Id of the player
     * @return new game field
     */
    public GameField getNewShipPositions(String userId) {
        return games.get(userId).getNewShipPositions(userId);
    }

    /**
     * Declare themselves ready for the game
     * @param userId Id of the player
     * @return true
     */
    public ShootResponseDTO ready(String userId) {
        Game game = games.get(userId);
        String opponent = game.getOtherPlayer(userId);
        ShootResponseDTO shootResponseDTO = new ShootResponseDTO();
        shootResponseDTO.setPlayer1(userId);
        shootResponseDTO.setPlayer2(opponent);

        // if the opponent left before I called ready
        if(game.getLeft().contains(opponent)) return getShootResponseDTOWhenPlayerLeft(userId, opponent, shootResponseDTO);


        if(!opponent.equals("robot")) {
            threads.put(userId, Thread.currentThread());
            if (threads.get(opponent) == null) {
                synchronized (threads.get(userId)) {
                    try {
                        Thread.currentThread().wait();
                    } catch (InterruptedException e) {
                        // if the opponent left while I was waiting for them
                        return getShootResponseDTOWhenPlayerLeft(userId, opponent, shootResponseDTO);
                    }
                }
            } else {
                synchronized (threads.get(opponent)) {
                    threads.get(opponent).notify();
                }
            }
            threads.remove(userId);
        }
        shootResponseDTO.setGameField1(game.ready(userId));
        shootResponseDTO.setGameField2(game.getOpponentGameField(userId));

        return shootResponseDTO;
    }

    /**
     * Function for returning DTO in case of a player leaves
     * @param userId the other player is the winner
     * @param opponent opponent leaves
     * @param shootResponseDTO DTO to return
     * @return DTO
     */
    private ShootResponseDTO getShootResponseDTOWhenPlayerLeft(String userId, String opponent, ShootResponseDTO shootResponseDTO) {
        shootResponseDTO.setWinner(userId);
        shootResponseDTO.setFinished(true);
        Optional<User> user1 = userRepository.findById(userId);
        user1.ifPresent(user -> user.setGamesPlayedVsUser(user.getGamesPlayedVsUser() + 1));
        user1.ifPresent(user -> user.setGamesWonVsUser(user.getGamesWonVsUser() + 1));
        user1.ifPresent(userRepository::save);
        Optional<User> user2 = userRepository.findById(opponent);
        user2.ifPresent(user -> user.setGamesPlayedVsUser(user.getGamesPlayedVsUser() + 1));
        user2.ifPresent(userRepository::save);
        games.remove(games.get(userId).getOtherPlayer(userId));
        games.remove(userId);
        return shootResponseDTO;
    }

    /**
     * Shoot at a specific field
     * @param userId user who shoots
     * @param fieldId field to be shot at
     * @return response with the outcomes
     */
    public ShootResponseDTO shoot(String userId, int fieldId) {
        if(userRepository.findById(userId).isEmpty()) throw new RuntimeException("no.such.user");
        Game game = games.get(userId);
        if(game == null) throw new RuntimeException("no.such.game");

        String opponent = game.getOtherPlayer(userId);
        ShootResponseDTO shootResponseDTO = new ShootResponseDTO();
        shootResponseDTO.setPlayer1(userId);
        shootResponseDTO.setPlayer2(opponent);

        // if the opponent left before I called shoot
        if(game.getLeft().contains(opponent)) return getShootResponseDTOWhenPlayerLeft(userId, opponent, shootResponseDTO);

        if(game.getOtherPlayer(userId).equals("robot"))  {
            robots.get(userId).shoot();
            shootResponseDTO.setGameField2(game.shoot(userId, fieldId));
        } else {
            if (threads.get(opponent) == null) {
                if (threads.get(userId) != null) {
                    // multiple shots
                    shootResponseDTO.setGameField2(game.getOpponentGameField(userId));
                    shootResponseDTO.setGameField1(game.getGameField(userId));
                    return shootResponseDTO;
                }
                threads.put(userId, Thread.currentThread());
                synchronized (Thread.currentThread()) {
                    shootResponseDTO.setGameField2(game.shoot(userId, fieldId));
                    try {
                        Thread.currentThread().wait();
                    } catch (InterruptedException e) {
                        // if the opponent left while I was waiting for them
                        return getShootResponseDTOWhenPlayerLeft(userId, opponent, shootResponseDTO);
                    }
                }
            } else {
                synchronized (threads.get(opponent)) {
                    shootResponseDTO.setGameField2(game.shoot(userId, fieldId));
                    threads.get(opponent).notify();
                    threads.remove(opponent);
                }
            }
        }

        shootResponseDTO.setGameField1(game.getGameField(userId));


        if(game.getIsFinished()){
            Optional<User> userTmp = userRepository.findById(userId);
            if(userTmp.isEmpty())  throw new RuntimeException("no.such.user");
            shootResponseDTO.setFinished(game.getIsFinished());
            shootResponseDTO.setWinner(game.getWinner());
            if(opponent.equals("robot")){
                userTmp.get().setGamesPlayedVsAi(userTmp.get().getGamesPlayedVsAi() + 1);
                if(game.getWinner().equals(userId)) userTmp.get().setGamesWonVsAi(userTmp.get().getGamesWonVsAi() + 1);
                robots.remove(userId);
            } else {
                userTmp.get().setGamesPlayedVsUser(userTmp.get().getGamesPlayedVsUser() + 1);
                if(game.getWinner().equals(userTmp.get().getId()))  userTmp.get().setGamesWonVsUser(userTmp.get().getGamesWonVsUser() + 1);
            }
            games.remove(userId);
            userRepository.save(userTmp.get());
        }
        return shootResponseDTO;
    }


    /**
     * Get leaderboard
     * @param opponent "user" or "robot"
     * @return leaderboard DTO
     */
    public LeaderboardDTO getLeaderboard(String opponent) {
        return "robot".equals(opponent) ? new LeaderboardDTO(userRepository.findAllByOrderByGamesWonVsAiDesc()) :
                new LeaderboardDTO(userRepository.findAllByOrderByGamesWonVsUserDesc());
    }

}
