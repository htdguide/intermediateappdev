package com.quizapp.quizApp.controller;

import com.quizapp.quizApp.dto.QuizQuestionDTO;
import com.quizapp.quizApp.model.Question;
import com.quizapp.quizApp.model.QuizQuestion;
import com.quizapp.quizApp.services.QuizQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quiz-questions")
public class QuizQuestionController {

    private final QuizQuestionService quizQuestionService;
    private static final boolean LOGGING_ENABLED = true;

    @Autowired
    public QuizQuestionController(QuizQuestionService quizQuestionService) {
        this.quizQuestionService = quizQuestionService;
    }

    // Get all QuizQuestion mappings
    @GetMapping
    public List<QuizQuestion> getAllQuizQuestions() {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching all QuizQuestions.");
            return quizQuestionService.getAllQuizQuestions();
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching all QuizQuestions: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/quiz/{quizId}")
    public List<QuizQuestionDTO> getQuestionsByQuizId(@PathVariable Long quizId) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching questions for quiz ID: " + quizId);

            List<QuizQuestion> quizQuestions = quizQuestionService.getQuestionsByQuizId(quizId);

            // Map each QuizQuestion to a QuizQuestionDTO
            return quizQuestions.stream().map(quizQuestion -> {
                Question question = quizQuestion.getQuestion();
                QuizQuestionDTO dto = new QuizQuestionDTO();
                dto.setQuestionId(question.getQuestionId());
                dto.setText(question.getText());
                dto.setCategory(question.getCategory());
                dto.setDifficulty(question.getDifficulty().name());

                // Combine correct and incorrect answers, then shuffle
                List<String> options = new ArrayList<>(question.getIncorrectAnswers());
                options.add(question.getAnswer());
                Collections.shuffle(options); // Randomize options

                dto.setOptions(options);
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching questions for quiz ID: " + e.getMessage());
            throw e;
        }
    }


    // Get all quizzes containing a specific question by questionId
    @GetMapping("/question/{questionId}")
    public List<QuizQuestion> getQuizzesByQuestionId(@PathVariable Long questionId) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching quizzes for question ID: " + questionId);
            return quizQuestionService.getQuizzesByQuestionId(questionId);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching quizzes for question ID: " + e.getMessage());
            throw e;
        }
    }

    // Count the number of questions in a specific quiz
    @GetMapping("/quiz/{quizId}/count")
    public Long countQuestionsInQuiz(@PathVariable Long quizId) {
        try {
            if (LOGGING_ENABLED) System.out.println("Counting questions for quiz ID: " + quizId);
            return quizQuestionService.countQuestionsInQuiz(quizId);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error counting questions for quiz ID: " + e.getMessage());
            throw e;
        }
    }

    // Check if a specific question is part of a specific quiz
    @GetMapping("/quiz/{quizId}/question/{questionId}/exists")
    public boolean isQuestionInQuiz(@PathVariable Long quizId, @PathVariable Long questionId) {
        try {
            if (LOGGING_ENABLED) System.out.println("Checking if question ID: " + questionId + " is in quiz ID: " + quizId);
            return quizQuestionService.isQuestionInQuiz(quizId, questionId);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error checking if question is in quiz: " + e.getMessage());
            throw e;
        }
    }

    // Add a new QuizQuestion mapping
    @PostMapping
    public QuizQuestion createQuizQuestion(@RequestBody QuizQuestion quizQuestion) {
        try {
            if (LOGGING_ENABLED) System.out.println("Creating a new QuizQuestion.");
            return quizQuestionService.saveQuizQuestion(quizQuestion);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error creating QuizQuestion: " + e.getMessage());
            throw e;
        }
    }

    // Add multiple QuizQuestion mappings
    @PostMapping("/batch")
    public List<QuizQuestion> createQuizQuestions(@RequestBody List<QuizQuestion> quizQuestions) {
        try {
            if (LOGGING_ENABLED) System.out.println("Creating multiple QuizQuestions.");
            return quizQuestionService.saveQuizQuestions(quizQuestions);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error creating multiple QuizQuestions: " + e.getMessage());
            throw e;
        }
    }

    // Update an existing QuizQuestion mapping
    @PutMapping("/{id}")
    public ResponseEntity<QuizQuestion> updateQuizQuestion(
            @PathVariable Long id,
            @RequestBody QuizQuestion updatedQuizQuestion) {
        try {
            if (LOGGING_ENABLED) System.out.println("Updating QuizQuestion with ID: " + id);
            return quizQuestionService.getQuizQuestionById(id).map(existingQuizQuestion -> {
                existingQuizQuestion.setQuiz(updatedQuizQuestion.getQuiz());
                existingQuizQuestion.setQuestion(updatedQuizQuestion.getQuestion());
                QuizQuestion savedQuizQuestion = quizQuestionService.updateQuizQuestionById(id, existingQuizQuestion);
                return ResponseEntity.ok(savedQuizQuestion);
            }).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error updating QuizQuestion: " + e.getMessage());
            throw e;
        }
    }

    // Delete a QuizQuestion mapping by ID
    @DeleteMapping("/{id}")
    public void deleteQuizQuestionById(@PathVariable Long id) {
        try {
            if (LOGGING_ENABLED) System.out.println("Deleting QuizQuestion with ID: " + id);
            quizQuestionService.deleteQuizQuestionById(id);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error deleting QuizQuestion: " + e.getMessage());
            throw e;
        }
    }
}
