package com.quizapp.quizApp.services;

import com.quizapp.quizApp.model.Difficulty;
import com.quizapp.quizApp.model.Question;
import com.quizapp.quizApp.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.quizapp.quizApp.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private static final boolean LOGGING_ENABLED = true;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getAllQuestions() {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching all questions.");
            return questionRepository.findAll();
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching all questions: " + e.getMessage());
            return List.of();
        }
    }

    public Optional<Question> getQuestionById(Long id) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching question by ID: " + id);
            return questionRepository.findById(id);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching question by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Question> getQuestionsByDifficulty(Difficulty difficulty) {
        if (LOGGING_ENABLED) System.out.println("Fetching questions by difficulty: " + difficulty);
        return questionRepository.findByDifficulty(difficulty);
    }

    public List<Question> getQuestionsByCategory(String category, boolean randomize) {
        if (randomize) {
            // Fetch random questions by category
            return questionRepository.findByCategory(category);  // Uses the query with ORDER BY FUNCTION('RAND')
        } else {
            // Fetch all questions by category without randomization
            return questionRepository.findByCategory(category);  // Uses the same method since there's no pagination or filtering
        }
    }

    public List<Question> searchQuestionsByKeyword(String keyword) {
        if (LOGGING_ENABLED) System.out.println("Searching questions with keyword: " + keyword);
        return questionRepository.findByTextContainingIgnoreCase(keyword);
    }

    public Optional<Question> getQuestionByIdDifficultyAndCategory(Long id, String difficulty, String category) {
        try {
            if (LOGGING_ENABLED) System.out.println("Fetching question by ID, difficulty, and category.");
            return questionRepository.findByIdAndDifficultyAndCategory(id, difficulty, category);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error fetching question: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Question> saveQuestions(List<Question> questions) {
        try {
            if (LOGGING_ENABLED) System.out.println("Saving multiple questions.");
            return questionRepository.saveAll(questions);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error saving multiple questions: " + e.getMessage());
            return List.of();
        }
    }

    public Question saveQuestion(Question question) {
        try {
            if (LOGGING_ENABLED) System.out.println("Saving single question: " + question.getText());
            return questionRepository.save(question);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error saving single question: " + e.getMessage());
            return null;
        }
    }

    public Question updateQuestionById(Long id, Question updatedQuestion) {
        try {
            if (LOGGING_ENABLED) System.out.println("Service: Updating question with ID: " + id);
            return questionRepository.findById(id).map(existingQuestion -> {
                // Update all fields from updatedQuestion
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
                return questionRepository.save(existingQuestion);
            }).orElseThrow(() -> new IllegalArgumentException("Question with ID " + id + " not found."));
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error in service while updating question: " + e.getMessage());
            throw e;
        }
    }

    public void deleteQuestionById(Long id) {
        try {
            if (LOGGING_ENABLED) System.out.println("Deleting question by ID: " + id);
            questionRepository.deleteById(id);
        } catch (Exception e) {
            if (LOGGING_ENABLED) System.err.println("Error deleting question by ID: " + e.getMessage());
        }
    }

    // Fetch questions from an external API and save them to the database
    public List<Question> fetchQuestionsFromApi(String apiUrl) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            if (LOGGING_ENABLED) System.out.println("Fetching questions from external API: " + apiUrl);

            // Fetch raw response from the API
            String rawResponse = restTemplate.getForObject(apiUrl, String.class);
            if (LOGGING_ENABLED) System.out.println("Raw API Response: " + rawResponse);

            // Parse raw JSON response into ApiResponse DTO
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
            List<Question> savedQuestions = questionRepository.saveAll(questions);
            questionRepository.flush(); // Ensure the changes are flushed to the database
            if (LOGGING_ENABLED) System.out.println("Questions saved and flushed to database: " + savedQuestions.size());

            return savedQuestions;

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

}