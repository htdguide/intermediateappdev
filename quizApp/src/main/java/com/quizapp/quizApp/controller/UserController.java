package com.quizapp.quizApp.controller;

import com.quizapp.quizApp.model.User;
import com.quizapp.quizApp.model.UserType;
import com.quizapp.quizApp.services.EmailService;
import com.quizapp.quizApp.services.UserService;
import com.quizapp.quizApp.services.VerificationCodeService;
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
    private final EmailService emailService;
    private final VerificationCodeService verificationCodeService;
    private static final boolean LOGGING_ENABLED = false; // Logging flag

    @Autowired
    public UserController(UserService userService, EmailService emailService, VerificationCodeService verificationCodeService) {
        this.userService = userService;
        this.emailService = emailService;
        this.verificationCodeService = verificationCodeService;
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

    @GetMapping("/test-email")
    public ResponseEntity<String> testEmail(
            @RequestParam String email,
            @RequestParam(required = false) String replyTo // Optional reply-to parameter
    ) {
        boolean emailSent = emailService.sendEmail(
                email,
                "Test Email from QuizApp",
                "This is a test email to verify the email service is working.",
                replyTo // Pass the reply-to address to the email service
        );

        if (emailSent) {
            return ResponseEntity.ok("Email sent successfully to: " + email +
                    (replyTo != null ? " with reply-to: " + replyTo : ""));
        } else {
            return ResponseEntity.status(500).body("Failed to send email to: " + email);
        }
    }

    @PostMapping("/reset-password-request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        try {
            Optional<User> userOptional = userService.getUserByEmail(email);

            if (userOptional.isPresent()) {
                // Generate a verification code
                String verificationCode = verificationCodeService.generateCode(email);

                // Send the verification code via email
                emailService.sendEmail(
                        email,
                        "Password Reset Request",
                        "Your password reset verification code is: " + verificationCode,
                        "dholakiaharshil7@gmail.com"
                );

                return ResponseEntity.ok("Password reset email sent successfully.");
            } else {
                return ResponseEntity.status(404).body("Email address not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing password reset request.");
        }
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<String> verifyResetCode(
            @RequestParam String email,
            @RequestParam String code
    ) {
        boolean isValid = verificationCodeService.validateCode(email, code);

        if (isValid) {
            return ResponseEntity.ok("Verification code is valid.");
        } else {
            return ResponseEntity.status(400).body("Invalid or expired verification code.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email,
            @RequestParam String code,
            @RequestParam String newPassword
    ) {
        boolean isValid = verificationCodeService.validateCode(email, code);

        if (isValid) {
            // Update the user's password
            Optional<User> userOptional = userService.getUserByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setPassword(PasswordHashingUtil.hashPassword(newPassword));
                userService.saveUser(user);

                // Remove the verification code after successful reset
                verificationCodeService.removeCode(email);

                return ResponseEntity.ok("Password has been reset successfully.");
            } else {
                return ResponseEntity.status(404).body("User not found.");
            }
        } else {
            return ResponseEntity.status(400).body("Invalid or expired verification code.");
        }
    }


}
