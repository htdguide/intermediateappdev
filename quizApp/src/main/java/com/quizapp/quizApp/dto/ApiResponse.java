package com.quizapp.quizApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ApiResponse {

    private static final boolean LOGGING_ENABLED = true; // Logging flag

    @JsonProperty("response_code")  // Map "response_code" from JSON to this field
    private int responseCode;

    private List<ApiQuestion> results;

    // Getters and Setters
    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        logDebug("Set responseCode: " + responseCode);
    }

    public List<ApiQuestion> getResults() {
        return results;
    }

    public void setResults(List<ApiQuestion> results) {
        this.results = results;
        logDebug("Set results: " + results);
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "responseCode=" + responseCode +
                ", results=" + results +
                '}';
    }

    // Validation Method
    public boolean isValid() {
        boolean valid = results != null && !results.isEmpty();
        logDebug("Validation check - isValid: " + valid);
        return valid;
    }

    // Logging utility
    private void logDebug(String message) {
        if (LOGGING_ENABLED) {
            System.out.println("DEBUG: " + message);
        }
    }
}
