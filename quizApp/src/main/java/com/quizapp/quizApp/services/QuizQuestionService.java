package com.quizapp.quizApp.services;

import com.quizapp.quizApp.model.QuizQuestion;
import com.quizapp.quizApp.repositories.QuizQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizQuestionService {

    private final QuizQuestionRepository quizQuestionRepository;
    private static final boolean LOGGING_ENABLED = true;

    @Autowired
    public QuizQuestionService(QuizQuestionRepository quizQuestionRepository) {
        this.quizQuestionRepository = quizQuestionRepository;
    }

    // Get all QuizQuestion mappings
    public List<QuizQuestion> getAllQuizQuestions() {
        if (LOGGING_ENABLED) System.out.println("Fetching all QuizQuestions.");
        return quizQuestionRepository.findAll();
    }

    // Get a QuizQuestion by its ID
    public Optional<QuizQuestion> getQuizQuestionById(Long quizQuestionId) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching QuizQuestion by ID: " + quizQuestionId);
            return quizQuestionRepository.findById(quizQuestionId);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching QuizQuestion: " + e.getMessage());
            return Optional.empty();
        }
    }

    // Get all questions for a specific quiz by quizId
    public List<QuizQuestion> getQuestionsByQuizId(Long quizId) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching questions for quiz ID: " + quizId);
            return quizQuestionRepository.findByQuiz_QuizId(quizId);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching questions for quiz ID: " + e.getMessage());
            throw e;
        }
    }

    // Get all quizzes containing a specific question by questionId
    public List<QuizQuestion> getQuizzesByQuestionId(Long questionId) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching quizzes for question ID: " + questionId);
            return quizQuestionRepository.findByQuestion_QuestionId(questionId);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching quizzes for question ID: " + e.getMessage());
            throw e;
        }
    }

    // Count the number of questions in a specific quiz
    public Long countQuestionsInQuiz(Long quizId) {
        try {
            if (LOGGING_ENABLED) System.out.println("Counting questions for quiz ID: " + quizId);
            return quizQuestionRepository.countQuestionsInQuiz(quizId);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error counting questions for quiz ID: " + e.getMessage());
            throw e;
        }
    }

    // Check if a specific question is part of a specific quiz
    public boolean isQuestionInQuiz(Long quizId, Long questionId) {
        try {
            if (LOGGING_ENABLED) System.out.println("Checking if question ID: " + questionId + " is in quiz ID: " + quizId);
            return quizQuestionRepository.existsByQuizIdAndQuestionId(quizId, questionId);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error checking if question is in quiz: " + e.getMessage());
            throw e;
        }
    }

    // Save a single QuizQuestion
    public QuizQuestion saveQuizQuestion(QuizQuestion quizQuestion) {
        try {
            if (LOGGING_ENABLED) System.out.println("Saving single QuizQuestion.");
            return quizQuestionRepository.save(quizQuestion);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error saving QuizQuestion: " + e.getMessage());
            return null;
        }
    }

    // Save multiple QuizQuestions
    public List<QuizQuestion> saveQuizQuestions(List<QuizQuestion> quizQuestions) {
        try {
            if (LOGGING_ENABLED) System.out.println("Saving multiple QuizQuestions.");
            return quizQuestionRepository.saveAll(quizQuestions);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error saving QuizQuestions: " + e.getMessage());
            throw e;
        }
    }

    // Update a QuizQuestion by ID
    public QuizQuestion updateQuizQuestionById(Long id, QuizQuestion updatedQuizQuestion) {
        try {
            if (LOGGING_ENABLED) System.out.println("Updating QuizQuestion with ID: " + id);
            return quizQuestionRepository.findById(id).map(existingQuizQuestion -> {
                existingQuizQuestion.setQuiz(updatedQuizQuestion.getQuiz());
                existingQuizQuestion.setQuestion(updatedQuizQuestion.getQuestion());
                return quizQuestionRepository.save(existingQuizQuestion);
            }).orElseThrow(() -> new IllegalArgumentException("QuizQuestion with ID " + id + " not found."));
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error updating QuizQuestion with ID: " + id + " - " + e.getMessage());
            throw e;
        }
    }

    // Delete a QuizQuestion by ID
    public void deleteQuizQuestionById(Long quizQuestionId) {
        try {
            if (LOGGING_ENABLED) System.out.println("Deleting QuizQuestion by ID: " + quizQuestionId);
            quizQuestionRepository.deleteById(quizQuestionId);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error deleting QuizQuestion: " + e.getMessage());
        }
    }
}