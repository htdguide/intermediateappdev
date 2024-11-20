package com.quizapp.quizApp.controller;

import com.quizapp.quizApp.model.QuizQuestion;
import com.quizapp.quizApp.services.QuizQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz-questions")
public class QuizQuestionController {

    private final QuizQuestionService quizQuestionService;

    @Autowired
    public QuizQuestionController(QuizQuestionService quizQuestionService) {
        this.quizQuestionService = quizQuestionService;
    }

    // Get all QuizQuestion mappings
    @GetMapping
    public List<QuizQuestion> getAllQuizQuestions() {
        return quizQuestionService.getAllQuizQuestions();
    }

    // Get all questions for a specific quiz by quizId
    @GetMapping("/quiz/{quizId}")
    public List<QuizQuestion> getQuestionsByQuizId(@PathVariable Long quizId) {
        return quizQuestionService.getQuestionsByQuizId(quizId);
    }

    // Get all quizzes containing a specific question by questionId
    @GetMapping("/question/{questionId}")
    public List<QuizQuestion> getQuizzesByQuestionId(@PathVariable Long questionId) {
        return quizQuestionService.getQuizzesByQuestionId(questionId);
    }

    // Count the number of questions in a specific quiz
    @GetMapping("/quiz/{quizId}/count")
    public Long countQuestionsInQuiz(@PathVariable Long quizId) {
        return quizQuestionService.countQuestionsInQuiz(quizId);
    }

    // Check if a specific question is part of a specific quiz
    @GetMapping("/quiz/{quizId}/question/{questionId}/exists")
    public boolean isQuestionInQuiz(@PathVariable Long quizId, @PathVariable Long questionId) {
        return quizQuestionService.isQuestionInQuiz(quizId, questionId);
    }

    // Add a new QuizQuestion mapping
    @PostMapping
    public QuizQuestion createQuizQuestion(@RequestBody QuizQuestion quizQuestion) {
        return quizQuestionService.saveQuizQuestion(quizQuestion);
    }

    // Add multiple QuizQuestion mappings
    @PostMapping("/batch")
    public List<QuizQuestion> createQuizQuestions(@RequestBody List<QuizQuestion> quizQuestions) {
        return quizQuestionService.saveQuizQuestions(quizQuestions);
    }

    // Update an existing QuizQuestion mapping
    @PutMapping("/{id}")
    public ResponseEntity<QuizQuestion> updateQuizQuestion(
            @PathVariable Long id,
            @RequestBody QuizQuestion updatedQuizQuestion) {
        return quizQuestionService.getQuizQuestionById(id).map(existingQuizQuestion -> {
            existingQuizQuestion.setQuiz(updatedQuizQuestion.getQuiz());
            existingQuizQuestion.setQuestion(updatedQuizQuestion.getQuestion());
            QuizQuestion savedQuizQuestion = quizQuestionService.saveQuizQuestion(existingQuizQuestion);
            return ResponseEntity.ok(savedQuizQuestion);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete a QuizQuestion mapping by ID
    @DeleteMapping("/{id}")
    public void deleteQuizQuestionById(@PathVariable Long id) {
        quizQuestionService.deleteQuizQuestionById(id);
    }
}
