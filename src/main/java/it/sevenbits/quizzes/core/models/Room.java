package it.sevenbits.quizzes.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Model for room
 */
public class Room {
    private final String roomId;
    private final String roomName;
    private final String ownerId;
    private List<Player> players;

    /**
     * Constructor for model
     * @param roomName - name of the room
     * @param players - list of players inside the room
     * @param ownerId - id of the room's owner
     * @param roomId - id of the room
     */
    public Room(final String roomName, final List<Player> players, final String ownerId, final String roomId) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.ownerId = ownerId;
        this.players = players;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Add player to the room
     * @param player - player to be added
     */
    public void addPlayer(final Player player) {
        players.add(player);
    }

    /**
     * Method to remove player from room
     * @param playerId - id of the player to remove
     */
    public void removePlayer(final String playerId) {
        this.players = players.stream()
                .filter(player -> !player.getPlayerId().equals(playerId))
                .collect(Collectors.toList());
    }

    /**
     * Getter for list of ids of the players inside the room
     * @return - list of ids of the players inside the room
     */
    public List<String> getPlayersIds() {
        List<String> tempIdList = new ArrayList<>();
        for (Player player : players) {
            tempIdList.add(player.getPlayerId());
        }
        return tempIdList;
    }
}
