package com.quizapp.quizApp.services;

import com.quizapp.quizApp.model.User;
import com.quizapp.quizApp.repositories.UserRecordRepository;
import com.quizapp.quizApp.repositories.UserRepository;
import com.quizapp.quizApp.utils.PasswordHashingUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    private UserRecordRepository userRecordRepository;
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

            // Hash the password if it is not already hashed
            if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) { // Avoid re-hashing
                user.setPassword(PasswordHashingUtil.hashPassword(user.getPassword()));
            }

            return userRepository.save(user);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error saving user: " + e.getMessage());
            throw e;
        }
    }

    // Delete a user by ID
    @Transactional
    public void deleteUserById(Long userId) {
        try {
            if (LOGGING_ENABLED) System.out.println("Deleting user by ID: " + userId);

            // Delete associated UserRecord entries first
            userRecordRepository.deleteByUserId(userId);

            // Now delete the User
            userRepository.deleteById(userId);

            if (LOGGING_ENABLED) System.out.println("User and associated records deleted successfully.");
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error deleting user by ID: " + e.getMessage());
            throw e; // Re-throw the exception to propagate it
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

    // Authenticate user based on email and password
    public Optional<User> authenticateUser(String email, String password) {
        try {
            if (LOGGING_ENABLED) System.out.println("Authenticating user with email: " + email);

            // Fetch user by email
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                // Check if the provided password matches the stored hashed password
                if (PasswordHashingUtil.verifyPassword(password, user.getPassword())) {
                    return Optional.of(user);  // Password is correct, return the user
                } else {
                    if (LOGGING_ENABLED) System.err.println("Authentication failed: Incorrect password.");
                    return Optional.empty();  // Incorrect password
                }
            } else {
                if (LOGGING_ENABLED) System.err.println("Authentication failed: User with email " + email + " not found.");
                return Optional.empty();  // User not found
            }
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error during authentication: " + e.getMessage());
            throw e;
        }
    }

}
