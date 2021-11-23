package com.battleship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Leaderboard DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LeaderboardDTO {
    private List<User> leaderboard;
}
