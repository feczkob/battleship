package com.battleship.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Entity class for users
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "user")
public class User {
    @Id
    private String Id;

    @NotNull
    @NotBlank(message = "Name is mandatory")
    private String name;

    private int gamesPlayedVsAi = 0;
    private int gamesPlayedVsUser = 0;
    private int gamesWonVsAi = 0;
    private int gamesWonVsUser = 0;

    /**
     * Constructor
     * @param id Id of the player
     * @param name name of the player
     */
    public User(String id, String name){
        this.Id = id;
        this.name = name;
    }
}
