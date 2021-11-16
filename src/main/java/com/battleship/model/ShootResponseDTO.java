package com.battleship.model;

import com.battleship.game.GameField;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShootResponseDTO {
    // my Id
    String player1;
    // my gameField
    GameField gameField1;

    // opponent's Id
    String player2;
    // opponent's gameField
    GameField gameField2;

    boolean isFinished = false;
    String winner = "";
}
