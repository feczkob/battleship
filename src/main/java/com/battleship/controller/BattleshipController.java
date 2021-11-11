package com.battleship.controller;

import com.battleship.model.Room;
import com.battleship.model.User;
import com.battleship.service.BattleshipService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api")
public class BattleshipController {
    @Autowired
    private BattleshipService battleshipService;

    @Schema(name = "findById",description = "Find user by Id and if not found create a new user")
    @ApiResponses(value = {
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

    @Schema(name = "findAll", description = "Find all users")
    @ApiResponses(value = {
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

    @Schema(name = "addUser",description = "Add user - not used")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/profile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public User addUser(@Valid @RequestParam User user){
        return battleshipService.save(user);
    }

    @Schema(name = "changeUsername",description = "Change username by Id")
    @ApiResponses(value = {
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

    @Schema(name = "createRoom",description = "Create room for multi player game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Room.class))}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/createRoom", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Room createRoom(@RequestParam String userId){
        return battleshipService.createRoom(userId);
    }

    @Schema(name = "playSinglePlayer",description = "Play single player game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/play/opponent={opponent}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void playSinglePlayer(
            @PathVariable String opponent){
        battleshipService.play(opponent, 0L, "");
    }

    @Schema(name = "playMultiPlayer", description = "Play multi player game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/play/{roomId}/opponent={opponent}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void playMultiPlayer(
            @PathVariable String opponent,
            @PathVariable Long roomId,
            @RequestParam String userId
    ){
        battleshipService.play(opponent, roomId, userId);
    }

    @Schema(name = "getRooms",description = "Find all rooms")
    @ApiResponses(value = {
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

    @Schema(name = "shoot",description = "Shoot to a specific field")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/play/shoot", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void shoot(
            @RequestParam String userId,
            @RequestParam int fieldId
    ){
        battleshipService.shoot(userId, fieldId);
    }
}
