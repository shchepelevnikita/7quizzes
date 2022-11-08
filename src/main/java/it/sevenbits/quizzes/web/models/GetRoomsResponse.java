package it.sevenbits.quizzes.web.models;

import it.sevenbits.quizzes.core.models.Room;

import java.util.List;

/**
 * Response for model for method getRooms
 */
public class GetRoomsResponse {
    private final List<Room> rooms;

    /**
     * Model constructor
     * @param rooms - all rooms present
     */
    public GetRoomsResponse(final List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
