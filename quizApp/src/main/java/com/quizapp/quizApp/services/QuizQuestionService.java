package com.quizapp.quizApp.services;

import com.quizapp.quizApp.model.QuizQuestion;
import com.quizapp.quizApp.repositories.QuizQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizQuestionService {

    private final QuizQuestionRepository quizQuestionRepository;

    @Autowired
    public QuizQuestionService(QuizQuestionRepository quizQuestionRepository) {
        this.quizQuestionRepository = quizQuestionRepository;
    }

    // Get all questions for a specific quiz by quizId
    public List<QuizQuestion> getQuestionsByQuizId(Long quizId) {
        return quizQuestionRepository.findByQuiz_QuizId(quizId);
    }

    // Get all quizzes containing a specific question by questionId
    public List<QuizQuestion> getQuizzesByQuestionId(Long questionId) {
        return quizQuestionRepository.findByQuestion_QuestionId(questionId);
    }

    // Count the number of questions in a specific quiz
    public Long countQuestionsInQuiz(Long quizId) {
        return quizQuestionRepository.countQuestionsInQuiz(quizId);
    }

    // Check if a specific question is part of a specific quiz
    public boolean isQuestionInQuiz(Long quizId, Long questionId) {
        return quizQuestionRepository.existsByQuizIdAndQuestionId(quizId, questionId);
    }

    // Get all QuizQuestion mappings for a quiz sorted by quizQuestionId
    public List<QuizQuestion> getQuestionsByQuizIdSorted(Long quizId) {
        return quizQuestionRepository.findQuestionsByQuizIdSorted(quizId);
    }

    // Save a QuizQuestion mapping
    public QuizQuestion saveQuizQuestion(QuizQuestion quizQuestion) {
        return quizQuestionRepository.save(quizQuestion);
    }

    // Delete a QuizQuestion mapping by ID
    public void deleteQuizQuestionById(Long quizQuestionId) {
        quizQuestionRepository.deleteById(quizQuestionId);
    }
}
