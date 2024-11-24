package com.quizapp.quizApp.services;

import com.quizapp.quizApp.model.Question;
import com.quizapp.quizApp.model.QuestionCategory;
import com.quizapp.quizApp.model.Quiz;
import com.quizapp.quizApp.model.QuizQuestion;
import com.quizapp.quizApp.repositories.QuizQuestionRepository;
import com.quizapp.quizApp.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizCreationService {

    private static final boolean LOGGING_ENABLED = true;  // Logging flag

    private final QuizRepository quizRepository;
    private final QuestionService questionService;
    private final QuizQuestionRepository quizQuestionRepository;

    @Autowired
    public QuizCreationService(QuizRepository quizRepository, QuestionService questionService, QuizQuestionRepository quizQuestionRepository) {
        this.quizRepository = quizRepository;
        this.questionService = questionService;
        this.quizQuestionRepository = quizQuestionRepository;
    }

    public Quiz createQuizWithRandomQuestions(String title, LocalDate startDate, LocalDate endDate, String category, String difficulty) {
        try {
            if (LOGGING_ENABLED) System.out.println("Creating quiz with random questions.");

            // Step 1: Fetch and save questions from the external API
            String apiUrl = buildApiUrl(category, difficulty);
            if (LOGGING_ENABLED) System.out.println("API URL generated: " + apiUrl);
            questionService.fetchQuestionsFromApi(apiUrl); // Save questions and flush to DB

            // Step 2: Fetch 10 random questions from the database based on the provided category
            List<Question> questions = getRandomQuestionsFromDatabase(category, 10);
            if (questions.isEmpty()) {
                if (LOGGING_ENABLED) System.err.println("No questions found in the category: " + category);
                throw new IllegalStateException("No questions found for the selected category.");
            }
            if (LOGGING_ENABLED) System.out.println("Fetched " + questions.size() + " questions from the database.");

            // Step 3: Create and save the quiz
            Quiz quiz = createQuiz(title, startDate, endDate);
            if (LOGGING_ENABLED) System.out.println("Quiz created with ID: " + quiz.getQuizId());

            // Step 4: Link the questions to the quiz
            linkQuestionsToQuiz(quiz, questions);
            if (LOGGING_ENABLED) System.out.println("Questions linked to the quiz successfully.");

            return quiz;
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error in creating quiz with random questions: " + e.getMessage());
            throw e;
        }
    }

    // Method to build the API URL using the category and difficulty
    private String buildApiUrl(String category, String difficulty) {
        // Log category and difficulty
        if (LOGGING_ENABLED) System.out.println("Building API URL with category: " + category + " and difficulty: " + difficulty);

        // Define the amount and type variables
        int amount = 10;  // Default value for amount
        String type = "multiple";  // Default value for question type

        // Get the Category enum from the category name
        QuestionCategory catEnum = QuestionCategory.fromString(category);
        int categoryId = catEnum.getCategoryId();

        // Log the generated category ID
        if (LOGGING_ENABLED) System.out.println("Mapped category ID: " + categoryId);

        // Construct and return the API URL
        return "https://opentdb.com/api.php?amount=" + amount + "&category=" + categoryId + "&difficulty=" + difficulty + "&type=" + type;
    }

    private List<Question> getRandomQuestionsFromDatabase(String category, int numberOfQuestions) {
        if (LOGGING_ENABLED) System.out.println("Fetching random " + numberOfQuestions + " questions from category: " + category);
        List<Question> allQuestions = questionService.getQuestionsByCategory(category, true);

        // Shuffle the list and get the first `numberOfQuestions` items
        Collections.shuffle(allQuestions);
        List<Question> randomQuestions = allQuestions.stream()
                .limit(numberOfQuestions)
                .collect(Collectors.toList());

        // Log the result of the random question fetch
        if (LOGGING_ENABLED) System.out.println("Random questions fetched: " + randomQuestions.size());
        return randomQuestions;
    }

    // Method to create and save a quiz
    private Quiz createQuiz(String title, LocalDate startDate, LocalDate endDate) {
        if (LOGGING_ENABLED) System.out.println("Creating quiz with title: " + title);
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setStartDate(startDate);
        quiz.setEndDate(endDate);
        return quizRepository.save(quiz);
    }

    // Method to link questions to a quiz
    private void linkQuestionsToQuiz(Quiz quiz, List<Question> questions) {
        for (Question question : questions) {
            if (LOGGING_ENABLED) System.out.println("Linking question ID: " + question.getQuestionId() + " to quiz ID: " + quiz.getQuizId());
            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setQuiz(quiz);
            quizQuestion.setQuestion(question);
            quizQuestionRepository.save(quizQuestion);
        }
    }
}