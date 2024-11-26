package com.quizapp.quizApp.controller;

import com.quizapp.quizApp.dto.QuizSubmissionDTO;
import com.quizapp.quizApp.dto.QuizValidationResultDTO;
import com.quizapp.quizApp.model.QuizQuestion;
import com.quizapp.quizApp.services.QuizQuestionService;
import com.quizapp.quizApp.services.UserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quiz-validation")
public class QuizValidationController {

    @Autowired
    private QuizQuestionService quizQuestionService;

    @Autowired
    private UserRecordService userRecordService;

    @PostMapping("/validate-and-save")
    public ResponseEntity<QuizValidationResultDTO> validateAndSaveQuizSubmission(@RequestBody QuizSubmissionDTO submission) {
        try {
            Long quizId = submission.getQuizId();
            Long userId = submission.getUserId();
            Map<Long, String> answers = submission.getAnswers();

            // Fetch questions and correct answers
            List<QuizQuestion> quizQuestions = quizQuestionService.getQuestionsByQuizId(quizId);
            if (quizQuestions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Map<Long, String> correctAnswers = quizQuestions.stream()
                    .collect(Collectors.toMap(
                            qq -> qq.getQuestion().getQuestionId(),
                            qq -> qq.getQuestion().getAnswer()
                    ));

            // Validate answers
            int score = 0;
            List<Long> correctlyAnsweredQuestions = answers.entrySet().stream()
                    .filter(entry -> correctAnswers.containsKey(entry.getKey())
                            && correctAnswers.get(entry.getKey()).equals(entry.getValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            score = correctlyAnsweredQuestions.size();

            // Save to UserRecord
            userRecordService.createOrUpdateUserRecord(userId, quizId, score);

            // Prepare feedback
            List<QuizValidationResultDTO.QuestionFeedbackDTO> feedback = answers.entrySet().stream()
                    .map(entry -> {
                        Long questionId = entry.getKey();
                        String userAnswer = entry.getValue();
                        String correctAnswer = correctAnswers.get(questionId);

                        QuizValidationResultDTO.QuestionFeedbackDTO fb = new QuizValidationResultDTO.QuestionFeedbackDTO();
                        fb.setQuestionId(questionId);
                        fb.setUserAnswer(userAnswer);
                        fb.setCorrectAnswer(correctAnswer);
                        fb.setCorrect(correctAnswer.equals(userAnswer));

                        return fb;
                    })
                    .collect(Collectors.toList());

            // Prepare result
            QuizValidationResultDTO result = new QuizValidationResultDTO();
            result.setQuizId(quizId);
            result.setScore(score);
            result.setTotalQuestions(correctAnswers.size());
            result.setCorrectAnswers(correctlyAnsweredQuestions);
            result.setFeedback(feedback);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}