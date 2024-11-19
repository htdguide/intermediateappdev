package com.quizapp.quizApp.repositories;

import com.quizapp.quizApp.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    // Find quiz by ID
    Optional<Quiz> findByQuizId(Long quizId);

    // Find quiz by title
    List<Quiz> findByTitleContaining(String title); // Supports partial match

    // Find quizzes starting on a specific date
    @Query("SELECT q FROM Quiz q WHERE q.startDate = :startDate")
    List<Quiz> findByStartDate(@Param("startDate") LocalDate startDate);

    // Find quizzes ending on a specific date
    @Query("SELECT q FROM Quiz q WHERE q.endDate = :endDate")
    List<Quiz> findByEndDate(@Param("endDate") LocalDate endDate);

    // Find quizzes that start between two dates
    @Query("SELECT q FROM Quiz q WHERE q.startDate BETWEEN :startDate AND :endDate")
    List<Quiz> findQuizzesBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
