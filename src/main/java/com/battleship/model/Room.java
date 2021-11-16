package com.battleship.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;

/**
 * Entity class for rooms
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String userId;

    public Room(String Id){
        userId = Id;
    }
}
