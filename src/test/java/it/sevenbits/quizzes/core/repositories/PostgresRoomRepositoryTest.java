package it.sevenbits.quizzes.core.repositories;

import it.sevenbits.quizzes.core.models.Player;
import it.sevenbits.quizzes.core.models.Room;
import it.sevenbits.quizzes.core.repositories.RoomRepository.IRoomRepository;
import it.sevenbits.quizzes.core.repositories.RoomRepository.PostgresRoomRepository;
import it.sevenbits.quizzes.web.models.CreateRoomRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PostgresRoomRepositoryTest {
    private JdbcOperations mockJdbcOperations;
    private IRoomRepository roomRepository;

    @BeforeEach
    public void setup() {
        mockJdbcOperations = mock(JdbcOperations.class);
        roomRepository = new PostgresRoomRepository(mockJdbcOperations);
    }

    @Test
    public void testGetRooms() {
        when(mockJdbcOperations.query(anyString(), any(RowMapper.class))).thenAnswer((InvocationOnMock invocation) -> {
            RowMapper<Room> rowMapper = invocation.getArgument(1);
            ResultSet rs = mock(ResultSet.class);
            when(rs.getString(eq("name"))).thenReturn("mockName");
            when(rs.getString(eq("id"))).thenReturn("mockId");

            List<Room> rooms = new ArrayList<>();
            rooms.add(rowMapper.mapRow(rs, 0));

            return rooms;
        });

        List<Room> rooms = roomRepository.getRooms();

        Assertions.assertEquals("mockName", rooms.get(0).getRoomName());
        Assertions.assertEquals("mockId", rooms.get(0).getRoomId());
    }

    @Test
    public void testCreateRoom() {
        CreateRoomRequest mockCreateRoomRequest = mock(CreateRoomRequest.class);
        String mockName = "mockName";
        String mockUserId = "mockUserId";
        when(mockCreateRoomRequest.getRoomName()).thenReturn(mockName);
        Room room = roomRepository.createRoom(mockName, mockUserId);
        verify(mockJdbcOperations, times(1)).update(anyString(), anyString(), anyString(), anyString());
        Assertions.assertEquals("mockName", room.getRoomName());
    }

    @Test
    public void testGetRoom() {
        String mockId = "mockId";

        when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class), anyString())).thenAnswer((InvocationOnMock invocation) -> {
            RowMapper<Room> rowMapper = invocation.getArgument(1);
            ResultSet rs = mock(ResultSet.class);
            when(rs.getString(eq("name"))).thenReturn("mockName");
            when(rs.getString(eq("id"))).thenReturn(mockId);

            Room room = rowMapper.mapRow(rs, 0);

            return room;
        });

        Room room = roomRepository.getRoom(mockId);

        Assertions.assertEquals("mockName", room.getRoomName());
        Assertions.assertEquals("mockId", room.getRoomId());
    }
}
