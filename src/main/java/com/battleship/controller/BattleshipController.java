package com.battleship.controller;

import com.battleship.game.GameField;
import com.battleship.model.LeaderboardDTO;
import com.battleship.model.Room;
import com.battleship.model.ShootResponseDTO;
import com.battleship.model.User;
import com.battleship.service.BattleshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller class for the server-side application
 */
@RestController
@RequestMapping("/api")
public class BattleshipController {

    private final BattleshipService battleshipService;

    /**
     * Injection of service instance
     * @param battleshipService service
     */
    @Autowired
    public BattleshipController(BattleshipService battleshipService){
        this.battleshipService = battleshipService;
    }

    /**
     * Find user by Id
     * @param id Id of user
     * @return user
     */
    @Schema(name = "findById",description = "Find user by Id and if not found create a new user")
    @Operation(summary = "Find user by Id and if not found create a new user", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/profile/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public User findById(@PathVariable String id){
        return battleshipService.findById(id);
    }

    /**
     * Find all users
     * @return list of users
     */
    @Schema(name = "findAll", description = "Find all users")
    @Operation(summary = "Find all users", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/profile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> findAll(){
        return battleshipService.findAll();
    }

    /**
     * Change username
     * @param Id Id of user
     * @param newUsername new username
     * @return user
     */
    @Schema(name = "changeUsername",description = "Change username by Id")
    @Operation(summary = "Change username by Id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/profile/changeUsername", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public User changeUsername(
            @RequestParam String Id,
            @RequestParam String newUsername){
        return battleshipService.changeUsername(Id, newUsername);
    }

    /**
     * Create a room
     * @param userId owner of room
     * @return room
     */
    @Schema(name = "createRoom",description = "Create room for multi player game")
    @Operation(summary = "Create room for multi player game", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameField.class))}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/createRoom", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public GameField createRoom(@RequestParam String userId){
        return battleshipService.createRoom(userId);
    }

    /**
     * Get the list of rooms
     * @return list of rooms
     */
    @Schema(name = "getRooms",description = "Find all rooms")
    @Operation(summary = "Find all rooms", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Room.class))}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/rooms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Room> getRooms(){
        return battleshipService.getRooms();
    }

    /**
     * Create a room
     * @param userId owner of room
     */
    @Schema(name = "leaveRoom",description = "Leave room")
    @Operation(summary = "Leave room", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/leaveRoom", method = RequestMethod.DELETE)
    public void leaveRoom(@RequestParam String userId){
        battleshipService.leaveRoom(userId);
    }

    /**
     * Leave game
     * @param userId Id of the player
     */
    @Schema(name = "leaveGame",description = "Leave game")
    @Operation(summary = "Leave room", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/leaveGame", method = RequestMethod.DELETE)
    public void leaveGame(@RequestParam String userId){
        battleshipService.leaveGame(userId);
    }

    /**
     * Play in single player game mode
     * @param opponent "robot"
     * @param userId Id of the player
     * @return initial state of game field
     */
    @Schema(name = "playSinglePlayer", description = "Play single player game")
    @Operation(summary = "Play single player game", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameField.class))}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/play/opponent={opponent}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public GameField playSinglePlayer(
            @PathVariable String opponent,
            @RequestParam String userId){
        return battleshipService.play(opponent, 0L, userId);
    }

    /**
     * Play in multiplayer game mode
     * @param opponent Id of the opponent
     * @param roomId Id of the room
     * @param userId Id of the player
     * @return initial state of the game field
     */
    @Schema(name = "playMultiPlayer", description = "Play multi player game")
    @Operation(summary = "Play multi player game", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameField.class))}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/play/{roomId}/opponent={opponent}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public GameField playMultiPlayer(
            @PathVariable String opponent,
            @PathVariable Long roomId,
            @RequestParam String userId){
        return battleshipService.play(opponent, roomId, userId);
    }

    /**
     * Get newly positioned ships
     * @param userId Id of the player
     * @return new game field
     */
    @Schema(name = "getNewShipPositions", description = "Get new ship positions")
    @Operation(summary = "Get new ship positions", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameField.class))}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/play/getNewShipPositions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public GameField getNewShipPositions(@RequestParam String userId){
        return battleshipService.getNewShipPositions(userId);
    }

    /**
     * Declare themselves ready for the game
     * @param userId Id of the player
     * @return true
     */
    @Schema(name = "ready", description = "Ready")
    @Operation(summary = "Ready", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/play/ready", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ShootResponseDTO ready(@RequestParam String userId){
        return battleshipService.ready(userId);
    }

    /**
     * Shoot at a specific field
     * @param userId user who shoots
     * @param fieldId field to be shot at
     * @return response with the outcomes
     */
    @Schema(name = "shoot",description = "Shoot to a specific field")
    @Operation(summary = "Shoot to a specific field", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShootResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/play/shoot", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ShootResponseDTO shoot(
            @RequestParam String userId,
            @RequestParam int fieldId
    ){
        return battleshipService.shoot(userId, fieldId);
    }

    /**
     * Get leaderboard ordered by games won
     * @param opponent "user" or "robot"
     * @return leaderboard
     */
    @Schema(name = "shoot",description = "Get leaderboard ordered by games won")
    @Operation(summary = "Get leaderboard ordered by games won", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LeaderboardDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/leaderboard/{opponent}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public LeaderboardDTO getLeaderboard(@PathVariable String opponent){
        return battleshipService.getLeaderboard(opponent);
    }
}
