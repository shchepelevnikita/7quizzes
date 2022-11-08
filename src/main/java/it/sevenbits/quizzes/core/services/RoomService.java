package it.sevenbits.quizzes.core.services;

import it.sevenbits.quizzes.core.exceptions.PlayerAlreadyInsideRoomException;
import it.sevenbits.quizzes.core.exceptions.PlayerExitRoomBeforeJoinException;
import it.sevenbits.quizzes.core.models.Room;
import it.sevenbits.quizzes.core.repositories.GameRepository.IGameRepository;
import it.sevenbits.quizzes.core.repositories.RoomRepository.IRoomRepository;
import it.sevenbits.quizzes.web.models.CreateRoomRequest;
import org.springframework.stereotype.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Service that contains business logic about rooms and their management
 */
@Service
public class RoomService {
    private final IRoomRepository roomRepository;
    private final IGameRepository gameRepository;
    @Autowired
    private SimpMessagingTemplate template;

    /**
     * Constructor for service
     * @param roomRepository - instance of room repository
     * @param gameRepository - instance of game repository
     */
    public RoomService(final IRoomRepository roomRepository, final IGameRepository gameRepository) {
        this.roomRepository = roomRepository;
        this.gameRepository = gameRepository;
    }

    public List<Room> getRooms() {
        return roomRepository.getRooms();
    }

    /**
     * Method to create a room, change its status and put a player who created the room inside it
     * @param createRoomRequest - request model containing playerId
     * @param userId - user id
     * @return - response model containing roomId, roomName and its players
     */
    public Room createRoom(final CreateRoomRequest createRoomRequest, final String userId) {
        Room tempRoom = roomRepository.createRoom(createRoomRequest.getRoomName(), userId);
        joinRoom(tempRoom.getRoomId(), userId);
        template.convertAndSend("/topic/rooms",  "roomCreated");
        return tempRoom;
    }

    /**
     * Getter for room
     * @param roomId - id of the room
     * @return room by id
     */
    public Room getRoom(final String roomId) {
        return roomRepository.getRoom(roomId);
    }

    /**
     * Method to join room (returns http status conflict if player tries to join while he's already in the room)
     * It also initializes new players stats to work with
     * @param roomId - id of the room
     * @param userId - user id
     * @return room that the player joins
     */
    public Room joinRoom(final String roomId, final String userId) {
        if (roomRepository.getRoom(roomId).getPlayersIds().contains(userId)) {
            throw new PlayerAlreadyInsideRoomException("You can not join room as you are already inside it !");
        }
        roomRepository.addPlayer(roomId, userId);
        if (gameRepository.isGameExisting(roomId) && gameRepository.getIsGameStarted(roomId)) {
            for (String questionId : gameRepository.getQuestionIdsPerGame(roomId)) {
                gameRepository.setQuestionScore(roomId, userId, questionId, 0);
                gameRepository.setHasPlayerAnswered(roomId, userId, questionId, false);
            }
        }

        template.convertAndSend("/topic/rooms",  "roomJoined");
        return roomRepository.getRoom(roomId);
    }

    /**
     * Method to exit room (returns http status conflict if player tries to exit the room he's not in)
     * @param roomId - id of the room
     * @param userId - user id
     */
    public void exitRoom(final String roomId, final String userId) {
        if (!roomRepository.getRoom(roomId).getPlayersIds().contains(userId)) {
            throw new PlayerExitRoomBeforeJoinException("You have to join the room before exiting it !");
        }
        roomRepository.removePlayer(roomId, userId);
    }
}
