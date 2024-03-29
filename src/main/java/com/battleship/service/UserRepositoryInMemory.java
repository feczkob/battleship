package com.battleship.service;

import com.battleship.model.User;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Unused in-memory repository of users
 */
@Repository
@NoArgsConstructor
public class UserRepositoryInMemory {
    private List<User> users = createUsers();

    private List<User> createUsers() {
        List<User> u = new ArrayList<>();
        u.add(new User("1", "Tomi"));
        return u;
    }

    public User findById(String id){
        for (User user: users) {
            if(user.getId().equals(id)){
                return user;
            }
        }
        throw new RuntimeException("user.with.id.not.found");
    }

    public List<User> findAll(){
        return users;
    }

    public User add(User user) {
        users.add(user);
        return user;
    }

    public User changeUsername(String id, String newUsername) {
        User user = findById(id);
        user.setName(newUsername);
        return user;
    }
}
