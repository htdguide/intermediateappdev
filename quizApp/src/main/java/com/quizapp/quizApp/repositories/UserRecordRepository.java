package com.quizapp.quizApp.repositories;

import com.quizapp.quizApp.model.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRecordRepository extends JpaRepository<UserRecord, Long> {

    // Find a record by userId and quizId
    UserRecord findByUser_UserIdAndQuiz_QuizId(Long userId, Long quizId);

    // Find a record by userId and quizId
    //UserRecord findByUserIdAndQuizId(Long userId, Long quizId);

    // Find all records for a specific user
    List<UserRecord> findByUser_UserId(Long userId);

    // Find all records for a specific quiz
    List<UserRecord> findByQuiz_QuizId(Long quizId);

    // Find all records for a specific user with scores greater than a threshold
    @Query("SELECT ur FROM UserRecord ur WHERE ur.user.userId = :userId AND ur.score > :score")
    List<UserRecord> findByUserIdAndScoreGreaterThan(@Param("userId") Long userId, @Param("score") Integer score);

    // Find all records played within a specific date range
    @Query("SELECT ur FROM UserRecord ur WHERE ur.playedAt BETWEEN :startDate AND :endDate")
    List<UserRecord> findRecordsPlayedBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Find the top scorer for a specific quiz
    @Query("SELECT ur FROM UserRecord ur WHERE ur.quiz.quizId = :quizId ORDER BY ur.score DESC")
    List<UserRecord> findTopScorerByQuizId(@Param("quizId") Long quizId);
}
