package com.quizapp.quizApp.services;

import com.quizapp.quizApp.model.Quiz;
import com.quizapp.quizApp.repositories.QuizRepository;
import com.quizapp.quizApp.repositories.UserRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRecordRepository userRecordRepository;
    private static final boolean LOGGING_ENABLED = true;

    @Autowired
    public QuizService(QuizRepository quizRepository, UserRecordRepository userRecordRepository) {
        this.quizRepository = quizRepository;
        this.userRecordRepository = userRecordRepository;
    }

    // Get all quizzes
    public List<Quiz> getAllQuizzes() {
        if (LOGGING_ENABLED) System.out.println("Fetching all quizzes.");
        return quizRepository.findAll();
    }

    // Get a quiz by ID
    public Optional<Quiz> getQuizById(Long quizId) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching quiz by ID: " + quizId);
            return quizRepository.findById(quizId);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching quiz: " + e.getMessage());
            return Optional.empty();
        }
    }

    // Get quizzes by title (partial match)
    public List<Quiz> getQuizzesByTitle(String title) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching quizzes by title: " + title);
            return quizRepository.findByTitleContaining(title);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching quizzes by title: " + e.getMessage());
            throw e;
        }
    }

    // Get quizzes starting on a specific date
    public List<Quiz> getQuizzesByStartDate(LocalDate startDate) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching quizzes by start date: " + startDate);
            return quizRepository.findByStartDate(startDate);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching quizzes by start date: " + e.getMessage());
            throw e;
        }
    }

    // Get quizzes ending on a specific date
    public List<Quiz> getQuizzesByEndDate(LocalDate endDate) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching quizzes by end date: " + endDate);
            return quizRepository.findByEndDate(endDate);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching quizzes by end date: " + e.getMessage());
            throw e;
        }
    }

    // Get quizzes starting between two dates
    public Page<Quiz> getQuizzesBetweenDates(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching quizzes between dates: " + startDate + " and " + endDate);
            return quizRepository.findQuizzesBetweenDates(startDate, endDate, pageable);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching quizzes between dates: " + e.getMessage());
            throw e;
        }
    }

    // Save a single quiz
    public Quiz saveQuiz(Quiz quiz) {
        try {
            if (LOGGING_ENABLED) System.out.println("Saving single quiz: " + quiz.getTitle());
            return quizRepository.save(quiz);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error saving quiz: " + e.getMessage());
            throw e;
        }
    }

    // Update a quiz by ID
    public Optional<Quiz> updateQuizById(Long id, Quiz updatedQuiz) {
        try {
            if (LOGGING_ENABLED) System.out.println("Updating quiz with ID: " + id);
            return quizRepository.findById(id).map(existingQuiz -> {
                existingQuiz.setTitle(updatedQuiz.getTitle());
                existingQuiz.setStartDate(updatedQuiz.getStartDate());
                existingQuiz.setEndDate(updatedQuiz.getEndDate());
                return quizRepository.save(existingQuiz);
            });
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error updating quiz: " + e.getMessage());
            throw e;
        }
    }

    // Delete a quiz by ID
    @Transactional
    public void deleteQuizById(Long quizId) {
        try {
            if (LOGGING_ENABLED) System.out.println("Deleting quiz by ID: " + quizId);

            // Delete associated records in dependent table(s) first
            userRecordRepository.deleteByQuizId(quizId);

            // Now delete the quiz
            quizRepository.deleteById(quizId);

            if (LOGGING_ENABLED) System.out.println("Quiz and associated records deleted successfully.");
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error deleting quiz by ID: " + e.getMessage());
            throw e; // Re-throw the exception to propagate it
        }
    }

}
