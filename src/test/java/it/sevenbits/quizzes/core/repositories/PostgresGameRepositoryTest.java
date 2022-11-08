package it.sevenbits.quizzes.core.repositories;

import it.sevenbits.quizzes.core.models.Game;
import it.sevenbits.quizzes.core.models.Question;
import it.sevenbits.quizzes.core.models.Room;
import it.sevenbits.quizzes.core.repositories.GameRepository.IGameRepository;
import it.sevenbits.quizzes.core.repositories.GameRepository.PostgresGameRepository;
import it.sevenbits.quizzes.core.repositories.RoomRepository.IRoomRepository;
import it.sevenbits.quizzes.core.repositories.RoomRepository.PostgresRoomRepository;
import it.sevenbits.quizzes.web.models.GameStatus;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PostgresGameRepositoryTest {
    private JdbcOperations mockJdbcOperations;
    private IGameRepository gameRepository;

    @BeforeEach
    public void setup() {
        mockJdbcOperations = mock(JdbcOperations.class);
        gameRepository = new PostgresGameRepository(mockJdbcOperations);
    }

    @Test
    public void testAddGame() {
        String mockRoomId = "mockRoomId";
        Game mockGame = mock(Game.class);
        GameStatus gameStatus = new GameStatus("mockGameStatus", "mockQuestionId", 0, 0);

        when(mockGame.isGameStarted()).thenReturn(false);
        when(mockGame.getGameStatus()).thenReturn(gameStatus);

        gameRepository.addGame(mockRoomId, mockGame);
        verify(mockJdbcOperations, times(1)).update(anyString(), anyBoolean(), anyString(), anyString(), anyInt(), anyInt(), anyString());
    }

    @Test
    public void testSetIsGameStarted() {
        String mockRoomId = "mockRoomId";
        boolean mockBool = true;
        gameRepository.setIsGameStarted(mockRoomId, mockBool);
        verify(mockJdbcOperations, times(1)).update(anyString(), anyBoolean(), anyString());
    }

    @Test
    public void testGetIsGameStarted() {
        String mockRoomId = "mockRoomId";
        when(mockJdbcOperations.queryForObject(anyString(), eq(Boolean.class), anyString())).thenReturn(true);
        boolean response = gameRepository.getIsGameStarted(mockRoomId);
        Assertions.assertTrue(response);
    }

    @Test
    public void testSetTotalScore() {
        String mockRoomId = "mockRoomId";
        String mockPlayerId = "mockPlayerId";
        int mockTotalScore = 0;
        gameRepository.setTotalScore(mockRoomId, mockPlayerId, mockTotalScore);
        verify(mockJdbcOperations, times(1)).update(anyString(), anyInt(), anyString(), anyString(), anyInt(), anyString(), anyString());
    }

    @Test
    public void testGetTotalScore() {
        String mockRoomId = "mockRoomId";
        String mockPlayerId = "mockPlayerId";
        when(mockJdbcOperations.queryForObject(anyString(), eq(Integer.class), anyString(), anyString())).thenReturn(0);
        int response = gameRepository.getTotalScore(mockRoomId, mockPlayerId);
        Assertions.assertEquals(0, response);
    }

    @Test
    public void testSetGameStatus() {
        String mockRoomId = "mockRoomId";
        String mockStatus = "mockStatus";

        gameRepository.setGameStatus(mockRoomId, mockStatus);
        verify(mockJdbcOperations, times(1)).update(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testGetGameStatus() {
        String mockRoomId = "mockRoomId";

        when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class), anyString())).thenAnswer((InvocationOnMock invocation) -> {
            RowMapper<GameStatus> rowMapper = invocation.getArgument(1);
            ResultSet rs = mock(ResultSet.class);
            when(rs.getString(eq("status"))).thenReturn("mockStatus");
            when(rs.getString(eq("questionid"))).thenReturn("mockQuestionId");
            when(rs.getInt(eq("questionnumber"))).thenReturn(0);
            when(rs.getInt(eq("questionscount"))).thenReturn(0);

            return rowMapper.mapRow(rs, 0);
        });

        GameStatus gameStatus = gameRepository.getGameStatus(mockRoomId);

        Assertions.assertEquals("mockStatus", gameStatus.getStatus());
        Assertions.assertEquals("mockQuestionId", gameStatus.getQuestionId());
        Assertions.assertEquals(0, gameStatus.getQuestionNumber());
        Assertions.assertEquals(0, gameStatus.getQuestionsCount());
    }

    @Test
    public void testSetGameQuestionsCount() {
        String mockRoomId = "mockRoomId";
        int mockCount = 0;

        gameRepository.setGameQuestionsCount(mockRoomId, mockCount);
        verify(mockJdbcOperations, times(1)).update(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testGetGameQuestionsCount() {
        String mockRoomId = "mockRoomId";

        when(mockJdbcOperations.queryForObject(anyString(), eq(Integer.class), anyString())).thenReturn(0);

        int response = gameRepository.getGameQuestionsCount(mockRoomId);

        Assertions.assertEquals(0, response);
    }

    @Test
    public void testSetGameQuestion() {
        String mockRoomId = "mockRoomId";
        String mockQuestionId = "mockQuestionId";

        gameRepository.setGameStatus(mockRoomId, mockQuestionId);
        verify(mockJdbcOperations, times(1)).update(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testGetGameQuestion() {
        String mockRoomId = "mockRoomId";
        String mockQuestionId = "mockQuestionId";
        when(mockJdbcOperations.queryForObject(anyString(), eq(String.class), anyString())).thenReturn(mockQuestionId);

        String response = gameRepository.getGameQuestion(mockRoomId);

        Assertions.assertEquals(mockQuestionId, response);
    }

    @Test
    public void testSetGameQuestionNumber() {
        String mockRoomId = "mockRoomId";
        int mockNumber = 0;

        gameRepository.setGameQuestionNumber(mockRoomId, mockNumber);
        verify(mockJdbcOperations, times(1)).update(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testGetAmountOfQuestions() {
        String mockRoomId = "mockRoomId";

        when(mockJdbcOperations.queryForObject(anyString(), eq(Integer.class), anyString())).thenReturn(0);

        int response = gameRepository.getAmountOfQuestions(mockRoomId);

        Assertions.assertEquals(0, response);
    }

    @Test
    public void testSetQuestionScore() {
        String mockRoomId = "mockRoomId";
        String mockPlayerId = "mockPlayerId";
        String mockQuestionId = "mockQuestionId";
        int mockScore = 0;

        gameRepository.setQuestionScore(mockRoomId, mockPlayerId, mockQuestionId, mockScore);
        verify(mockJdbcOperations, times(1)).update(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void testGetQuestionScore() {
        String mockRoomId = "mockRoomId";
        String mockPlayerId = "mockPlayerId";
        String mockQuestionId = "mockQuestionId";

        when(mockJdbcOperations.queryForObject(anyString(), eq(Integer.class), anyString(), anyString(), anyString())).thenReturn(0);

        int response = gameRepository.getQuestionScore(mockRoomId, mockPlayerId, mockQuestionId);

        Assertions.assertEquals(0, response);
    }

    @Test
    public void testSetIsQuestionAnswered() {
        String mockRoomId = "mockRoomId";
        String mockQuestionId = "mockQuestionId";

        gameRepository.setIsQuestionAnswered(mockRoomId, mockQuestionId);
        verify(mockJdbcOperations, times(1)).update(anyString(), anyString(), anyString(), anyBoolean(), anyBoolean());
    }

    @Test
    public void testGetIsQuestionAnswered() {
        String mockRoomId = "mockRoomId";
        String mockQuestionId = "mockQuestionId";

        when(mockJdbcOperations.queryForObject(anyString(), eq(Boolean.class), anyString(), anyString())).thenReturn(true);
        boolean response = gameRepository.getIsQuestionAnswered(mockRoomId, mockQuestionId);
        Assertions.assertTrue(response);
    }

    @Test
    public void testIsGameExisting() {
        String mockRoomId = "mockRoomId";

        when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class), anyString())).thenAnswer((InvocationOnMock invocation) -> {
            RowMapper<GameStatus> rowMapper = invocation.getArgument(1);
            ResultSet rs = mock(ResultSet.class);
            when(rs.getString(eq("status"))).thenReturn("mockStatus");
            when(rs.getString(eq("questionid"))).thenReturn("mockQuestionId");
            when(rs.getInt(eq("questionnumber"))).thenReturn(0);
            when(rs.getInt(eq("questionscount"))).thenReturn(0);

            return rowMapper.mapRow(rs, 0);
        });

        boolean response = gameRepository.isGameExisting(mockRoomId);

        Assertions.assertTrue(response);
    }

    @Test
    public void testSetQuestionIdsPerGame() {
        String mockRoomId = "mockRoomId";
        List<String> mockQuestionIdList = new ArrayList<>();
        mockQuestionIdList.add("mockId1");
        mockQuestionIdList.add("mockId2");

        gameRepository.setQuestionIdsPerGame(mockRoomId, mockQuestionIdList);
        verify(mockJdbcOperations, times(2)).update(anyString(), anyString(), anyString(), anyBoolean(), anyBoolean());
    }

    @Test
    public void testGetQuestionIdsPerGame() {
        String mockRoomId = "mockRoomId";

        when(mockJdbcOperations.query(anyString(), any(RowMapper.class), anyString())).thenAnswer((InvocationOnMock invocation) -> {
            RowMapper<String> rowMapper = invocation.getArgument(1);
            ResultSet rs = mock(ResultSet.class);
            when(rs.getString(eq("questionid"))).thenReturn("mockQuestionId");

            List<String> questionIds = new ArrayList<>();
            questionIds.add(rowMapper.mapRow(rs, 0));

            return questionIds;
        });

        List<String> questionIds = gameRepository.getQuestionIdsPerGame(mockRoomId);

        Assertions.assertEquals("mockQuestionId", questionIds.get(0));
    }

    @Test
    public void testSetHasPlayerAnswered() {
        String mockRoomId = "mockRoomId";
        String mockPlayerId = "mockPlayerId";
        String mockQuestionId = "mockQuestionId";
        boolean mockBool = true;

        gameRepository.setHasPlayerAnswered(mockRoomId, mockPlayerId, mockQuestionId, mockBool);
        verify(mockJdbcOperations, times(1)).update(anyString(), anyString(), anyString(), anyString(), anyBoolean(), anyBoolean());
    }

    @Test
    public void testGetHasPlayerAnswered() {
        String mockRoomId = "mockRoomId";
        String mockPlayerId = "mockPlayerId";
        String mockQuestionId = "mockQuestionId";
        when(mockJdbcOperations.queryForObject(anyString(), eq(Boolean.class), anyString(), anyString(), anyString())).thenReturn(true);
        boolean response = gameRepository.getHasPlayerAnswered(mockRoomId, mockPlayerId, mockQuestionId);
        Assertions.assertTrue(response);
    }
}
