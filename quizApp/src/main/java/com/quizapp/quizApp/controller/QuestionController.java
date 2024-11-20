package com.quizapp.quizApp.controller;

import com.quizapp.quizApp.model.Question;
import com.quizapp.quizApp.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // Get all questions
    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
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

    // Create a new question
    @PostMapping
    public Question createQuestion(@RequestBody Question question) {
        return questionService.saveQuestion(question);
    }

    // Update an existing question by ID
    @PutMapping("/{id}")
    public Optional<Question> updateQuestion(
            @PathVariable Long id,
            @RequestBody Question updatedQuestion) {
        return questionService.getQuestionById(id).map(existingQuestion -> {
            // Update only non-null fields
            if (updatedQuestion.getDifficulty() != null) {
                existingQuestion.setDifficulty(updatedQuestion.getDifficulty());
            }
            if (updatedQuestion.getCategory() != null) {
                existingQuestion.setCategory(updatedQuestion.getCategory());
            }
            if (updatedQuestion.getText() != null) {
                existingQuestion.setText(updatedQuestion.getText());
            }
            if (updatedQuestion.getAnswer() != null) {
                existingQuestion.setAnswer(updatedQuestion.getAnswer());
            }
            if (updatedQuestion.getIncorrectAnswers() != null && !updatedQuestion.getIncorrectAnswers().isEmpty()) {
                existingQuestion.setIncorrectAnswers(updatedQuestion.getIncorrectAnswers());
            }

            // Save the updated question
            return questionService.saveQuestion(existingQuestion);
        });
    }


    // Delete a question by ID
    @DeleteMapping("/{id}")
    public void deleteQuestionById(@PathVariable Long id) {
        questionService.deleteQuestionById(id);
    }
}