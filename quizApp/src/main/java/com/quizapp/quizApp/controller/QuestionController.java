package com.quizapp.quizApp.controller;

import com.quizapp.quizApp.model.Difficulty;
import com.quizapp.quizApp.model.Question;
import com.quizapp.quizApp.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;
    private static final boolean LOGGING_ENABLED = true;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // Get all questions
    @GetMapping()
    public List<Question> getAllQuestions() {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching all questions.");
            return questionService.getAllQuestions();
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching all questions: " + e.getMessage());
            throw e;
        }
    }

    // Get a question by ID
    @GetMapping("/{id}")
    public Optional<Question> getQuestionById(@PathVariable Long id) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching question by ID: " + id);
            return questionService.getQuestionById(id);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching question by ID: " + e.getMessage());
            throw e;
        }
    }

    // Get questions by difficulty
    @GetMapping("/difficulty/{difficulty}")
    public List<Question> getQuestionsByDifficulty(@PathVariable String difficulty) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching questions by difficulty: " + difficulty);

            // Convert String to Difficulty enum
            Difficulty difficultyEnum = Arrays.stream(Difficulty.values())
                    .filter(d -> d.name().equalsIgnoreCase(difficulty))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid difficulty: " + difficulty));

            return questionService.getQuestionsByDifficulty(difficultyEnum);
        } catch (IllegalArgumentException e) {
            if (LOGGING_ENABLED) System.err.println("Invalid difficulty provided: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching questions by difficulty: " + e.getMessage());
            throw e;
        }
    }

    // Get questions by category
    @GetMapping("/category/{category}")
    public List<Question> getQuestionsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "false") boolean randomize) {

        try {
            if (LOGGING_ENABLED) System.out.println("Fetching questions by category: " + category);
            return questionService.getQuestionsByCategory(category, randomize);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching questions by category: " + e.getMessage());
            throw e;
        }
    }

    // Search questions by keyword
    @GetMapping("/search")
    public List<Question> searchQuestionsByKeyword(@RequestParam String keyword) {
        try {
            if (LOGGING_ENABLED) System.out.println("Searching questions by keyword: " + keyword);
            return questionService.searchQuestionsByKeyword(keyword);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error searching questions by keyword: " + e.getMessage());
            throw e;
        }
    }

    // Get a question by ID, difficulty, and category
    @GetMapping("/{id}/details")
    public Optional<Question> getQuestionByIdDifficultyAndCategory(
            @PathVariable Long id,
            @RequestParam String difficulty,
            @RequestParam String category) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching question by ID, difficulty, and category.");
            return questionService.getQuestionByIdDifficultyAndCategory(id, difficulty, category);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching question details: " + e.getMessage());
            throw e;
        }
    }

    // Create a new question
    @PostMapping
    public Question createQuestion(@RequestBody Question question) {
        try {
            if (LOGGING_ENABLED) System.out.println("Creating a new question.");
            return questionService.saveQuestion(question);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error creating question: " + e.getMessage());
            throw e;
        }
    }

    // Create multiple questions
    @PostMapping("/batch")
    public List<Question> createQuestions(@RequestBody List<Question> questions) {
        try {
            if (LOGGING_ENABLED) System.out.println("Creating multiple questions.");
            return questionService.saveQuestions(questions);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error creating multiple questions: " + e.getMessage());
            throw e;
        }
    }

    // Fetch questions from an external API
    @PostMapping("/fetch")
    public List<Question> fetchQuestionsFromApi(@RequestParam String apiUrl) {
        try {
            if (LOGGING_ENABLED) System.out.println("Controller: Fetching questions from API.");
            return questionService.fetchQuestionsFromApi(apiUrl);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error in controller while fetching questions: " + e.getMessage());
            throw e;
        }
    }

    // Update an existing question by ID
    @PutMapping("/{id}")
    public Question updateQuestion(
            @PathVariable Long id,
            @RequestBody Question updatedQuestion) {
        try {
            if (LOGGING_ENABLED) System.out.println("Controller: Updating question with ID: " + id);

            // Update non-null fields in the service layer
            return questionService.updateQuestionById(id, updatedQuestion);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error in controller while updating question: " + e.getMessage());
            throw e;
        }
    }

    // Delete a question by ID
    @DeleteMapping("/{id}")
    public void deleteQuestionById(@PathVariable Long id) {
        try {
            if (LOGGING_ENABLED) System.out.println("Deleting question with ID: " + id);
            questionService.deleteQuestionById(id);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error deleting question: " + e.getMessage());
            throw e;
        }
    }
}
