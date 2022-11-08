package it.sevenbits.quizzes.core.repositories.QuestionRepository;

import it.sevenbits.quizzes.core.models.Question;
import it.sevenbits.quizzes.core.models.QuestionAnswer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Objects;


/**
 * Repository that contains questions and all related to them
 */
public class QuestionRepository implements IQuestionRepository {
    /**
     * List of questions
     */
    private final List<Question> questionList;

    /**
     * Random generator
     */
    private final Random rand;

    /**
     * QuestionRepository constructor that inits its maps, list of questions and random generator
     */
    @Autowired
    public QuestionRepository() {
        List<QuestionAnswer> answers1 = new ArrayList<>();
        List<QuestionAnswer> answers2 = new ArrayList<>();
        List<QuestionAnswer> answers3 = new ArrayList<>();
        answers1.add(new QuestionAnswer("Правильный", UUID.randomUUID().toString()));
        answers1.add(new QuestionAnswer("Неправильный", UUID.randomUUID().toString()));
        answers2.add(new QuestionAnswer("Правильный", UUID.randomUUID().toString()));
        answers2.add(new QuestionAnswer("Неправильный", UUID.randomUUID().toString()));
        answers3.add(new QuestionAnswer("Правильный", UUID.randomUUID().toString()));
        answers3.add(new QuestionAnswer("Неправильный", UUID.randomUUID().toString()));
        this.questionList = new ArrayList<>();
        questionList.add(new Question(answers1.get(0).getAnswerId(), "вопрос 1", answers1, 1, UUID.randomUUID().toString()));
        questionList.add(new Question(answers2.get(0).getAnswerId(), "вопрос 2", answers2, 1, UUID.randomUUID().toString()));
        questionList.add(new Question(answers3.get(0).getAnswerId(), "вопрос 3", answers3, 1, UUID.randomUUID().toString()));
        this.rand = new Random();
    }

    /**
     * Getter for question by id
     * @param questionId - questionId
     * @return - question containing questionId, rightAnswer, answersList, questionText and its value
     */
    public Question getQuestion(final String questionId) {
        return questionList.stream().filter(q -> Objects.equals(q.getQuestionId(), questionId)).findAny().orElse(null);
    }

    /**
     * Method to get random id of questions
     * @param randomQuestionIdList - list of random question ids
     * @return - id of the random non-answered question
     */
    public String getRandomQuestionId(final List<String> randomQuestionIdList) {
        Question tempQuestion = questionList.get(rand.nextInt(questionList.size()));
        while (randomQuestionIdList.contains(tempQuestion.getQuestionId())) {
            tempQuestion = questionList.get(rand.nextInt(questionList.size()));
        }
        return tempQuestion.getQuestionId();
    }
}
