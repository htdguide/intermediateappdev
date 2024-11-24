package com.quizapp.quizApp.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.quizapp.quizApp.model.QuizQuestion;

import java.io.IOException;

public class QuizQuestionSerializer extends StdSerializer<QuizQuestion> {

    public QuizQuestionSerializer() {
        this(null);
    }

    public QuizQuestionSerializer(Class<QuizQuestion> t) {
        super(t);
    }

    @Override
    public void serialize(QuizQuestion quizQuestion, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("quizQuestionId", quizQuestion.getQuizQuestionId());

        gen.writeObjectFieldStart("quiz");
        gen.writeNumberField("quizId", quizQuestion.getQuiz().getQuizId());
        gen.writeStringField("title", quizQuestion.getQuiz().getTitle());
        gen.writeEndObject();

        gen.writeObjectFieldStart("question");
        gen.writeNumberField("questionId", quizQuestion.getQuestion().getQuestionId());
        gen.writeStringField("text", quizQuestion.getQuestion().getText());
        gen.writeStringField("category", quizQuestion.getQuestion().getCategory());
        gen.writeStringField("difficulty", quizQuestion.getQuestion().getDifficulty().toString());
        gen.writeArrayFieldStart("incorrectAnswers");
        for (String incorrectAnswer : quizQuestion.getQuestion().getIncorrectAnswers()) {
            gen.writeString(incorrectAnswer);
        }
        gen.writeEndArray();
        gen.writeStringField("answer", quizQuestion.getQuestion().getAnswer());
        gen.writeEndObject();

        gen.writeEndObject();
    }
}
