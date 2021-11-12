package com.battleship.service;

import com.battleship.game.GRIDSTATE;
import com.battleship.game.Game;
import com.battleship.game.GameField;
import com.battleship.game.Robot;
import com.battleship.model.Room;
import com.battleship.model.ShootResponseDTO;
import com.battleship.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BattleshipService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    private final Map<String, Game> games = new HashMap<>();

    public User findById(String Id){
        if(userRepository.findById(Id).isPresent()) {
            return userRepository.findById(Id).get();
        } else {
            User userTmp = new User(Id, "Player1");
            userRepository.save(userTmp);
            return userTmp;
        }
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public User changeUsername(String Id, String newUsername){
        Optional<User> userTmp = userRepository.findById(Id);
        if(userTmp.isPresent()){
            userTmp.get().setName(newUsername);
            userRepository.save(userTmp.get());
            return userTmp.get();
        }
        throw new RuntimeException("user.not.found");
    }

    public Room createRoom(String userId){
        Optional<User> userTmp = userRepository.findById(userId);
        if (userTmp.isPresent()){
            Room room = new Room(userTmp.get().getId());
            roomRepository.save(room);
            return room;
        }
        else throw new RuntimeException("no.such.user");
    }

    public GameField play(String opponent, Long roomId, String userId){
        Game game;
        switch (opponent){
            case "user":
                if(roomRepository.findById(roomId).isPresent()) {
                    String roomUserId = roomRepository.findById(roomId).get().getUserId();
                    game = new Game(roomUserId, userId);
                    games.put(roomUserId, game);
                    games.put(userId, game);
                } else throw new RuntimeException("room.not.exist");
                roomRepository.deleteById(roomId);
                break;
            case "robot":
                game = new Game(userId);
                System.out.println(game);
                games.put(userId, game);
                break;
            default:
                throw new RuntimeException("no.such.game.mode");
        }
        //game.getGameLogic().getGameField(userId);
        return game.getGameField(userId);
    }

    public List<Room> getRooms(){
        return roomRepository.findAll();
    }

    public ShootResponseDTO shoot(String userId, int fieldId) {
        Game game = games.get(userId);
        if(game == null) throw new RuntimeException("no.such.game");
        ShootResponseDTO shootResponseDTO = new ShootResponseDTO();
        shootResponseDTO.setPlayer1(userId);
        shootResponseDTO.setField1(fieldId);
        shootResponseDTO.setGridstate1(game.shoot(userId, fieldId));
        if(game.getPlayer2().equals("robot"))  {
            Integer field2 = Robot.shoot();
            shootResponseDTO.setPlayer2("robot");
            shootResponseDTO.setField2(field2);
            shootResponseDTO.setGridstate2(game.shoot("robot", field2));
        }
        return shootResponseDTO;
        // vissza kell terni a masik ember lovesenek eredmenyevel is
    }
}
