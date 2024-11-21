package com.quizapp.quizApp.controller;

import com.quizapp.quizApp.model.Quiz;
import com.quizapp.quizApp.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizService quizService;
    private static final boolean LOGGING_ENABLED = true;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // Get all quizzes
    @GetMapping
    public List<Quiz> getAllQuizzes() {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching all quizzes.");
            return quizService.getAllQuizzes();
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching all quizzes: " + e.getMessage());
            throw e;
        }
    }

    // Get a quiz by ID
    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching quiz by ID: " + id);
            return quizService.getQuizById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching quiz by ID: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    // Get quizzes by title (partial match)
    @GetMapping("/search")
    public List<Quiz> getQuizzesByTitle(@RequestParam String title) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching quizzes by title: " + title);
            return quizService.getQuizzesByTitle(title);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching quizzes by title: " + e.getMessage());
            throw e;
        }
    }

    // Get quizzes starting on a specific date
    @GetMapping("/start-date")
    public List<Quiz> getQuizzesByStartDate(@RequestParam LocalDate startDate) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching quizzes by start date: " + startDate);
            return quizService.getQuizzesByStartDate(startDate);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching quizzes by start date: " + e.getMessage());
            throw e;
        }
    }

    // Get quizzes ending on a specific date
    @GetMapping("/end-date")
    public List<Quiz> getQuizzesByEndDate(@RequestParam LocalDate endDate) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching quizzes by end date: " + endDate);
            return quizService.getQuizzesByEndDate(endDate);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching quizzes by end date: " + e.getMessage());
            throw e;
        }
    }

    // Get quizzes starting between two dates
    @GetMapping("/between-dates")
    public Page<Quiz> getQuizzesBetweenDates(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching quizzes between dates in controller.");
            Pageable pageable = PageRequest.of(page, size);
            return quizService.getQuizzesBetweenDates(startDate, endDate, pageable);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error in controller while fetching quizzes between dates: " + e.getMessage());
            throw e;
        }
    }

    // Create a new quiz
    @PostMapping
    public Quiz createQuiz(@RequestBody Quiz quiz) {
        try {
            if (LOGGING_ENABLED) System.out.println("Creating a new quiz.");
            return quizService.saveQuiz(quiz);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error creating quiz: " + e.getMessage());
            throw e;
        }
    }

    // Update a quiz by ID
    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuizById(@PathVariable Long id, @RequestBody Quiz updatedQuiz) {
        try {
            if (LOGGING_ENABLED) System.out.println("Updating quiz with ID: " + id);
            return quizService.updateQuizById(id, updatedQuiz)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error updating quiz: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    // Delete a quiz by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizById(@PathVariable Long id) {
        try {
            if (LOGGING_ENABLED) System.out.println("Deleting quiz with ID: " + id);
            quizService.deleteQuizById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error deleting quiz: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
