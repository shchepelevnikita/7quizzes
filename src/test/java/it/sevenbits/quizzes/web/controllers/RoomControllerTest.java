package it.sevenbits.quizzes.web.controllers;

import it.sevenbits.quizzes.core.credentials.IUserCredentials;
import it.sevenbits.quizzes.core.credentials.UserCredentials;
import it.sevenbits.quizzes.core.models.Player;
import it.sevenbits.quizzes.core.models.Room;
import it.sevenbits.quizzes.core.services.RoomService;
import it.sevenbits.quizzes.web.models.CreateRoomRequest;
import it.sevenbits.quizzes.web.models.CreateRoomResponse;
import it.sevenbits.quizzes.web.models.GetRoomResponse;
import it.sevenbits.quizzes.web.models.JoinRoomRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RoomControllerTest {
    private RoomService mockRoomService;
    private RoomController roomController;

    @BeforeEach
    public void setup() {
        mockRoomService = mock(RoomService.class);
        roomController = new RoomController(mockRoomService);
    }

    @Test
    public void testGetRooms() {
        List<Room> mockRoomList = mock(List.class);
        when(mockRoomService.getRooms()).thenReturn(mockRoomList);

        ResponseEntity<List<Room>> answer = roomController.getRooms();
        verify(mockRoomService, times(1)).getRooms();
        Assertions.assertEquals(HttpStatus.OK, answer.getStatusCode());
        Assertions.assertEquals(mockRoomList, answer.getBody());
    }

    @Test
    public void testCreateRoom() {
        Room mockRoom = mock(Room.class);
        String roomId = "1";
        String roomName = "test";
        String userId = "userId";
        IUserCredentials userCredentials = mock(IUserCredentials.class);
        when(userCredentials.getUserId()).thenReturn(userId);
        List<Player> roomPlayers = new ArrayList<>();
        CreateRoomRequest request = new CreateRoomRequest(roomName, userId);
        when(mockRoom.getRoomId()).thenReturn(roomId);
        when(mockRoom.getRoomName()).thenReturn(roomName);
        when(mockRoom.getPlayers()).thenReturn(roomPlayers);
        when(mockRoomService.createRoom(request, userId)).thenReturn(mockRoom);
        CreateRoomResponse answer = roomController.createRoom(request, userCredentials).getBody();
        verify(mockRoomService, times(1)).createRoom(request, userId);
        Assertions.assertEquals(mockRoom.getRoomId(), answer.getRoomId());
        Assertions.assertEquals(mockRoom.getRoomName(), answer.getRoomName());
        Assertions.assertEquals(mockRoom.getPlayers(), answer.getPlayers());
    }

    @Test
    public void testGetRoom() {
        Room mockRoom = mock(Room.class);
        String roomId = "1";
        String roomName = "test";
        List<Player> roomPlayers = new ArrayList<>();
        when(mockRoomService.getRoom(roomId)).thenReturn(mockRoom);
        when(mockRoom.getRoomId()).thenReturn(roomId);
        when(mockRoom.getRoomName()).thenReturn(roomName);
        when(mockRoom.getPlayers()).thenReturn(roomPlayers);
        ResponseEntity<GetRoomResponse> answer = roomController.getRoom(roomId);
        verify(mockRoomService, times(1)).getRoom(roomId);
        Assertions.assertEquals(HttpStatus.OK, answer.getStatusCode());
        Assertions.assertEquals(mockRoom.getRoomId(), answer.getBody().getRoomId());
        Assertions.assertEquals(mockRoom.getRoomName(), answer.getBody().getRoomName());
        Assertions.assertEquals(mockRoom.getPlayers(), answer.getBody().getPlayers());
    }

    @Test
    public void joinRoom() {
        String userId = "userId";
        IUserCredentials userCredentials = mock(IUserCredentials.class);
        when(userCredentials.getUserId()).thenReturn(userId);
        JoinRoomRequest mockRoomRequest = mock(JoinRoomRequest.class);
        String roomId = "1";
        ResponseEntity<String> answer = roomController.joinRoom(roomId, userCredentials);
        verify(mockRoomService, times(1)).joinRoom(roomId, userId);
        Assertions.assertEquals(HttpStatus.OK, answer.getStatusCode());
    }
}
