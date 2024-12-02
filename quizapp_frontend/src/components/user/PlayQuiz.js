import React, { useState, useEffect } from 'react';
import { getAllQuizzes, getUserRecordsByUserId } from '../../services/api';
import { Container, Row, Col, Card, Button, Alert, Spinner } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import styles from './PlayQuiz.module.css';

const PlayQuiz = () => {
    const [quizzes, setQuizzes] = useState([]);
    const [userRecords, setUserRecords] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const user = JSON.parse(localStorage.getItem('user'));
                if (!user) throw new Error('User not found');

                const [quizzesData, userRecordsData] = await Promise.all([
                    getAllQuizzes(),
                    getUserRecordsByUserId(user.userId),
                ]);

                setQuizzes(quizzesData);
                setUserRecords(userRecordsData);
                setLoading(false);
            } catch (err) {
                setError(err.message || 'Failed to load data');
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    const playQuiz = (quizId) => {
        navigate(`/play-quiz/${quizId}`);
    };

    const getFilteredQuizzes = (status) =>
        quizzes.filter((quiz) => quiz.status === status && !userRecords.some((record) => record.quiz.quizId === quiz.quizId));

    const getPlayedQuizzes = () =>
        quizzes.filter((quiz) =>
            userRecords.some((record) => record.quiz.quizId === quiz.quizId)
        );

    return (
        <Container className={styles.quizContainer}>
            <h3 className={`text-center ${styles.pageTitle}`}>Available Quizzes</h3>
            {error && <Alert variant="danger" className={`text-center ${styles.alert}`}>{error}</Alert>}
            {loading && (
                <div className={`text-center ${styles.spinnerContainer}`}>
                    <Spinner animation="border" variant="primary" />
                    <p className={`text-muted mt-2 ${styles.loadingMessage}`}>Loading quizzes...</p>
                </div>
            )}
            {!loading && (
                <>
                    <section>
                        <h4 className={`text-center text-info ${styles.sectionTitle}`}>Played Quizzes</h4>
                        {getPlayedQuizzes().length > 0 ? (
                            <Row className="gy-4">
                                {getPlayedQuizzes().map((quiz) => {
                                    const record = userRecords.find((record) => record.quiz.quizId === quiz.quizId);
                                    return (
                                        <Col xs={12} md={6} lg={4} key={quiz.quizId}>
                                            <Card className={`h-100 shadow-sm ${styles.quizCard}`}>
                                                <Card.Body>
                                                    <Card.Title>{quiz.title}</Card.Title>
                                                    <Card.Text>{quiz.description || 'No description available'}</Card.Text>
                                                    <Card.Text>
                                                        <strong>Start Date:</strong> {quiz.startDate}
                                                    </Card.Text>
                                                    <Card.Text>
                                                        <strong>End Date:</strong> {quiz.endDate}
                                                    </Card.Text>
                                                    <Card.Text>
                                                        <strong>Your Score:</strong> {record.score}
                                                    </Card.Text>
                                                    <Button variant="secondary" className="w-100" disabled>
                                                        Already Played
                                                    </Button>
                                                </Card.Body>
                                            </Card>
                                        </Col>
                                    );
                                })}
                            </Row>
                        ) : (
                            <p className={`text-muted text-center ${styles.noQuizzesMessage}`}>
                                No played quizzes available at the moment.
                            </p>
                        )}
                    </section>

                    {['Active', 'Upcoming', 'Expired'].map((status) => (
                        <section key={status}>
                            <h4
                                className={`text-center ${
                                    status === 'Active'
                                        ? 'text-success'
                                        : status === 'Upcoming'
                                        ? 'text-warning'
                                        : 'text-danger'
                                } ${styles.sectionTitle}`}
                            >
                                {status} Quizzes
                            </h4>
                            {getFilteredQuizzes(status).length > 0 ? (
                                <Row className="gy-4">
                                    {getFilteredQuizzes(status).map((quiz) => (
                                        <Col xs={12} md={6} lg={4} key={quiz.quizId}>
                                            <Card className={`h-100 shadow-sm ${styles.quizCard}`}>
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
                                <p className={`text-muted text-center ${styles.noQuizzesMessage}`}>
                                    No {status.toLowerCase()} quizzes available at the moment.
                                </p>
                            )}
                        </section>
                    ))}
                </>
            )}
        </Container>
    );
};

export default PlayQuiz;
