import React, { useState, useEffect } from 'react';
import { getAllQuizzes } from '../services/api';
import { Container, Row, Col, Card, Button, Alert, Spinner } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const PlayQuiz = () => {
    const [quizzes, setQuizzes] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchQuizzes = async () => {
            try {
                const data = await getAllQuizzes();
                setQuizzes(data);
                setLoading(false);
            } catch (err) {
                setError(err.message || 'Failed to load quizzes');
                setLoading(false);
            }
        };

        fetchQuizzes();
    }, []);

    const playQuiz = (quizId) => {
        navigate(`/play-quiz/${quizId}`);
    };

    const getFilteredQuizzes = (status) => quizzes.filter((quiz) => quiz.status === status);

    return (
        <Container>
            <h3 className="text-center">Available Quizzes</h3>
            {error && <Alert variant="danger" className="text-center">{error}</Alert>}
            {loading && (
                <div className="text-center my-4">
                    <Spinner animation="border" variant="primary" />
                    <p className="text-muted mt-2">Loading quizzes...</p>
                </div>
            )}
            {!loading && (
                <>
                    {['Active', 'Upcoming', 'Expired'].map((status) => (
                        <div key={status}>
                            <h4 className={`text-center text-${status === 'Active' ? 'success' : status === 'Upcoming' ? 'warning' : 'danger'}`}>
                                {status} Quizzes
                            </h4>
                            {getFilteredQuizzes(status).length > 0 ? (
                                <Row className="gy-4">
                                    {getFilteredQuizzes(status).map((quiz) => (
                                        <Col xs={12} md={6} lg={4} key={quiz.quizId}>
                                            <Card className="h-100 shadow-sm">
                                                <Card.Body>
                                                    <Card.Title>{quiz.title}</Card.Title>
                                                    <Card.Text>{quiz.description || 'No description available'}</Card.Text>
                                                    <Card.Text>
                                                        <strong>Start Date:</strong> {quiz.startDate}
                                                    </Card.Text>
                                                    <Card.Text>
                                                        <strong>End Date:</strong> {quiz.endDate}
                                                    </Card.Text>
                                                    {status === 'Active' ? (
                                                        <Button
                                                            variant="primary"
                                                            className="w-100"
                                                            onClick={() => playQuiz(quiz.quizId)}
                                                        >
                                                            Play Quiz
                                                        </Button>
                                                    ) : (
                                                        <Button variant="secondary" className="w-100" disabled>
                                                            {status === 'Upcoming' ? 'Not Playable Yet' : 'Quiz Expired'}
                                                        </Button>
                                                    )}
                                                </Card.Body>
                                            </Card>
                                        </Col>
                                    ))}
                                </Row>
                            ) : (
                                <p className="text-muted text-center">No {status.toLowerCase()} quizzes available at the moment.</p>
                            )}
                        </div>
                    ))}
                </>
            )}
        </Container>
    );
};

export default PlayQuiz;
