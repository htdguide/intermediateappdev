package com.quizapp.quizApp.repositories;

import com.quizapp.quizApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by email
    Optional<User> findByEmail(String email);

    // Find a user by ID
    Optional<User> findByUserId(Long userId);

    // Find a user by first name and last name combined
    @Query("SELECT u FROM User u WHERE u.firstName = :firstName AND u.lastName = :lastName")
    Optional<User> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
