package com.quizapp.quizApp.services;

import com.quizapp.quizApp.model.UserRecord;
import com.quizapp.quizApp.repositories.UserRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserRecordService {

    private final UserRecordRepository userRecordRepository;

    @Autowired
    public UserRecordService(UserRecordRepository userRecordRepository) {
        this.userRecordRepository = userRecordRepository;
    }

    // Get all user records
    public List<UserRecord> getAllUserRecords() {
        return userRecordRepository.findAll();
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

    // Find all records played within a specific date range
    public List<UserRecord> getRecordsPlayedBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return userRecordRepository.findRecordsPlayedBetweenDates(startDate, endDate);
    }

    // Save or update a user record
    public UserRecord saveUserRecord(UserRecord userRecord) {
        return userRecordRepository.save(userRecord);
    }

    // Save multiple user records
    public List<UserRecord> saveUserRecords(List<UserRecord> userRecords) {
        return userRecordRepository.saveAll(userRecords);
    }

    // Find a user record by ID
    public Optional<UserRecord> getUserRecordById(Long id) {
        return userRecordRepository.findById(id);
    }

    // Delete a user record by ID
    public void deleteUserRecordById(Long userRecordId) {
        userRecordRepository.deleteById(userRecordId);
    }
}
