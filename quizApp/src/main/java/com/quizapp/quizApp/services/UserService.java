package com.quizapp.quizApp.services;

import com.quizapp.quizApp.model.User;
import com.quizapp.quizApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Find a user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Find a user by ID
    public Optional<User> getUserById(Long userId) {
        return userRepository.findByUserId(userId);
    }

    // Find a user by first name and last name
    public Optional<User> getUserByFirstAndLastName(String firstName, String lastName) {
        return userRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    // Save or update a user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Delete a user by ID
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    // Update a user by ID
    public Optional<User> updateUserById(Long userId, User updatedUser) {
        return userRepository.findByUserId(userId).map(user -> {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setUsertype(updatedUser.getUsertype());
            return userRepository.save(user);
        });
    }
}
