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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/profile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public User addUser(@Valid @RequestBody User user){
        return battleshipService.save(user);
    }

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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Service not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @RequestMapping(path = "/play/opponent={opponent}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void play(
            @PathVariable String opponent,
            @RequestParam Long roomId,
            @RequestParam String userId){
        battleshipService.play(opponent, roomId, userId);
    }

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
}
