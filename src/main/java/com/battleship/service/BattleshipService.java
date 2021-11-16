package com.battleship.service;

import com.battleship.game.Game;
import com.battleship.game.GameField;
import com.battleship.game.Robot;
import com.battleship.model.Room;
import com.battleship.model.ShootResponseDTO;
import com.battleship.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
     * Players with associated rooms
     */
    private final Map<String, Room> rooms = new HashMap<>();

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
     * @return room
     */
    public GameField createRoom(String userId){
        Optional<User> userTmp = userRepository.findById(userId);
        if (userTmp.isEmpty()) throw new RuntimeException("no.such.user");

        Room room = new Room(userTmp.get().getId());
        roomRepository.save(room);
        rooms.put(userId, room);

        synchronized (rooms.get(userId)){
            try {
                rooms.get(userId).wait();
            } catch (InterruptedException e){
                throw new RuntimeException("waiting.thread.interrupted");
            }
        }
        return games.get(userId).getGameField(userId);
    }

    /**
     * Delete a room
     * @param userId owner of room
     */
    public void leaveRoom(String userId) {
        roomRepository.deleteByUserId(userId);
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
                synchronized (rooms.get(roomUserId)){
                    rooms.get(roomUserId).notify();
                }
                roomRepository.deleteById(roomId);
                rooms.remove(roomUserId);
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
     * Get the list of rooms
     * @return list of rooms
     */
    public List<Room> getRooms(){
        return roomRepository.findAll();
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
        shootResponseDTO.setGameField2(game.shoot(userId, fieldId));
        shootResponseDTO.setPlayer2(game.getOtherPlayer(userId));
        if(game.getOtherPlayer(userId).equals("robot"))  {
            Integer field2 = Robot.shoot();
            game.shoot("robot", field2);
        }
        shootResponseDTO.setGameField1(game.getGameField(userId));
        shootResponseDTO.setFinished(game.getIsFinished());
        shootResponseDTO.setWinner(game.getWinner());

        return shootResponseDTO;
    }

}
