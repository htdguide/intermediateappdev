package com.quizapp.quizApp.services;

import com.quizapp.quizApp.model.User;
import com.quizapp.quizApp.repositories.UserRepository;
import com.quizapp.quizApp.utils.PasswordHashingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final boolean LOGGING_ENABLED = true; // Logging flag

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all users
    public List<User> getAllUsers() {
        if (LOGGING_ENABLED) System.out.println("Fetching all users.");
        return userRepository.findAll();
    }

    // Find a user by email
    public Optional<User> getUserByEmail(String email) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching user by email: " + email);
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching user by email: " + e.getMessage());
            throw e;
        }
    }

    // Find a user by ID
    public Optional<User> getUserById(Long userId) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching user by ID: " + userId);
            return userRepository.findByUserId(userId);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching user by ID: " + e.getMessage());
            throw e;
        }
    }

    // Find a user by first name and last name
    public Optional<User> getUserByFirstAndLastName(String firstName, String lastName) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching user by first name: " + firstName + " and last name: " + lastName);
            return userRepository.findByFirstNameAndLastName(firstName, lastName);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching user by name: " + e.getMessage());
            throw e;
        }
    }

    // Save or update a user
    public User saveUser(User user) {
        try {
            if (LOGGING_ENABLED) System.out.println("Saving user: " + user.getEmail());
            return userRepository.save(user);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error saving user: " + e.getMessage());
            throw e;
        }
    }

    // Delete a user by ID
    public void deleteUserById(Long userId) {
        try {
            if (LOGGING_ENABLED) System.out.println("Deleting user by ID: " + userId);
            userRepository.deleteById(userId);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error deleting user by ID: " + e.getMessage());
            throw e;
        }
    }

    // Update a user by ID
    public Optional<User> updateUserById(Long userId, User updatedUser) {
        try {
            if (LOGGING_ENABLED) System.out.println("Updating user by ID: " + userId);
            return userRepository.findByUserId(userId).map(user -> {
                if (updatedUser.getFirstName() != null) {
                    user.setFirstName(updatedUser.getFirstName());
                }
                if (updatedUser.getLastName() != null) {
                    user.setLastName(updatedUser.getLastName());
                }
                if (updatedUser.getEmail() != null) {
                    user.setEmail(updatedUser.getEmail());
                }
                if (updatedUser.getPassword() != null) {
                    // Hash the password before saving
                    if (LOGGING_ENABLED) System.out.println("Hashing password for user ID: " + userId);
                    user.setPassword(PasswordHashingUtil.hashPassword(updatedUser.getPassword()));
                }
                if (updatedUser.getUsertype() != null) {
                    user.setUsertype(updatedUser.getUsertype());
                }
                return userRepository.save(user);
            });
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error updating user by ID: " + e.getMessage());
            throw e;
        }
    }

}
