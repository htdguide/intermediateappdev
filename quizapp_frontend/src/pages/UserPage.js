import React, { useState, useEffect } from 'react';
import { getAllQuizzes } from '../services/api';
import { Container, Row, Col, Card, Button, Alert, Spinner } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const UserPage = () => {
    const user = JSON.parse(localStorage.getItem('user'));
    const [quizzes, setQuizzes] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchQuizzes = async () => {
            try {
                console.log('Fetching quizzes...');
                const data = await getAllQuizzes();

                // Categorize quizzes by status
                const categorizedQuizzes = [
                    ...data.filter((quiz) => quiz.status === 'Active'),
                    ...data.filter((quiz) => quiz.status === 'Upcoming'),
                    ...data.filter((quiz) => quiz.status === 'Expired'),
                ];

                console.log('Quizzes fetched and categorized:', categorizedQuizzes);
                setQuizzes(categorizedQuizzes);
                setLoading(false);
            } catch (err) {
                console.error('Error fetching quizzes:', err);
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

    if (!user) {
        return (
            <Container className="text-center mt-5">
                <Alert variant="danger">You must be logged in to view this page.</Alert>
            </Container>
        );
    }

    return (
        <Container className="mt-4">
            <Row className="mb-4">
                <Col>
                    <h2 className="text-center">Welcome, {user.firstName}!</h2>
                    <p className="text-center">Email: {user.email}</p>
                    <p className="text-center">User Type: {user.usertype}</p>
                </Col>
            </Row>

            <Row>
                <Col>
                    <h3 className="text-center">Available Quizzes</h3>
                    {error && <Alert variant="danger" className="text-center">{error}</Alert>}
                    {loading && (
                        <div className="text-center my-4">
                            <Spinner animation="border" variant="primary" />
                            <p className="text-muted mt-2">Loading quizzes...</p>
                        </div>
                    )}
                </Col>
            </Row>

            {!loading && (
                <>
                    <h4 className="text-center text-success mt-4">Active Quizzes</h4>
                    {getFilteredQuizzes('Active').length > 0 ? (
                        <Row className="gy-4">
                            {getFilteredQuizzes('Active').map((quiz) => (
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
                                            <Button
                                                variant="primary"
                                                className="w-100"
                                                onClick={() => playQuiz(quiz.quizId)}
                                            >
                                                Play Quiz
                                            </Button>
                                        </Card.Body>
                                    </Card>
                                </Col>
                            ))}
                        </Row>
                    ) : (
                        <p className="text-muted text-center">No active quizzes available at the moment.</p>
                    )}

                    <h4 className="text-center text-warning mt-4">Upcoming Quizzes</h4>
                    {getFilteredQuizzes('Upcoming').length > 0 ? (
                        <Row className="gy-4">
                            {getFilteredQuizzes('Upcoming').map((quiz) => (
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
                                            <Button variant="secondary" className="w-100" disabled>
                                                Not Playable Yet
                                            </Button>
                                        </Card.Body>
                                    </Card>
                                </Col>
                            ))}
                        </Row>
                    ) : (
                        <p className="text-muted text-center">No upcoming quizzes at the moment.</p>
                    )}

                    <h4 className="text-center text-danger mt-4">Expired Quizzes</h4>
                    {getFilteredQuizzes('Expired').length > 0 ? (
                        <Row className="gy-4">
                            {getFilteredQuizzes('Expired').map((quiz) => (
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
                                            <Button variant="secondary" className="w-100" disabled>
                                                Quiz Expired
                                            </Button>
                                        </Card.Body>
                                    </Card>
                                </Col>
                            ))}
                        </Row>
                    ) : (
                        <p className="text-muted text-center">No expired quizzes available at the moment.</p>
                    )}
                </>
            )}

            {!loading && !quizzes.length && !error && (
                <div className="text-center my-4">
                    <p className="text-muted">No quizzes available at the moment.</p>
                </div>
            )}
        </Container>
    );
};

export default UserPage;
