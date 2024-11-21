package com.quizapp.quizApp;
import com.quizapp.quizApp.model.Difficulty;
import com.quizapp.quizApp.model.Quiz;
import com.quizapp.quizApp.model.Question;
import com.quizapp.quizApp.model.QuizQuestion;
import com.quizapp.quizApp.repositories.QuizRepository;
import com.quizapp.quizApp.repositories.QuizQuestionRepository;
import com.quizapp.quizApp.services.QuestionService;
import com.quizapp.quizApp.services.QuizCreationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class QuizCreationServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuestionService questionService;

    @Mock
    private QuizQuestionRepository quizQuestionRepository;

    @InjectMocks
    private QuizCreationService quizCreationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

    @Test
    void testCreateQuizWithRandomQuestions() {
        // Given
        String title = "Test Quiz";
        LocalDate startDate = LocalDate.of(2024, 11, 22);
        LocalDate endDate = LocalDate.of(2024, 11, 25);
        String category = "History";
        String difficulty = "easy";

        Question question1 = new Question();  // Mocked question 1
        question1.setQuestionId(1L);
        question1.setCategory("History");
        question1.setDifficulty(Difficulty.valueOf("easy"));
        question1.setText("Sample question text 1");
        question1.setAnswer("Sample answer 1");

        Question question2 = new Question();  // Mocked question 2
        question2.setQuestionId(2L);
        question2.setCategory("History");
        question2.setDifficulty(Difficulty.valueOf("easy"));
        question2.setText("Sample question text 2");
        question2.setAnswer("Sample answer 2");

        List<Question> questions = Arrays.asList(question1, question2);  // Mock list of questions

        // Mock the behavior of the QuestionService to return mock questions
        when(questionService.getQuestionsByCategory(category, true)).thenReturn(questions);
        when(quizRepository.save(any(Quiz.class))).thenReturn(new Quiz());  // Mock quiz saving

        // When
        Quiz createdQuiz = quizCreationService.createQuizWithRandomQuestions(title, startDate, endDate, category, difficulty);

        // Then
        assertNotNull(createdQuiz);  // Ensure the quiz was created successfully
        assertEquals(title, createdQuiz.getTitle());  // Ensure title is set correctly
        verify(quizRepository, times(1)).save(any(Quiz.class));  // Verify quiz save is called once
        verify(quizQuestionRepository, times(2)).save(any(QuizQuestion.class));  // Verify QuizQuestion save is called for each question
    }
}
