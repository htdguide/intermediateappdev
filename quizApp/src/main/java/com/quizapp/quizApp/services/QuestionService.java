package com.quizapp.quizApp.services;

import com.quizapp.quizApp.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/questions")
public class QuestionService {

    private final QuestionService questionService;

    @Autowired
    public QuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    // Get a question by ID
    @GetMapping("/{id}")
    public Optional<Question> getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }

    // Get questions by difficulty
    @GetMapping("/difficulty/{difficulty}")
    public List<Question> getQuestionsByDifficulty(@PathVariable String difficulty) {
        return questionService.getQuestionsByDifficulty(difficulty);
    }

    // Get questions by category
    @GetMapping("/category/{category}")
    public List<Question> getQuestionsByCategory(@PathVariable String category) {
        return questionService.getQuestionsByCategory(category);
    }

    // Search questions by keyword
    @GetMapping("/search")
    public List<Question> searchQuestionsByKeyword(@RequestParam String keyword) {
        return questionService.searchQuestionsByKeyword(keyword);
    }

    // Get a question by ID, difficulty, and category
    @GetMapping("/{id}/details")
    public Optional<Question> getQuestionByIdDifficultyAndCategory(
            @PathVariable Long id,
            @RequestParam String difficulty,
            @RequestParam String category) {
        return questionService.getQuestionByIdDifficultyAndCategory(id, difficulty, category);
    }

    // Create or update a question
    @PostMapping
    public Question saveQuestion(@RequestBody Question question) {
        return questionService.saveQuestion(question);
    }

    // Delete a question by ID
    @DeleteMapping("/{id}")
    public void deleteQuestionById(@PathVariable Long id) {
        questionService.deleteQuestionById(id);
    }
}