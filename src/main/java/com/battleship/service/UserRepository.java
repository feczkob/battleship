package com.battleship.service;

import com.battleship.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User save(User user);
    Optional<User> findById(Long Id);
    List<User> findAll();
}
