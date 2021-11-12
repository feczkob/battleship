package com.battleship.model;

import com.battleship.game.GRIDSTATE;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShootResponseDTO {
    String player1;
    Integer field1;
    GRIDSTATE gridstate1;

    String player2;
    Integer field2;
    GRIDSTATE gridstate2;

}
