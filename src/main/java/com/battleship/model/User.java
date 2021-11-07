package com.battleship.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;
    private int gamesPlayedVsAi = 0;
    private int gamesPlayedVsUser = 0;
    private int gamesWonVsAi = 0;
    private int gamesWonVsUser = 0;

    public User(Long id, String name){
        this.Id = id;
        this.name = name;
    }
}
