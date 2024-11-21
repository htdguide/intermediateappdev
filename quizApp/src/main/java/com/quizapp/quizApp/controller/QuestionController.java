package com.quizapp.quizApp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quizapp.quizApp.dto.ApiResponse;
import com.quizapp.quizApp.model.Difficulty;
import com.quizapp.quizApp.model.Question;
import com.quizapp.quizApp.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // Get all questions
    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    // Get a question by ID
    @GetMapping("/{id}")
    public Optional<Question> getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }

    // Get questions by difficulty
    @GetMapping("/difficulty/{difficulty}")
    public List<Question> getQuestionsByDifficulty(@PathVariable String difficulty) {
        return questionService.getQuestionsByDifficulty(difficulty);
    }

    // Get questions by category
    @GetMapping("/category/{category}")
    public List<Question> getQuestionsByCategory(@PathVariable String category) {
        return questionService.getQuestionsByCategory(category);
    }

    // Search questions by keyword
    @GetMapping("/search")
    public List<Question> searchQuestionsByKeyword(@RequestParam String keyword) {
        return questionService.searchQuestionsByKeyword(keyword);
    }

    // Get a question by ID, difficulty, and category
    @GetMapping("/{id}/details")
    public Optional<Question> getQuestionByIdDifficultyAndCategory(
            @PathVariable Long id,
            @RequestParam String difficulty,
            @RequestParam String category) {
        return questionService.getQuestionByIdDifficultyAndCategory(id, difficulty, category);
    }

    // Create a new question
    @PostMapping
    public Question createQuestion(@RequestBody Question question) {
        return questionService.saveQuestion(question);
    }

    // Create multiple questions
    @PostMapping("/batch")
    public List<Question> createQuestions(@RequestBody List<Question> questions) {
        return questionService.saveQuestions(questions);
    }

    @PostMapping("/fetch")
    public List<Question> fetchQuestionsFromApi(@RequestParam String apiUrl) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Fetch raw response once
            String rawResponse = restTemplate.getForObject(apiUrl, String.class);
            System.out.println("Raw API Response: " + rawResponse);

            // Parse the raw JSON response into ApiResponse
            ApiResponse apiResponse = objectMapper.readValue(rawResponse, ApiResponse.class);

            if (apiResponse == null || apiResponse.getResults() == null || apiResponse.getResults().isEmpty()) {
                throw new RuntimeException("API response is empty or null");
            }

            // Map the API data to Question objects
            List<Question> questions = apiResponse.getResults().stream().map(apiQuestion -> {
                Question question = new Question();
                question.setDifficulty(
                        Arrays.stream(Difficulty.values())
                                .filter(d -> d.name().equalsIgnoreCase(apiQuestion.getDifficulty()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Invalid difficulty: " + apiQuestion.getDifficulty()))
                );
                question.setCategory(apiQuestion.getCategory());
                question.setText(apiQuestion.getQuestion());
                question.setAnswer(apiQuestion.getCorrectAnswer());
                question.setIncorrectAnswers(apiQuestion.getIncorrectAnswers());
                return question;
            }).toList();

            // Validate questions
            questions.forEach(question -> {
                if (question.getAnswer() == null || question.getText() == null || question.getCategory() == null) {
                    throw new IllegalArgumentException("Required fields missing in question: " + question);
                }
            });

            // Save the questions to the database
            return questionService.saveQuestions(questions);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                throw new RuntimeException("API rate limit exceeded. Please try again later.", e);
            } else {
                throw new RuntimeException("API returned an error: " + e.getStatusCode(), e);
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch data from the API: " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse API response: " + e.getMessage(), e);
        }
    }

    // Update an existing question by ID
    @PutMapping("/{id}")
    public Optional<Question> updateQuestion(
            @PathVariable Long id,
            @RequestBody Question updatedQuestion) {
        return questionService.getQuestionById(id).map(existingQuestion -> {
            // Update only non-null fields
            if (updatedQuestion.getDifficulty() != null) {
                existingQuestion.setDifficulty(updatedQuestion.getDifficulty());
            }
            if (updatedQuestion.getCategory() != null) {
                existingQuestion.setCategory(updatedQuestion.getCategory());
            }
            if (updatedQuestion.getText() != null) {
                existingQuestion.setText(updatedQuestion.getText());
            }
            if (updatedQuestion.getAnswer() != null) {
                existingQuestion.setAnswer(updatedQuestion.getAnswer());
            }
            if (updatedQuestion.getIncorrectAnswers() != null && !updatedQuestion.getIncorrectAnswers().isEmpty()) {
                existingQuestion.setIncorrectAnswers(updatedQuestion.getIncorrectAnswers());
            }

            // Save the updated question
            return questionService.saveQuestion(existingQuestion);
        });
    }


    // Delete a question by ID
    @DeleteMapping("/{id}")
    public void deleteQuestionById(@PathVariable Long id) {
        questionService.deleteQuestionById(id);
    }
}