package com.quizapp.quizApp.services;

import com.quizapp.quizApp.model.Question;
import com.quizapp.quizApp.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // Get all questions
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    // Get a question by ID
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    // Get questions by difficulty
    public List<Question> getQuestionsByDifficulty(String difficulty) {
        return questionRepository.findByDifficulty(difficulty);
    }

    // Get questions by category
    public List<Question> getQuestionsByCategory(String category) {
        return questionRepository.findByCategory(category);
    }

    // Search questions by keyword
    public List<Question> searchQuestionsByKeyword(String keyword) {
        return questionRepository.findByTextContainingIgnoreCase(keyword);
    }

    // Get a question by ID, difficulty, and category
    public Optional<Question> getQuestionByIdDifficultyAndCategory(Long id, String difficulty, String category) {
        return questionRepository.findByIdAndDifficultyAndCategory(id, difficulty, category);
    }

    // Save or update a question
    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    // Save multiple questions
    public List<Question> saveQuestions(List<Question> questions) {
        return questionRepository.saveAll(questions);
    }


    // Delete a question by ID
    public void deleteQuestionById(Long id) {
        questionRepository.deleteById(id);
    }
}
