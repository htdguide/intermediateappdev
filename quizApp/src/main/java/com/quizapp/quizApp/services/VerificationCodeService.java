package com.quizapp.quizApp.services;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationCodeService {

    private static final int EXPIRATION_MINUTES = 15;

    // Store verification codes in memory (email -> code, expiration time)
    private final Map<String, VerificationData> verificationCodes = new ConcurrentHashMap<>();

    // Generate and store a verification code
    public String generateCode(String email) {
        String code = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);

        verificationCodes.put(email, new VerificationData(code, expirationTime));
        return code;
    }

    // Validate a verification code
    public boolean validateCode(String email, String code) {
        VerificationData data = verificationCodes.get(email);

        if (data == null || data.getExpirationTime().isBefore(LocalDateTime.now())) {
            return false; // Invalid or expired
        }

        return data.getCode().equals(code);
    }

    // Remove a verification code after successful validation
    public void removeCode(String email) {
        verificationCodes.remove(email);
    }

    // Inner class to hold verification data
    private static class VerificationData {
        private final String code;
        private final LocalDateTime expirationTime;

        public VerificationData(String code, LocalDateTime expirationTime) {
            this.code = code;
            this.expirationTime = expirationTime;
        }

        public String getCode() {
            return code;
        }

        public LocalDateTime getExpirationTime() {
            return expirationTime;
        }
    }
}
