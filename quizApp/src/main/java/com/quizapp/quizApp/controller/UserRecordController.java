package com.quizapp.quizApp.controller;

import com.quizapp.quizApp.model.UserRecord;
import com.quizapp.quizApp.services.UserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/user-records")
public class UserRecordController {

    private final UserRecordService userRecordService;

    @Autowired
    public UserRecordController(UserRecordService userRecordService) {
        this.userRecordService = userRecordService;
    }

    // Get all user records
    @GetMapping
    public List<UserRecord> getAllUserRecords() {
        return userRecordService.getAllUserRecords();
    }

    // Get a user record by userId and quizId
    @GetMapping("/user/{userId}/quiz/{quizId}")
    public ResponseEntity<?> getUserRecordByUserIdAndQuizId(
            @PathVariable Long userId, @PathVariable Long quizId) {
        UserRecord record = userRecordService.getUserRecordByUserIdAndQuizId(userId, quizId);

        if (record != null) {
            return ResponseEntity.ok(record); // Return the record if found
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No record found for userId " + userId + " and quizId " + quizId); // Handle null value
        }
    }

    // Get all records for a specific user
    @GetMapping("/user/{userId}")
    public List<UserRecord> getUserRecordsByUserId(@PathVariable Long userId) {
        return userRecordService.getUserRecordsByUserId(userId);
    }

    // Get all records for a specific quiz
    @GetMapping("/quiz/{quizId}")
    public List<UserRecord> getUserRecordsByQuizId(@PathVariable Long quizId) {
        return userRecordService.getUserRecordsByQuizId(quizId);
    }

    // Get all records played within a specific date range
    @GetMapping("/played-between")
    public List<UserRecord> getRecordsPlayedBetweenDates(
            @RequestParam String startDate, @RequestParam String endDate) {
        return userRecordService.getRecordsPlayedBetweenDates(
                LocalDateTime.parse(startDate), LocalDateTime.parse(endDate));
    }

//    // Add a new user record
//    @PostMapping
//    public UserRecord createUserRecord(@RequestBody UserRecord userRecord) {
//        return userRecordService.saveUserRecord(userRecord);
//    }

    // Add multiple user records
    @PostMapping("/batch")
    public List<UserRecord> createUserRecords(@RequestBody List<UserRecord> userRecords) {
        return userRecordService.saveUserRecords(userRecords);
    }

    // Update an existing user record by ID
    @PutMapping("/{id}")
    public ResponseEntity<UserRecord> updateUserRecord(
            @PathVariable Long id, @RequestBody UserRecord updatedRecord) {
        return userRecordService.getUserRecordById(id).map(existingRecord -> {
            existingRecord.setUser(updatedRecord.getUser());
            existingRecord.setQuiz(updatedRecord.getQuiz());
            existingRecord.setScore(updatedRecord.getScore());
            existingRecord.setPlayedAt(updatedRecord.getPlayedAt());
            UserRecord savedRecord = userRecordService.saveUserRecord(existingRecord);
            return ResponseEntity.ok(savedRecord);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete a user record by ID
    @DeleteMapping("/{id}")
    public void deleteUserRecordById(@PathVariable Long id) {
        userRecordService.deleteUserRecordById(id);
    }

    // Add or update a user record
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createOrUpdateUserRecord(@RequestBody UserRecord userRecord) {
        try {
            System.out.println("Received UserRecord: " + userRecord);

            // Validate user and quiz fields
            if (userRecord.getUser() == null || userRecord.getUser().getUserId() == null) {
                System.err.println("Invalid user information: " + userRecord.getUser());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid user information in the request.");
            }

            if (userRecord.getQuiz() == null || userRecord.getQuiz().getQuizId() == null) {
                System.err.println("Invalid quiz information: " + userRecord.getQuiz());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid quiz information in the request.");
            }

            // Proceed with existing logic...
            UserRecord existingRecord = userRecordService.getUserRecordByUserIdAndQuizId(
                    userRecord.getUser().getUserId(),
                    userRecord.getQuiz().getQuizId()
            );

            if (existingRecord != null) {
                existingRecord.setScore(userRecord.getScore());
                existingRecord.setPlayedAt(LocalDateTime.now());
                UserRecord updatedRecord = userRecordService.saveUserRecord(existingRecord);
                return ResponseEntity.ok(updatedRecord);
            }

            userRecord.setPlayedAt(LocalDateTime.now());
            UserRecord newRecord = userRecordService.saveUserRecord(userRecord);
            return ResponseEntity.ok(newRecord);

        } catch (Exception e) {
            System.err.println("Error in createOrUpdateUserRecord: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating or updating the user record.");
        }
    }

    // Submit or update a user record for a quiz
    @PostMapping("/submit-quiz")
    public ResponseEntity<UserRecord> submitQuiz(
            @RequestParam Long userId,
            @RequestParam Long quizId,
            @RequestParam Integer score) {
        try {
            UserRecord userRecord = userRecordService.createOrUpdateUserRecord(userId, quizId, score);
            return ResponseEntity.ok(userRecord);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

}
