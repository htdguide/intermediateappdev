package com.quizapp.quizApp.services;

import com.quizapp.quizApp.model.Quiz;
import com.quizapp.quizApp.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    //Get all quizzes
    public List<Quiz> getAllQuiz() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> getQuizById(Long quizId) {
        return quizRepository.findByQuizId(quizId);
    }

    public Optional<Quiz> updateQuizById(Long quizId, Quiz updatedQuiz) {
        return quizRepository.findByQuizId(quizId).map(existingQuiz -> {
            existingQuiz.setTitle(updatedQuiz.getTitle());
            existingQuiz.setStartDate(updatedQuiz.getStartDate());
            existingQuiz.setEndDate(updatedQuiz.getEndDate());
            return quizRepository.save(existingQuiz);
        });
    }

    public List<Quiz> getQuizzesByTitle(String title) {
        return quizRepository.findByTitleContaining(title);
    }

    public List<Quiz> getQuizzesByStartDate(LocalDate startDate) {
        return quizRepository.findByStartDate(startDate);
    }

    public List<Quiz> getQuizzesByEndDate(LocalDate endDate) {
        return quizRepository.findByEndDate(endDate);
    }

    public List<Quiz> getQuizzesBetweenDates(LocalDate startDate, LocalDate endDate) {
        return quizRepository.findQuizzesBetweenDates(startDate, endDate);
    }

    public Quiz saveQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public void deleteQuizById(Long quizId) {
        quizRepository.deleteById(quizId);
    }
}
