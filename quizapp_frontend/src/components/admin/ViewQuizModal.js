import React, { useEffect, useState } from 'react';
import { Modal, Button, Spinner, Alert, Card } from 'react-bootstrap';
import { getQuizQuestions } from '../../services/api';

const ViewQuizModal = ({ show, onHide, quizId }) => {
    const [questions, setQuestions] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        if (quizId) {
            const fetchQuestions = async () => {
                setLoading(true);
                setError('');
                try {
                    const fetchedQuestions = await getQuizQuestions(quizId);
                    console.log('Fetched Questions:', fetchedQuestions); // Debugging
                    // Extract the `question` field from each fetched question
                    const formattedQuestions = fetchedQuestions.map((item) => item.question);
                    setQuestions(formattedQuestions);
                } catch (err) {
                    console.error('Error fetching questions:', err);
                    setError(err.message || 'Failed to load questions');
                } finally {
                    setLoading(false);
                }
            };

            fetchQuestions();
        }
    }, [quizId]);

    return (
        <Modal show={show} onHide={onHide} size="lg" centered>
            <Modal.Header closeButton>
                <Modal.Title>Quiz Questions</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {loading ? (
                    <div className="text-center my-4">
                        <Spinner animation="border" variant="primary" />
                        <p className="text-muted mt-2">Loading questions...</p>
                    </div>
                ) : error ? (
                    <Alert variant="danger">{error}</Alert>
                ) : questions.length > 0 ? (
                    <div>
                        {questions.map((question, index) => (
                            <Card key={question.questionId} className="mb-4">
                                <Card.Body>
                                    <Card.Title>
                                        Question {index + 1}: {question.text}
                                    </Card.Title>
                                    <div className="mt-3">
                                        <ul>
                                            {[...question.incorrectAnswers, question.answer].map(
                                                (option, idx) => (
                                                    <li
                                                        key={idx}
                                                        className={
                                                            option === question.answer
                                                                ? 'text-success font-weight-bold'
                                                                : 'text-muted'
                                                        }
                                                    >
                                                        {option}
                                                    </li>
                                                )
                                            )}
                                        </ul>
                                    </div>
                                </Card.Body>
                            </Card>
                        ))}
                    </div>
                ) : (
                    <p className="text-muted">No questions found for this quiz.</p>
                )}
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>
                    Close
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ViewQuizModal;
