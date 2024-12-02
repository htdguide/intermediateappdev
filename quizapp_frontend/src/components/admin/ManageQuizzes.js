import React, { useState, useEffect } from 'react';
import { updateQuizById, deleteQuizById } from '../../services/adminApi';
import { getAllQuizzes } from '../../services/api';
import { Table, Button, Spinner, Alert, Container } from 'react-bootstrap';
import AddQuizModal from './AddQuizModal';
import ViewQuizModal from './ViewQuizModal';
import styles from './ManageQuizzes.module.css';

const ManageQuizzes = () => {
    const [quizzes, setQuizzes] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);
    const [editQuiz, setEditQuiz] = useState(null);
    const [viewQuizId, setViewQuizId] = useState(null);

    useEffect(() => {
        const fetchQuizzes = async () => {
            try {
                const data = await getAllQuizzes();
                setQuizzes(data);
            } catch (err) {
                setError(err.message || 'Failed to load quizzes');
            } finally {
                setLoading(false);
            }
        };

        fetchQuizzes();
    }, []);

    const handleQuizCreated = (newQuiz) => {
        setQuizzes((prev) => [...prev, newQuiz]);
    };

    const handleQuizUpdated = async (updatedQuiz) => {
        try {
            const result = await updateQuizById(updatedQuiz.quizId, updatedQuiz);
            setQuizzes((prev) =>
                prev.map((quiz) => (quiz.quizId === updatedQuiz.quizId ? result : quiz))
            );
            setEditQuiz(null);
        } catch (error) {
            setError(error.message || 'Failed to update quiz');
        }
    };

    const handleQuizDeleted = async (quizId) => {
        if (!window.confirm('Are you sure you want to delete this quiz?')) return;
        try {
            await deleteQuizById(quizId);
            setQuizzes((prev) => prev.filter((quiz) => quiz.quizId !== quizId));
        } catch (error) {
            setError(error.message || 'Failed to delete quiz');
        }
    };

    return (
        <Container fluid className={styles.manageQuizzesContainer}>
            <h2 className={`text-center mb-4 ${styles.title}`}>Manage Quizzes</h2>
            {error && <Alert variant="danger">{error}</Alert>}
            {loading ? (
                <div className={styles.loadingContainer}>
                    <Spinner animation="border" variant="primary" />
                    <p className="text-muted mt-2">Loading quizzes...</p>
                </div>
            ) : (
                <>
                    <div className={`mb-3 text-end ${styles.actionBar}`}>
                        <Button variant="primary" className={styles.addButton} onClick={() => setShowModal(true)}>
                            Add Quiz
                        </Button>
                    </div>
                    <Table bordered hover responsive className={styles.quizTable}>
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Title</th>
                                <th>Start Date</th>
                                <th>End Date</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {quizzes.length > 0 ? (
                                quizzes.map((quiz, index) => (
                                    <tr key={quiz.quizId}>
                                        <td>{index + 1}</td>
                                        <td>{quiz.title}</td>
                                        <td>{quiz.startDate || 'N/A'}</td>
                                        <td>{quiz.endDate || 'N/A'}</td>
                                        <td className={styles.actionButtons}>
                                            <Button
                                                variant="info"
                                                size="sm"
                                                className={styles.actionButton}
                                                onClick={() => setEditQuiz(quiz)}
                                            >
                                                Edit
                                            </Button>
                                            <Button
                                                variant="danger"
                                                size="sm"
                                                className={styles.actionButton}
                                                onClick={() => handleQuizDeleted(quiz.quizId)}
                                            >
                                                Delete
                                            </Button>
                                            <Button
                                                variant="primary"
                                                size="sm"
                                                className={styles.actionButton}
                                                onClick={() => setViewQuizId(quiz.quizId)}
                                            >
                                                View Quiz
                                            </Button>
                                        </td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan="5" className="text-center">
                                        No quizzes available at the moment.
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </Table>
                </>
            )}
            <AddQuizModal
                show={showModal}
                onHide={() => setShowModal(false)}
                onQuizCreated={handleQuizCreated}
            />
            {editQuiz && (
                <AddQuizModal
                    show={!!editQuiz}
                    onHide={() => setEditQuiz(null)}
                    quiz={editQuiz}
                    onQuizCreated={handleQuizUpdated}
                />
            )}
            {viewQuizId && (
                <ViewQuizModal
                    show={!!viewQuizId}
                    onHide={() => setViewQuizId(null)}
                    quizId={viewQuizId}
                />
            )}
        </Container>
    );
};

export default ManageQuizzes;
