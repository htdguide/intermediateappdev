package com.quizapp.quizApp.controller;

import com.quizapp.quizApp.model.Quiz;
import com.quizapp.quizApp.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // Get all quizzes
    @GetMapping
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuiz();
    }

    // Get a quiz by ID
    @GetMapping("/{id}")
    public Optional<Quiz> getQuizById(@PathVariable Long id) {
        return quizService.getQuizById(id);
    }

    // Get quizzes by title (partial match)
    @GetMapping("/search")
    public List<Quiz> getQuizzesByTitle(@RequestParam String title) {
        return quizService.getQuizzesByTitle(title);
    }

    // Get quizzes starting on a specific date
    @GetMapping("/start-date")
    public List<Quiz> getQuizzesByStartDate(@RequestParam LocalDate startDate) {
        return quizService.getQuizzesByStartDate(startDate);
    }

    // Get quizzes ending on a specific date
    @GetMapping("/end-date")
    public List<Quiz> getQuizzesByEndDate(@RequestParam LocalDate endDate) {
        return quizService.getQuizzesByEndDate(endDate);
    }

    // Get quizzes starting between two dates
    @GetMapping("/between-dates")
    public List<Quiz> getQuizzesBetweenDates(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return quizService.getQuizzesBetweenDates(startDate, endDate);
    }

    // Create a new quiz
    @PostMapping
    public Quiz createQuiz(@RequestBody Quiz quiz) {
        return quizService.saveQuiz(quiz);
    }

    // Update a quiz by ID
    @PutMapping("/{id}")
    public Optional<Quiz> updateQuizById(@PathVariable Long id, @RequestBody Quiz updatedQuiz) {
        return quizService.updateQuizById(id, updatedQuiz);
    }

    // Delete a quiz by ID
    @DeleteMapping("/{id}")
    public void deleteQuizById(@PathVariable Long id) {
        quizService.deleteQuizById(id);
    }
}

