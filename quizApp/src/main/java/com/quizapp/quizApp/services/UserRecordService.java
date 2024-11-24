package com.quizapp.quizApp.services;

import com.quizapp.quizApp.model.Quiz;
import com.quizapp.quizApp.model.User;
import com.quizapp.quizApp.model.UserRecord;
import com.quizapp.quizApp.repositories.QuizRepository;
import com.quizapp.quizApp.repositories.UserRecordRepository;
import com.quizapp.quizApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserRecordService {

    private final UserRecordRepository userRecordRepository;
    private final UserRepository userRepository;  // Inject UserRepository
    private final QuizRepository quizRepository;  // Inject QuizRepository

    @Autowired
    public UserRecordService(UserRecordRepository userRecordRepository, UserRepository userRepository, QuizRepository quizRepository) {
        this.userRecordRepository = userRecordRepository;
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
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

    // Find a record by userId and quizId
    public UserRecord createOrUpdateUserRecord(Long userId, Long quizId, Integer score) {
        // Fetch the user and quiz objects using the repositories
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));

        // Check if a record already exists for this user and quiz
        UserRecord existingRecord = userRecordRepository.findByUser_UserIdAndQuiz_QuizId(userId, quizId);

        if (existingRecord != null) {
            // If the record exists, update it
            existingRecord.setScore(score);
            existingRecord.setPlayedAt(LocalDateTime.now()); // Update playedAt to the current time
            return userRecordRepository.save(existingRecord);
        } else {
            // If the record does not exist, create a new record
            UserRecord newRecord = new UserRecord(user, quiz, score, LocalDateTime.now());
            return userRecordRepository.save(newRecord);
        }
    }

}
