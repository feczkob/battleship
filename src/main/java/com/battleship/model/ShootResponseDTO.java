package com.battleship.model;

import com.battleship.game.GameField;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShootResponseDTO {
    String player1;
    GameField gameField1;

    String player2;
    GameField gameField2;

    boolean isFinished = false;
    String winner = "";
}
