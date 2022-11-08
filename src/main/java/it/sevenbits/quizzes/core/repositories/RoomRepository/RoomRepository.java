package it.sevenbits.quizzes.core.repositories.RoomRepository;

import it.sevenbits.quizzes.core.models.Player;
import it.sevenbits.quizzes.core.models.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

/**
 * Repository containing rooms and method to work with them directly
 */
public class RoomRepository implements IRoomRepository {
    private final List<Room> rooms;

    /**
     * Constructor for room repository
     */
    public RoomRepository() {
        this.rooms = new ArrayList<>();
    }

    /**
     * method to get all rooms
     * @return - all rooms
     */
    @Override
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * method to create a room
     * @param createRoomRequest - request model that contains playerId and roomName
     * @return - created room
     */
    @Override
    public Room createRoom(final String roomName, final String userId) {
        Room tempRoom = new Room(roomName, new ArrayList<>(), userId, UUID.randomUUID().toString());
        rooms.add(tempRoom);
        return tempRoom;
    }

    /**
     * method to get room by roomid
     * @param roomId - id of the room to get
     * @return - room
     */
    @Override
    public Room getRoom(final String roomId) {
        return rooms.stream()
                .filter(room -> room.getRoomId().equals(roomId))
                .collect(Collectors.toList()).get(0);
    }

    /**
     * method to add player to the room
     * @param roomId - id of the room
     * @param joinRoomRequest - request to join the room
     */
    @Override
    public void addPlayer(final String roomId, final String userId) {
        Room room = getRoom(roomId);
        room.addPlayer(new Player(userId));
    }

    /**
     * method to remove player from the room
     * @param roomId - id of the room
     * @param userId - id of the user
     */
    @Override
    public void removePlayer(final String roomId, final String userId) {
        Room room = getRoom(roomId);
        room.removePlayer(userId);
    }
}
