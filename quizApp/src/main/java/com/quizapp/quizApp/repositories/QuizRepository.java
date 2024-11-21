package com.quizapp.quizApp.repositories;

import com.quizapp.quizApp.model.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    boolean LOGGING_ENABLED = true;

    // Find quiz by ID
    default Optional<Quiz> findByQuizId(Long quizId) {
        try {
            if (LOGGING_ENABLED) System.out.println("Finding Quiz by ID: " + quizId);
            return findById(quizId);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error finding Quiz by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    // Find quiz by title
    List<Quiz> findByTitleContaining(String title);

    // Find quizzes starting on a specific date
    @Query("SELECT q FROM Quiz q WHERE q.startDate = :startDate")
    List<Quiz> findByStartDate(@Param("startDate") LocalDate startDate);

    // Find quizzes ending on a specific date
    @Query("SELECT q FROM Quiz q WHERE q.endDate = :endDate")
    List<Quiz> findByEndDate(@Param("endDate") LocalDate endDate);

    // Find quizzes that start between two dates with pagination
    @Query("SELECT q FROM Quiz q WHERE q.startDate BETWEEN :startDate AND :endDate")
    Page<Quiz> findQuizzesBetweenDates(@Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate,
                                       Pageable pageable);

    // Find active quizzes
    @Query("SELECT q FROM Quiz q WHERE q.startDate <= CURRENT_DATE AND q.endDate > CURRENT_DATE")
    List<Quiz> findActiveQuizzes();
}
