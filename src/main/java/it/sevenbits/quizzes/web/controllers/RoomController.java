package it.sevenbits.quizzes.web.controllers;

import it.sevenbits.quizzes.core.annotations.AuthRoleRequired;
import it.sevenbits.quizzes.core.credentials.IUserCredentials;
import it.sevenbits.quizzes.core.models.Room;
import it.sevenbits.quizzes.core.services.RoomService;
import it.sevenbits.quizzes.web.models.CreateRoomRequest;
import it.sevenbits.quizzes.web.models.CreateRoomResponse;
import it.sevenbits.quizzes.web.models.GetRoomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Room Controller - controller that keeps endpoints of main application
 * (deals with HTTP requests from client and sends him responses)
 * Intermediary between client and service
 * This controller is responsible for requests and responses, related to game rooms
 */
@Controller
public class RoomController {
    private final RoomService roomService;

    /**
     * RoomController constructor
     * @param roomService - instance of RoomService to access room service from inside controller
     */
    public RoomController(final RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Endpoint get request that returns array of all rooms
     * @return - array of all rooms
     */
    @AuthRoleRequired("USER")
    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> getRooms() {
        return new ResponseEntity<>(roomService.getRooms(), HttpStatus.OK);
    }

    /**
     * Endpoint post request that creates a room
     * @param createRoomRequest - model containing playerId and roomName
     * @param userCredentials - user credentials
     * @return - room with its id, name and players in it
     */
    @AuthRoleRequired("USER")
    @PostMapping("/rooms")
    public ResponseEntity<CreateRoomResponse> createRoom(
            @RequestBody final CreateRoomRequest createRoomRequest,
            final IUserCredentials userCredentials
            ) {
        Room tempRoom = roomService.createRoom(createRoomRequest, userCredentials.getUserId());
        return new ResponseEntity<>(new CreateRoomResponse(tempRoom.getRoomId(), tempRoom.getRoomName(),
                tempRoom.getOwnerId(), tempRoom.getPlayers()), HttpStatus.OK);
    }

    /**
     * Endpoint get request that returns a room by id
     * @param roomId - id of the room
     * @return room with roomId
     */
    @AuthRoleRequired("USER")
    @GetMapping("rooms/{roomId}")
    public ResponseEntity<GetRoomResponse> getRoom(@PathVariable final String roomId) {
        Room tempRoom = roomService.getRoom(roomId);
        return new ResponseEntity<>(new GetRoomResponse(tempRoom.getRoomId(), tempRoom.getRoomName(),
                tempRoom.getOwnerId(), tempRoom.getPlayers()), HttpStatus.OK);
    }

    /**
     * Endpoint POST request that returns http status of whether join was successful
     * @param roomId - id of the room to join
     * @param userCredentials - user credentials
     * @return - http status code which tells us whether we have joined the room
     */
    @AuthRoleRequired("USER")
    @PostMapping("/rooms/{roomId}/join")
    public ResponseEntity<String> joinRoom(@PathVariable final String roomId,
                                           final IUserCredentials userCredentials) {
        roomService.joinRoom(roomId, userCredentials.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Endpoint POST request that returns http status of whether exit was successful
     * @param roomId - id of the room to exit
     * @param userCredentials - user credentials
     * @return - http status code which tells us whether we have exited the room
     */
    @AuthRoleRequired("USER")
    @PostMapping("/rooms/{roomId}/exit")
    public ResponseEntity<String> exitRoom(@PathVariable final String roomId,
                                           final IUserCredentials userCredentials) {
        roomService.exitRoom(roomId, userCredentials.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
