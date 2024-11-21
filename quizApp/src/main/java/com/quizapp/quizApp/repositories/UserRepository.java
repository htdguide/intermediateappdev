package com.quizapp.quizApp.repositories;

import com.quizapp.quizApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean LOGGING_ENABLED = true; // Logging flag

    // Find a user by email
    default Optional<User> findByEmail(String email) {
        if (LOGGING_ENABLED) {
            System.out.println("Fetching user by email: " + email);
        }
        return findByEmailQuery(email);
    }

    // Find a user by ID
    default Optional<User> findByUserId(Long userId) {
        if (LOGGING_ENABLED) {
            System.out.println("Fetching user by ID: " + userId);
        }
        return findByUserIdQuery(userId);
    }

    // Find a user by first name and last name combined
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) = LOWER(:firstName) AND LOWER(u.lastName) = LOWER(:lastName)")
    Optional<User> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    // Internal method for findByEmail query
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmailQuery(@Param("email") String email);

    // Internal method for findByUserId query
    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    Optional<User> findByUserIdQuery(@Param("userId") Long userId);
}
