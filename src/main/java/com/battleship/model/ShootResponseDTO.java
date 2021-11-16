package com.battleship.model;

import com.battleship.game.GameField;
import lombok.*;

/**
 * Response class that is sent to the clients
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShootResponseDTO {
    /**
     * Id of the player to whom this object is sent
     */
    String player1;

    /**
     * Game field of the player to whom this object is sent
     */
    GameField gameField1;

    /**
     * Id of the opponent
     */
    String player2;

    /**
     * Game field of the opponent
     */
    GameField gameField2;

    boolean isFinished = false;
    String winner = "";
}
