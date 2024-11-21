package com.quizapp.quizApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ApiResponse {

    @JsonProperty("response_code")  // Map "response_code" from JSON to this field
    private int responseCode;

    private List<ApiQuestion> results;

    // Getters and Setters
    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public List<ApiQuestion> getResults() {
        return results;
    }

    public void setResults(List<ApiQuestion> results) {
        this.results = results;
    }
}
