import React, { useState, useEffect } from 'react';
import { getAllQuizzes } from '../services/api';
import { Container, Row, Col, Card, Button, Alert, Spinner } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom'; // Import useNavigate for navigation

const UserPage = () => {
    const user = JSON.parse(localStorage.getItem('user')); // Get logged-in user data
    const [quizzes, setQuizzes] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true); // Add loading state
    const navigate = useNavigate(); // Hook for navigation

    useEffect(() => {
        const fetchQuizzes = async () => {
            try {
                console.log('Fetching quizzes...');
                const data = await getAllQuizzes();
                console.log('Quizzes fetched:', data);
                setQuizzes(data);
                setLoading(false); // Set loading to false after data is fetched
            } catch (err) {
                console.error('Error fetching quizzes:', err);
                setError(err.message || 'Failed to load quizzes');
                setLoading(false); // Set loading to false even if there's an error
            }
        };

        fetchQuizzes();
    }, []);

    const playQuiz = (quizId) => {
        navigate(`/play-quiz/${quizId}`); // Redirect to PlayQuizPage with the quiz ID
    };

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

            <Row className="gy-4">
                {quizzes.map((quiz) => (
                    <Col xs={12} md={6} lg={4} key={quiz.id || quiz.quizId}>
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

            {!loading && !quizzes.length && !error && (
                <div className="text-center my-4">
                    <p className="text-muted">No quizzes available at the moment.</p>
                </div>
            )}
        </Container>
    );
};

export default UserPage;
