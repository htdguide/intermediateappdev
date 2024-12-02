import React, { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import { Container, Row, Col, Card, Form, Button, Alert, Spinner } from 'react-bootstrap';
import { getQuizQuestions, validateAndSaveQuizSubmission } from '../services/api';
import styles from '../styles/PlayQuizPage.module.css';

// Utility function to shuffle an array
const shuffleArray = (array) => {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
};

function PlayQuizPage() {
    const { quizId } = useParams();
    const user = JSON.parse(localStorage.getItem('user'));
    const [questions, setQuestions] = useState([]);
    const [userAnswers, setUserAnswers] = useState({});
    const [score, setScore] = useState(null);
    const [feedback, setFeedback] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [quizSubmitted, setQuizSubmitted] = useState(false);

    const fetchQuizQuestions = useCallback(async () => {
        try {
            const fetchedQuestions = await getQuizQuestions(quizId);
            if (!fetchedQuestions || fetchedQuestions.length === 0) {
                throw new Error('No questions found for this quiz.');
            }
            const formattedQuestions = fetchedQuestions.map((item) => {
                const shuffledOptions = shuffleArray(item.options);
                return {
                    ...item,
                    shuffledOptions,
                };
            });
            setQuestions(formattedQuestions);
        } catch (error) {
            setError('Failed to load quiz questions.');
        }
    }, [quizId]);

    useEffect(() => {
        const initializePage = async () => {
            setLoading(true);
            await fetchQuizQuestions();
            setLoading(false);
        };

        initializePage();
    }, [fetchQuizQuestions]);

    const handleOptionChange = (questionId, selectedOption) => {
        setUserAnswers((prevAnswers) => ({
            ...prevAnswers,
            [questionId]: selectedOption,
        }));
    };

    const handleSubmitQuiz = async () => {
        const unansweredQuestions = questions.filter(
            (question) => !userAnswers[question.questionId]
        );

        if (unansweredQuestions.length > 0) {
            alert('Please answer all the questions before submitting!');
            return;
        }

        const quizSubmission = {
            quizId: Number(quizId),
            userId: user.userId,
            answers: userAnswers,
        };

        try {
            const validationResult = await validateAndSaveQuizSubmission(quizSubmission);
            setScore(validationResult.score);
            setFeedback(validationResult.feedback);
            setQuizSubmitted(true);
        } catch (error) {
            setError('Error submitting your quiz. Please try again later.');
        }
    };

    if (loading) {
        return (
            <div className={styles.loaderContainer}>
                <Spinner animation="border" variant="primary" />
                <p className="mt-3">Loading quiz...</p>
            </div>
        );
    }

    if (error) {
        return <Alert variant="danger" className={`mt-4 ${styles.errorAlert}`}>{error}</Alert>;
    }

    return (
        <Container className={`mt-5 ${styles.quizContainer}`}>
            {score !== null && (
                <Row className="justify-content-center mb-4">
                    <Col md={8} className={`text-center ${styles.scoreSection}`}>
                        <h2>Your Score: {score} / {questions.length}</h2>
                    </Col>
                </Row>
            )}
            <Row className="justify-content-center">
                <Col md={8}>
                    <h3 className={`text-center mb-4 ${styles.quizTitle}`}>Quiz Questions</h3>
                    <Form>
                        {questions.map((question, index) => {
                            const feedbackItem = feedback.find(
                                (item) => item.questionId === question.questionId
                            );
                            return (
                                <Card key={question.questionId} className={`mb-4 ${styles.questionCard}`}>
                                    <Card.Body>
                                        <Card.Title>
                                            Question {index + 1}: {question.text}
                                        </Card.Title>
                                        <div className={`mt-3 ${styles.optionsContainer}`}>
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
                                                                ? styles.wrongOption
                                                                : isCorrect && quizSubmitted
                                                                ? styles.correctOption
                                                                : ''
                                                        }`}
                                                    />
                                                );
                                            })}
                                        </div>
                                        {quizSubmitted && feedbackItem && !feedbackItem.correct && (
                                            <div className={`mt-3 ${styles.correctAnswer}`}>
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
