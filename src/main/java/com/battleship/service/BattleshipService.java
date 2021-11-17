package com.battleship.service;

import com.battleship.game.Game;
import com.battleship.game.GameField;
import com.battleship.game.Robot;
import com.battleship.model.Room;
import com.battleship.model.ShootResponseDTO;
import com.battleship.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class
 */
@Service
public class BattleshipService {
    private final UserRepository userRepository;

    /**
     * Repository for rooms - maybe not needed
     */
    private final RoomRepository roomRepository;

    /**
     * Players with associated games
     */
    private final Map<String, Game> games = new HashMap<>();

    /**
     * Players with associated threads
     */
    private final Map<String, Thread> threads = new HashMap<>();

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

                synchronized (threads.get(roomUserId)){
                    threads.get(roomUserId).notify();
                }
                roomRepository.deleteById(roomId);

                break;
            case "robot":
                game = new Game(userId);
                games.put(userId, game);
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
        //TODO
        return games.get(userId).getGameField(userId);
    }

    /**
     * Declare themselves ready for the game
     * @param userId Id of the player
     * @return true
     */
    public boolean ready(String userId) {
        return games.get(userId).ready(userId);
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
        ShootResponseDTO shootResponseDTO = new ShootResponseDTO();

        shootResponseDTO.setPlayer1(userId);
        try {
            shootResponseDTO.setGameField2(game.shoot(userId, fieldId));
        } catch (RuntimeException e){
            shootResponseDTO.setGameField2(game.getOpponentGameField(userId));
            shootResponseDTO.setPlayer2(game.getOtherPlayer(userId));
            shootResponseDTO.setGameField1(game.getGameField(userId));
            return shootResponseDTO;
        }

        if(game.getOtherPlayer(userId).equals("robot"))  {
            Integer field2 = Robot.shoot();
            game.shoot("robot", field2);
        }

        shootResponseDTO.setPlayer2(game.getOtherPlayer(userId));
        shootResponseDTO.setGameField1(game.getGameField(userId));
        if(game.getIsFinished()){
            Optional<User> userTmp = userRepository.findById(game.getWinner());
            if(userTmp.isEmpty())  throw new RuntimeException("no.such.user");
            shootResponseDTO.setFinished(game.getIsFinished());
            shootResponseDTO.setWinner(game.getWinner());
            if(game.getOtherPlayer(userId).equals("robot")){
                userTmp.get().setGamesPlayedVsAi(userTmp.get().getGamesPlayedVsAi() + 1);
                if(game.getWinner().equals(userId)) userTmp.get().setGamesWonVsAi(userTmp.get().getGamesWonVsAi() + 1);
            } else {
                userTmp.get().setGamesPlayedVsUser(userTmp.get().getGamesPlayedVsUser() + 1);
                Optional<User> userTmp2 = userRepository.findById(game.getOtherPlayer(userId));
                if(userTmp2.isEmpty())  throw new RuntimeException("no.such.user");
                userTmp2.get().setGamesPlayedVsUser(userTmp2.get().getGamesPlayedVsUser() + 1);
                if(game.getWinner().equals(userTmp.get().getId()))  {
                    userTmp.get().setGamesWonVsUser(userTmp.get().getGamesWonVsUser() + 1);
                } else {
                    userTmp2.get().setGamesWonVsUser(userTmp.get().getGamesWonVsUser() + 1);
                }
            }
        }


        return shootResponseDTO;
    }
}
