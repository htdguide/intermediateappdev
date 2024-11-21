package com.quizapp.quizApp.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashingUtil {

    private static final boolean LOGGING_ENABLED = true; // Debugging flag
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /**
     * Hashes the provided plain-text password.
     *
     * @param plainPassword The plain-text password.
     * @return The hashed password.
     */
    public static String hashPassword(String plainPassword) {
        try {
            if (LOGGING_ENABLED) System.out.println("Hashing password.");
            return PASSWORD_ENCODER.encode(plainPassword);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error hashing password: " + e.getMessage());
            throw e; // Propagate the exception if necessary
        }
    }

    /**
     * Verifies if the plain-text password matches the hashed password.
     *
     * @param plainPassword The plain-text password.
     * @param hashedPassword The hashed password.
     * @return True if passwords match, otherwise false.
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        try {
            if (LOGGING_ENABLED) System.out.println("Verifying password.");
            return PASSWORD_ENCODER.matches(plainPassword, hashedPassword);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error verifying password: " + e.getMessage());
            throw e; // Propagate the exception if necessary
        }
    }
}
