package com.quizapp.quizApp.controller;

import com.quizapp.quizApp.model.User;
import com.quizapp.quizApp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Validated  // Enable validation support for the controller
public class UserController {

    private final UserService userService;
    private static final boolean LOGGING_ENABLED = true; // Logging flag

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        if (LOGGING_ENABLED) System.out.println("Controller: Fetching all users.");
        return userService.getAllUsers();
    }

    // Get a user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        try {
            if (LOGGING_ENABLED) System.out.println("Controller: Fetching user by email: " + email);
            Optional<User> user = userService.getUserByEmail(email);
            return user.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error in controller while fetching user by email: " + e.getMessage());
            throw e;
        }
    }

    // Get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            if (LOGGING_ENABLED) System.out.println("Controller: Fetching user by ID: " + id);
            Optional<User> user = userService.getUserById(id);
            return user.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error in controller while fetching user by ID: " + e.getMessage());
            throw e;
        }
    }

    // Get a user by first and last name
    @GetMapping("/name")
    public ResponseEntity<User> getUserByFirstAndLastName(
            @RequestParam String firstName,
            @RequestParam String lastName) {
        try {
            if (LOGGING_ENABLED) System.out.println("Controller: Fetching user by name: " + firstName + " " + lastName);
            Optional<User> user = userService.getUserByFirstAndLastName(firstName, lastName);
            return user.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error in controller while fetching user by name: " + e.getMessage());
            throw e;
        }
    }

    // Create or update a user
    @PostMapping
    public User saveUser(@RequestBody @Valid User user) {
        try {
            if (LOGGING_ENABLED) System.out.println("Controller: Saving user: " + user.getEmail());
            return userService.saveUser(user);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error in controller while saving user: " + e.getMessage());
            throw e;
        }
    }

    // Update a user by ID
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable Long id, @RequestBody @Valid User updatedUser) {
        try {
            if (LOGGING_ENABLED) System.out.println("Controller: Updating user with ID: " + id);
            Optional<User> updated = userService.updateUserById(id, updatedUser);
            return updated.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error in controller while updating user: " + e.getMessage());
            throw e;
        }
    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        try {
            if (LOGGING_ENABLED) System.out.println("Controller: Deleting user with ID: " + id);
            userService.deleteUserById(id);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error in controller while deleting user: " + e.getMessage());
            throw e;
        }
    }
}
