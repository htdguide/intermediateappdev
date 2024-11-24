import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Container, Row, Col, Card, Form, Button, Alert } from 'react-bootstrap';
import { getQuizQuestions, getUserRecordByUserIdAndQuizId, saveOrUpdateUserRecord } from '../services/api';

function PlayQuizPage() {
    const { quizId } = useParams(); // Get quizId from URL parameters
    const user = JSON.parse(localStorage.getItem('user')); // Logged-in user data
    const [questions, setQuestions] = useState([]);
    const [userAnswers, setUserAnswers] = useState({});
    const [score, setScore] = useState(null);
    const [existingRecord, setExistingRecord] = useState(null);
    const [loading, setLoading] = useState(true); // Loading state
    const [error, setError] = useState(''); // For displaying error messages

    // Fetch quiz questions
    const fetchQuizQuestions = async () => {
        try {
            console.log(`Fetching questions for quiz ID: ${quizId}`);
            const fetchedQuestions = await getQuizQuestions(quizId);

            if (!fetchedQuestions || fetchedQuestions.length === 0) {
                throw new Error('No questions found for this quiz.');
            }

            const formattedQuestions = fetchedQuestions.map((item) => {
                if (!item.question) {
                    throw new Error(`Invalid question data: ${JSON.stringify(item)}`);
                }
                return { ...item.question };
            });

            setQuestions(formattedQuestions);
        } catch (error) {
            console.error('Error fetching quiz questions:', error);
            setError('Failed to load quiz questions.');
        }
    };

    // Check user record
    const checkUserRecord = async () => {
        try {
            console.log(`Checking for existing record for user ID ${user.userId} and quiz ID ${quizId}`);
            const record = await getUserRecordByUserIdAndQuizId(user.userId, quizId);
            if (record) {
                console.log('Existing Record Found:', record);
                setExistingRecord(record);
            } else {
                console.log('No existing record found.');
                setExistingRecord(null);
            }
        } catch (error) {
            console.warn('No existing record found or error fetching record:', error);
            setExistingRecord(null);
        }
    };

    useEffect(() => {
        const initializePage = async () => {
            setLoading(true);
            await fetchQuizQuestions();
            await checkUserRecord();
            setLoading(false);
        };

        initializePage();
    }, [quizId, user.userId]);

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

        // Calculate score
        let calculatedScore = 0;
        questions.forEach((question) => {
            if (userAnswers[question.questionId] === question.answer) {
                calculatedScore += 1;
            }
        });
        console.log(`Final Score: ${calculatedScore}`);
        setScore(calculatedScore);

        try {
            console.log('Saving or updating quiz record...');
            const record = await saveOrUpdateUserRecord(user.userId, quizId, calculatedScore);
            console.log('Quiz Record Saved/Updated:', record);
            setExistingRecord(record);
        } catch (error) {
            console.error('Error submitting quiz:', error);
            setError('Error submitting your quiz record. Please try again later.');
        }
    };

    if (loading) {
        return <p>Loading quiz...</p>;
    }

    if (error) {
        return <Alert variant="danger" className="mt-4">{error}</Alert>;
    }

    if (score !== null) {
        return (
            <Container className="mt-5">
                <Row className="justify-content-center">
                    <Col md={8} className="text-center">
                        <h2>Your Score: {score} / {questions.length}</h2>
                        {existingRecord && (
                            <p className="text-muted">
                                Your previous score: {existingRecord.score} (played on {new Date(existingRecord.playedAt).toLocaleString()})
                            </p>
                        )}
                        <p className="text-success">Your latest score has been saved successfully!</p>
                        <Button variant="primary" onClick={() => window.location.reload()}>
                            Retake Quiz
                        </Button>
                    </Col>
                </Row>
            </Container>
        );
    }

    return (
        <Container className="mt-5">
            <Row className="justify-content-center">
                <Col md={8}>
                    <h3 className="text-center mb-4">Quiz Questions</h3>
                    <Form>
                        {questions.map((question, index) => (
                            <Card key={question.questionId} className="mb-4">
                                <Card.Body>
                                    <Card.Title>
                                        Question {index + 1}: {question.text}
                                    </Card.Title>
                                    <div className="mt-3">
                                        {[...question.incorrectAnswers, question.answer].map((option, idx) => (
                                            <Form.Check
                                                type="radio"
                                                key={idx}
                                                id={`q${question.questionId}-option${idx}`}
                                                name={`question-${question.questionId}`}
                                                label={option}
                                                value={option}
                                                onChange={() => handleOptionChange(question.questionId, option)}
                                                checked={userAnswers[question.questionId] === option}
                                            />
                                        ))}
                                    </div>
                                </Card.Body>
                            </Card>
                        ))}
                        <div className="text-center mt-4">
                            <Button variant="success" onClick={handleSubmitQuiz}>
                                Submit Quiz
                            </Button>
                        </div>
                    </Form>
                </Col>
            </Row>
        </Container>
    );
}

export default PlayQuizPage;
