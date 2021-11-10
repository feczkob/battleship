package com.battleship.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Long userId;

    public Room(Long Id){
        userId = Id;
    }
}
