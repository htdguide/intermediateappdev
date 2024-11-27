package com.quizapp.quizApp.services;

import com.quizapp.quizApp.model.Question;
import com.quizapp.quizApp.model.QuestionCategory;
import com.quizapp.quizApp.model.Quiz;
import com.quizapp.quizApp.model.QuizQuestion;
import com.quizapp.quizApp.model.User;
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
    private final EmailService emailService;  // Add EmailService
    private final UserService userService;

    @Autowired
    public QuizCreationService(QuizRepository quizRepository, QuestionService questionService,
                               QuizQuestionRepository quizQuestionRepository, EmailService emailService,
                               UserService userService) {
        this.quizRepository = quizRepository;
        this.questionService = questionService;
        this.quizQuestionRepository = quizQuestionRepository;
        this.emailService = emailService;
        this.userService = userService;
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

            // Step 5: Notify all users about the new quiz
            notifyUsersOfNewQuiz(quiz);

            return quiz;
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error in creating quiz with random questions: " + e.getMessage());
            throw e;
        }
    }

    // Method to build the API URL using the category and difficulty
    private String buildApiUrl(String category, String difficulty) {
        if (LOGGING_ENABLED) System.out.println("Building API URL with category: " + category + " and difficulty: " + difficulty);

        int amount = 10;  // Default value for amount
        String type = "multiple";  // Default value for question type

        QuestionCategory catEnum = QuestionCategory.fromString(category);
        int categoryId = catEnum.getCategoryId();

        if (LOGGING_ENABLED) System.out.println("Mapped category ID: " + categoryId);

        return "https://opentdb.com/api.php?amount=" + amount + "&category=" + categoryId + "&difficulty=" + difficulty + "&type=" + type;
    }

    private List<Question> getRandomQuestionsFromDatabase(String category, int numberOfQuestions) {
        if (LOGGING_ENABLED) System.out.println("Fetching random " + numberOfQuestions + " questions from category: " + category);
        List<Question> allQuestions = questionService.getQuestionsByCategory(category, true);

        Collections.shuffle(allQuestions);
        List<Question> randomQuestions = allQuestions.stream()
                .limit(numberOfQuestions)
                .collect(Collectors.toList());

        if (LOGGING_ENABLED) System.out.println("Random questions fetched: " + randomQuestions.size());
        return randomQuestions;
    }

    private Quiz createQuiz(String title, LocalDate startDate, LocalDate endDate) {
        if (LOGGING_ENABLED) System.out.println("Creating quiz with title: " + title);
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setStartDate(startDate);
        quiz.setEndDate(endDate);
        return quizRepository.save(quiz);
    }

    private void linkQuestionsToQuiz(Quiz quiz, List<Question> questions) {
        for (Question question : questions) {
            if (LOGGING_ENABLED) System.out.println("Linking question ID: " + question.getQuestionId() + " to quiz ID: " + quiz.getQuizId());
            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setQuiz(quiz);
            quizQuestion.setQuestion(question);
            quizQuestionRepository.save(quizQuestion);
        }
    }

    private void notifyUsersOfNewQuiz(Quiz quiz) {
        try {
            List<User> users = userService.getAllUsers(); // Fetch all users

            if (users.isEmpty()) {
                if (LOGGING_ENABLED) System.out.println("No users found to notify.");
                return;
            }

            for (User user : users) {
                String subject = "New Quiz Created: " + quiz.getTitle();
                String body = "Hello " + user.getFirstName() + ",\n\n" +
                        "A new quiz has been created on QuizApp:\n" +
                        "Title: " + quiz.getTitle() + "\n" +
                        "Start Date: " + quiz.getStartDate() + "\n" +
                        "End Date: " + quiz.getEndDate() + "\n\n" +
                        "Good luck!\nQuizApp Team";

                emailService.sendEmail(user.getEmail(), subject, body, "dholakiaharshil7@gmail.com");
            }

            if (LOGGING_ENABLED) System.out.println("Notification emails sent to all users.");
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error notifying users: " + e.getMessage());
        }
    }
}
