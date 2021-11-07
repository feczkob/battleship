package com.battleship.service;

import com.battleship.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BattleshipService {
    @Autowired
    private UserRepository userRepository;

    public User findById(Long Id){
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

    public User changeUsername(Long Id, String newUsername){
        Optional<User> userTmp = userRepository.findById(Id);
        if(userTmp.isPresent()){
            userTmp.get().setName(newUsername);
            userRepository.save(userTmp.get());
            return userTmp.get();
        }
        throw new RuntimeException("user.not.found");
    }
}
