package it.sevenbits.quizzes.core.repositories.RoomRepository;

import it.sevenbits.quizzes.core.models.Room;

import java.util.List;

/**
 * RoomRepository interface
 */
public interface IRoomRepository {
    /**
     * Get method to get all rooms
     * @return - model that contains list of rooms
     */
    List<Room> getRooms();

    /**
     * Method to create new room
     * @param roomName - room name
     * @param userId - user id
     * @return - response model containing roomId, roomName and list of players inside that room
     */
    Room createRoom(final String roomName, final String userId);

    /**
     * Method to get a single room
     * @param roomId - id of the room to get
     * @return - response model containing roomId, roomName and a list of players
     */
    Room getRoom(final String roomId);

    /**
     * method to add player to the room
     * @param roomId - id of the room
     * @param userId - user id
     */
    void addPlayer(final String roomId, final String userId);

    /**
     * method to remove player from the room
     * @param roomId - id of the room
     * @param userId - id of the user
     */
    void removePlayer(final String roomId, final String userId);
}
