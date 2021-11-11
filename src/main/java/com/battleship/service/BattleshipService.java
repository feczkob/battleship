package com.battleship.service;

import com.battleship.model.Room;
import com.battleship.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BattleshipService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    public User findById(String Id){
        if(userRepository.findById(Id).isPresent())
            return userRepository.findById(Id).get();
        throw new RuntimeException("user.not.found");
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

    public void play(String opponent, Long roomId, String userId){

        switch (opponent){
            case "user":
                roomRepository.deleteById(roomId);
                // new game
            case "AI":
                //TODO
        }
    }

    public List<Room> getRooms(){
        return roomRepository.findAll();
    }
}
