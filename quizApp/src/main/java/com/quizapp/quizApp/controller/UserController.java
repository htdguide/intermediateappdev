package com.quizapp.quizApp.controller;

import com.quizapp.quizApp.model.User;
import com.quizapp.quizApp.model.UserType;
import com.quizapp.quizApp.services.UserService;
import com.quizapp.quizApp.utils.PasswordHashingUtil;
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
    private static final boolean LOGGING_ENABLED = false; // Logging flag

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
    public ResponseEntity<?> saveUser(@RequestBody @Valid User user) {
        try {
            if (LOGGING_ENABLED) System.out.println("Controller: Saving user: " + user.getEmail());

            // Check if email already exists
            if (userService.emailExists(user.getEmail())) {
                return ResponseEntity.badRequest().body("Email is already registered.");
            }

            // Set default usertype if not provided
            if (user.getUsertype() == null) {
                user.setUsertype(UserType.PLAYER);
            }

            // Hash the password before saving the user
            String hashedPassword = PasswordHashingUtil.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);

            User savedUser = userService.saveUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error in controller while saving user: " + e.getMessage());
            return ResponseEntity.status(500).body("Failed to save user.");
        }
    }

    // Update a user by ID
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable Long id, @RequestBody @Valid User updatedUser) {
        try {
            if (LOGGING_ENABLED) System.out.println("Controller: Updating user with ID: " + id);

            // If the password is being updated, hash it before saving
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                String hashedPassword = PasswordHashingUtil.hashPassword(updatedUser.getPassword());
                updatedUser.setPassword(hashedPassword);
            }

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

    @PostMapping("/login")
    public ResponseEntity<User> authenticateUser(@RequestBody User loginUser) {
        try {
            if (LOGGING_ENABLED) System.out.println("Request received: " + loginUser);

            String email = loginUser.getEmail();
            String password = loginUser.getPassword();

            if (LOGGING_ENABLED) System.out.println("Authenticating user with email: " + email);

            Optional<User> user = userService.authenticateUser(email, password);
            return user.map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        if (LOGGING_ENABLED) System.out.println("Authentication failed for email: " + email);
                        return ResponseEntity.status(401).body(null); // Unauthorized if user not found
                    });
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error in controller: " + e.getMessage());
            throw e;
        }
    }

}
