package com.quizapp.quizApp.services;

import com.quizapp.quizApp.model.UserRecord;
import com.quizapp.quizApp.repositories.UserRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserRecordService {

    private final UserRecordRepository userRecordRepository;

    @Autowired
    public UserRecordService(UserRecordRepository userRecordRepository) {
        this.userRecordRepository = userRecordRepository;
    }

    // Find a record by userId and quizId
    public UserRecord getUserRecordByUserIdAndQuizId(Long userId, Long quizId) {
        return userRecordRepository.findByUser_UserIdAndQuiz_QuizId(userId, quizId);
    }

    // Find all records for a specific user
    public List<UserRecord> getUserRecordsByUserId(Long userId) {
        return userRecordRepository.findByUser_UserId(userId);
    }

    // Find all records for a specific quiz
    public List<UserRecord> getUserRecordsByQuizId(Long quizId) {
        return userRecordRepository.findByQuiz_QuizId(quizId);
    }

    // Find all records for a specific user with scores greater than a threshold
    public List<UserRecord> getUserRecordsByUserIdAndScoreGreaterThan(Long userId, Integer score) {
        return userRecordRepository.findByUserIdAndScoreGreaterThan(userId, score);
    }

    // Find all records played within a specific date range
    public List<UserRecord> getRecordsPlayedBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return userRecordRepository.findRecordsPlayedBetweenDates(startDate, endDate);
    }

    // Find the top scorer for a specific quiz
    public List<UserRecord> getTopScorersByQuizId(Long quizId) {
        return userRecordRepository.findTopScorerByQuizId(quizId);
    }

    // Save or update a user record
    public UserRecord saveUserRecord(UserRecord userRecord) {
        return userRecordRepository.save(userRecord);
    }

    // Delete a user record by ID
    public void deleteUserRecordById(Long userRecordId) {
        userRecordRepository.deleteById(userRecordId);
    }
}
