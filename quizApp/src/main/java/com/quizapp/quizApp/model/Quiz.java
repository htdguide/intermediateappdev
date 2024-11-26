package com.quizapp.quizApp.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "quizId")
@Entity
@Table(name = "Quiz", indexes = {
        @Index(name = "idx_start_date", columnList = "start_date"),
        @Index(name = "idx_end_date", columnList = "end_date")
})
public class Quiz implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id") // Updated column name
    private Long quizId; // Updated field name

    @Column(name = "title", nullable = false)
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters.")
    private String title;

    @Column(name = "start_date", nullable = false)
    @FutureOrPresent(message = "Start date must be today or in the future.")
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    @Future(message = "End date must be in the future.")
    private LocalDate endDate;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Prevents recursion by ignoring quizQuestions during serialization
    private List<QuizQuestion> quizQuestions;

    @Transient // This annotation ensures this field is not persisted in the database
    @JsonProperty("status") // Expose this field in serialized JSON responses
    public String getStatus() {
        LocalDate today = LocalDate.now();

        if (startDate.isAfter(today)) {
            return "Upcoming"; // Quiz is not playable yet
        } else if (endDate.isBefore(today)) {
            return "Expired"; // Quiz is not playable anymore
        } else {
            return "Active"; // Quiz is playable
        }
    }

    public Quiz() {}

    public Quiz(String title, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<QuizQuestion> getQuizQuestions() {
        return quizQuestions;
    }

    public void setQuizQuestions(List<QuizQuestion> quizQuestions) {
        this.quizQuestions = quizQuestions;
    }

    // Utility Methods
    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return (today.isEqual(startDate) || today.isAfter(startDate)) && today.isBefore(endDate);
    }

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after the start date.");
        }
    }
}
