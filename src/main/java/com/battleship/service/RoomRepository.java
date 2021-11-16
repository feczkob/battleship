package com.battleship.service;

import com.battleship.model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository of rooms
 */
@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
    Room save(Room room);
    Optional<Room> findById(Long Id);
    List<Room> findAll();
    void deleteById(Long Id);
    void deleteByUserId(String userId);
    Room findByUserId(String userId);
}
