import React, { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import { Container, Row, Col, Card, Form, Button, Alert } from 'react-bootstrap';
import { getQuizQuestions, validateAndSaveQuizSubmission } from '../services/api';

// Utility function to shuffle an array
const shuffleArray = (array) => {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
};

function PlayQuizPage() {
    const { quizId } = useParams(); // Get quizId from URL parameters
    const user = JSON.parse(localStorage.getItem('user')); // Logged-in user data
    const [questions, setQuestions] = useState([]);
    const [userAnswers, setUserAnswers] = useState({});
    const [score, setScore] = useState(null);
    const [feedback, setFeedback] = useState([]); // Store feedback for incorrect answers
    const [loading, setLoading] = useState(true); // Loading state
    const [error, setError] = useState(''); // For displaying error messages
    const [quizSubmitted, setQuizSubmitted] = useState(false); // Track if the quiz has been submitted

    // Fetch quiz questions
    const fetchQuizQuestions = useCallback(async () => {
        try {
            console.log(`Fetching questions for quiz ID: ${quizId}`);
            const fetchedQuestions = await getQuizQuestions(quizId);

            if (!fetchedQuestions || fetchedQuestions.length === 0) {
                throw new Error('No questions found for this quiz.');
            }

            // Map questions and shuffle options
            const formattedQuestions = fetchedQuestions.map((item) => {
                const shuffledOptions = shuffleArray(item.options); // Shuffle options array
                return {
                    ...item,
                    shuffledOptions, // Add shuffled options to the question object
                };
            });

            setQuestions(formattedQuestions); // Set formatted questions to state
            console.log('Fetched and formatted quiz questions:', formattedQuestions); // Log formatted questions
        } catch (error) {
            console.error('Error fetching quiz questions:', error);
            setError('Failed to load quiz questions.');
        }
    }, [quizId]); // Dependency on quizId ensures it updates if quizId changes

    useEffect(() => {
        const initializePage = async () => {
            setLoading(true);
            await fetchQuizQuestions();
            setLoading(false);
        };

        initializePage();
    }, [fetchQuizQuestions]);

    const handleOptionChange = (questionId, selectedOption) => {
        console.log(`Selected option for question ID ${questionId}: ${selectedOption}`);
        setUserAnswers((prevAnswers) => ({
            ...prevAnswers,
            [questionId]: selectedOption,
        }));
    };

    const handleSubmitQuiz = async () => {
        console.log('Submitting quiz...');

        // Check if all questions are answered
        const unansweredQuestions = questions.filter(
            (question) => !userAnswers[question.questionId]
        );

        if (unansweredQuestions.length > 0) {
            alert('Please answer all the questions before submitting!');
            return;
        }

        // Prepare submission payload
        const quizSubmission = {
            quizId: Number(quizId),
            userId: user.userId,
            answers: userAnswers, // This is the map of questionId -> selectedOption
        };

        try {
            console.log('Validating and saving quiz submission...');
            const validationResult = await validateAndSaveQuizSubmission(quizSubmission);

            // Update score and feedback
            console.log('Validation Result:', validationResult);
            setScore(validationResult.score);
            setFeedback(validationResult.feedback); // Set feedback for questions
            setQuizSubmitted(true); // Mark quiz as submitted
        } catch (error) {
            console.error('Error submitting quiz:', error);
            setError('Error submitting your quiz. Please try again later.');
        }
    };

    if (loading) {
        return <p>Loading quiz...</p>;
    }

    if (error) {
        return <Alert variant="danger" className="mt-4">{error}</Alert>;
    }

    return (
        <Container className="mt-5">
            {score !== null && (
                <Row className="justify-content-center mb-4">
                    <Col md={8} className="text-center">
                        <h2>Your Score: {score} / {questions.length}</h2>
                    </Col>
                </Row>
            )}
            <Row className="justify-content-center">
                <Col md={8}>
                    <h3 className="text-center mb-4">Quiz Questions</h3>
                    <Form>
                        {questions.map((question, index) => {
                            const feedbackItem = feedback.find(
                                (item) => item.questionId === question.questionId
                            );
                            return (
                                <Card key={question.questionId} className="mb-4">
                                    <Card.Body>
                                        <Card.Title>
                                            Question {index + 1}: {question.text}
                                        </Card.Title>
                                        <div className="mt-3">
                                            {question.shuffledOptions.map((option, idx) => {
                                                const isSelected = userAnswers[question.questionId] === option;
                                                const isCorrect = feedbackItem?.correctAnswer === option;
                                                const isWrongSelected =
                                                    isSelected && !isCorrect && quizSubmitted;

                                                return (
                                                    <Form.Check
                                                        type="radio"
                                                        key={idx}
                                                        id={`q${question.questionId}-option${idx}`}
                                                        name={`question-${question.questionId}`}
                                                        label={option}
                                                        value={option}
                                                        onChange={() => handleOptionChange(question.questionId, option)}
                                                        checked={isSelected}
                                                        className={`${
                                                            isWrongSelected
                                                                ? 'text-danger'
                                                                : isCorrect && quizSubmitted
                                                                ? 'text-success'
                                                                : ''
                                                        }`}
                                                    />
                                                );
                                            })}
                                        </div>
                                        {quizSubmitted && feedbackItem && !feedbackItem.correct && (
                                            <div className="mt-3 text-success">
                                                <strong>Correct Answer:</strong> {feedbackItem.correctAnswer}
                                            </div>
                                        )}
                                    </Card.Body>
                                </Card>
                            );
                        })}
                        {!quizSubmitted && (
                            <div className="text-center mt-4">
                                <Button variant="success" onClick={handleSubmitQuiz}>
                                    Submit Quiz
                                </Button>
                            </div>
                        )}
                    </Form>
                </Col>
            </Row>
        </Container>
    );
}

export default PlayQuizPage;