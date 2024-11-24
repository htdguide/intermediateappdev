import React, { useState, useEffect } from 'react';
import { getAllQuizzes } from '../../services/api'; // Ensure admin API is used for fetching quizzes
import { Table, Button, Spinner, Alert, Container } from 'react-bootstrap';

const ManageQuizzes = () => {
    const [quizzes, setQuizzes] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchQuizzes = async () => {
            try {
                console.log('Fetching quizzes...');
                const data = await getAllQuizzes();
                console.log('Quizzes fetched:', data);
                setQuizzes(data);
            } catch (err) {
                console.error('Error fetching quizzes:', err);
                setError(err.message || 'Failed to load quizzes');
            } finally {
                setLoading(false);
            }
        };

        fetchQuizzes();
    }, []);

    return (
        <Container className="mt-4">
            <h2 className="text-center mb-4">Manage Quizzes</h2>
            {error && <Alert variant="danger">{error}</Alert>}
            {loading ? (
                <div className="text-center my-4">
                    <Spinner animation="border" variant="primary" />
                    <p className="text-muted mt-2">Loading quizzes...</p>
                </div>
            ) : (
                <>
                    <div className="mb-3 text-end">
                        <Button
                            variant="primary"
                            className="me-2"
                            onClick={() => console.log('Create Quiz clicked')}
                        >
                            Add Quiz
                        </Button>
                    </div>
                    <Table bordered hover responsive>
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Title</th>
                                <th>Description</th>
                                <th>Start Date</th>
                                <th>End Date</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {quizzes.length > 0 ? (
                                quizzes.map((quiz, index) => (
                                    <tr key={quiz.quizId || quiz.id}>
                                        <td>{index + 1}</td>
                                        <td>{quiz.title}</td>
                                        <td>{quiz.description || 'No description'}</td>
                                        <td>{quiz.startDate || 'N/A'}</td>
                                        <td>{quiz.endDate || 'N/A'}</td>
                                        <td>
                                            <Button
                                                variant="info"
                                                size="sm"
                                                className="me-2"
                                                onClick={() => console.log(`Edit Quiz: ${quiz.quizId}`)}
                                            >
                                                Edit
                                            </Button>
                                            <Button
                                                variant="danger"
                                                size="sm"
                                                onClick={() => console.log(`Delete Quiz: ${quiz.quizId}`)}
                                            >
                                                Delete
                                            </Button>
                                        </td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan="6" className="text-center">
                                        No quizzes available at the moment.
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </Table>
                </>
            )}
        </Container>
    );
};

export default ManageQuizzes;
