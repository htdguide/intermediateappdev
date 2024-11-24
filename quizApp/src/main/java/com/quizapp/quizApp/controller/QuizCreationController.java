package com.quizapp.quizApp.controller;

import com.quizapp.quizApp.model.Quiz;
import com.quizapp.quizApp.services.QuizCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/quizcreation")
public class QuizCreationController {

    private final QuizCreationService quizCreationService;

    @Autowired
    public QuizCreationController(QuizCreationService quizCreationService) {
        this.quizCreationService = quizCreationService;
    }

    /**
     * Endpoint to create a quiz with random questions.
     *
     * @param title     Quiz title
     * @param startDate Quiz start date
     * @param endDate   Quiz end date
     * @param category  Question category
     * @param difficulty Question difficulty level
     * @return Created quiz
     */
    @PostMapping("/create")
    public ResponseEntity<Quiz> createQuiz(
            @RequestParam String title,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam String category,
            @RequestParam String difficulty
    ) {
        try {
            // Use the service to create a quiz
            Quiz quiz = quizCreationService.createQuizWithRandomQuestions(title, startDate, endDate, category, difficulty);

            // Return the created quiz
            return ResponseEntity.ok(quiz);
        } catch (Exception e) {
            // Handle errors gracefully
            return ResponseEntity.badRequest().body(null);
        }
    }
}
