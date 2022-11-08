package it.sevenbits.quizzes.core.repositories;

import it.sevenbits.quizzes.core.models.Question;
import it.sevenbits.quizzes.core.models.Room;
import it.sevenbits.quizzes.core.repositories.QuestionRepository.IQuestionRepository;
import it.sevenbits.quizzes.core.repositories.QuestionRepository.PostgresQuestionRepository;
import it.sevenbits.quizzes.core.repositories.RoomRepository.IRoomRepository;
import it.sevenbits.quizzes.core.repositories.RoomRepository.PostgresRoomRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PostgresQuestionRepositoryTest {
    private JdbcOperations mockJdbcOperations;
    private IQuestionRepository questionRepository;

    @BeforeEach
    public void setup() {
        mockJdbcOperations = mock(JdbcOperations.class);
        questionRepository = new PostgresQuestionRepository(mockJdbcOperations);
    }

    @Test
    public void testGetQuestion() {
        String mockQuestionId = "mockQuestionId";
        when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class), anyString())).thenAnswer((InvocationOnMock invocation) -> {
            RowMapper<Question> rowMapper = invocation.getArgument(1);
            ResultSet rs = mock(ResultSet.class);
            when(rs.getString(eq("correctanswerid"))).thenReturn("mockCorrectAnswerId");
            when(rs.getString(eq("id"))).thenReturn("mockQuestionId");
            when(rs.getString(eq("text"))).thenReturn("mockText");
            when(rs.getInt(eq("score"))).thenReturn(0);

            return rowMapper.mapRow(rs, 0);
        });

        Question question = questionRepository.getQuestion(mockQuestionId);

        Assertions.assertEquals("mockCorrectAnswerId", question.getCorrectAnswerId());
        Assertions.assertEquals("mockQuestionId", question.getQuestionId());
        Assertions.assertEquals("mockText", question.getQuestionText());
        Assertions.assertEquals(0, question.getQuestionScore());
    }

    @Test
    public void testGetRandomQuestionId() {
        List<String> questionIdList = new ArrayList<>();

        when(mockJdbcOperations.queryForObject(anyString(), any(RowMapper.class))).thenAnswer((InvocationOnMock invocation) -> {
            RowMapper<String> rowMapper = invocation.getArgument(1);
            ResultSet rs = mock(ResultSet.class);
            when(rs.getString(eq("id"))).thenReturn("mockQuestionId");

            return rowMapper.mapRow(rs, 0);
        });

        String questionId = questionRepository.getRandomQuestionId(questionIdList);

        verify(mockJdbcOperations, times(1)).queryForObject(anyString(), any(RowMapper.class));
        Assertions.assertEquals("mockQuestionId", questionId);
    }
}
