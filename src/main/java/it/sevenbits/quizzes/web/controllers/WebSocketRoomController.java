package it.sevenbits.quizzes.web.controllers;

import it.sevenbits.quizzes.core.credentials.IUserCredentials;
import it.sevenbits.quizzes.core.models.Room;
import it.sevenbits.quizzes.core.services.RoomService;
import it.sevenbits.quizzes.web.models.CreateRoomRequest;
import it.sevenbits.quizzes.web.models.CreateRoomResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Web Socket Room Controller
 */
@Controller
@MessageMapping("/ws/rooms")
public class WebSocketRoomController {
    private final RoomService roomService;

    /**
     * Constructor for room controller
     * @param roomService - room service
     */
    public WebSocketRoomController(final RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * method for creating room
     * @param createRoomRequest - request for creating room
     * @param userCredentials - user credentials
     * @return - response with created room
     */
    @MessageMapping()
    @SendTo("/topic/rooms")
    public CreateRoomResponse createRoom(
            @RequestBody final CreateRoomRequest createRoomRequest,
            final IUserCredentials userCredentials
    ) {
        Room tempRoom = roomService.createRoom(createRoomRequest, userCredentials.getUserId());
        return new CreateRoomResponse(tempRoom.getRoomId(), tempRoom.getRoomName(),
                tempRoom.getOwnerId(), tempRoom.getPlayers());
    }

    /**
     * method to join room
     * @param roomId - room id
     * @param userCredentials - user credentials
     * @return - response with room joined
     */
    @MessageMapping("/{roomId}/join")
    @SendTo("/topic/rooms")
    public Room joinRoom(@PathVariable final String roomId,
                                           final IUserCredentials userCredentials) {
        return roomService.joinRoom(roomId, userCredentials.getUserId());
    }
}
