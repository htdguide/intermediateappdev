import React, { useState, useEffect } from 'react';
import { Modal, Button, Form, Alert } from 'react-bootstrap';
import { createQuiz, updateQuizById } from '../../services/adminApi'; // Import the update method

const AddQuizModal = ({ show, onHide, onQuizCreated, quiz }) => {
    const [title, setTitle] = useState('');
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [category, setCategory] = useState('');
    const [difficulty, setDifficulty] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    // Populate fields if editing a quiz
    useEffect(() => {
        if (quiz) {
            setTitle(quiz.title || '');
            setStartDate(quiz.startDate || '');
            setEndDate(quiz.endDate || '');
            setCategory(''); // Reset category since it's not needed for editing
            setDifficulty(''); // Reset difficulty since it's not needed for editing
        } else {
            // Reset fields if not editing
            setTitle('');
            setStartDate('');
            setEndDate('');
            setCategory('');
            setDifficulty('');
        }
    }, [quiz]);

    const handleSubmit = async () => {
        setLoading(true);
        setError('');
        try {
            if (quiz && quiz.quizId) { // Check for quiz.quizId
                const updatedQuiz = { title, startDate, endDate };
                const result = await updateQuizById(quiz.quizId, updatedQuiz); // Pass quiz.quizId
                onQuizCreated(result); // Notify parent of the updated quiz
            } else {
                const newQuiz = await createQuiz({ title, startDate, endDate, category, difficulty });
                onQuizCreated(newQuiz); // Notify parent of the new quiz
            }
            onHide(); // Close the modal
        } catch (err) {
            setError(err.message || 'Failed to save quiz');
        } finally {
            setLoading(false);
        }
    };
    
    
    return (
        <Modal show={show} onHide={onHide} centered>
            <Modal.Header closeButton>
                <Modal.Title>{quiz ? 'Edit Quiz' : 'Add Quiz'}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {error && <Alert variant="danger">{error}</Alert>}
                <Form>
                    <Form.Group className="mb-3" controlId="formQuizTitle">
                        <Form.Label>Title</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Enter quiz title"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                        />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="formStartDate">
                        <Form.Label>Start Date</Form.Label>
                        <Form.Control
                            type="date"
                            value={startDate}
                            onChange={(e) => setStartDate(e.target.value)}
                        />
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="formEndDate">
                        <Form.Label>End Date</Form.Label>
                        <Form.Control
                            type="date"
                            value={endDate}
                            onChange={(e) => setEndDate(e.target.value)}
                        />
                    </Form.Group>
                    {!quiz && (
                        <>
                            <Form.Group className="mb-3" controlId="formCategory">
                                <Form.Label>Category</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Enter quiz category"
                                    value={category}
                                    onChange={(e) => setCategory(e.target.value)}
                                />
                            </Form.Group>
                            <Form.Group className="mb-3" controlId="formDifficulty">
                                <Form.Label>Difficulty</Form.Label>
                                <Form.Control
                                    as="select"
                                    value={difficulty}
                                    onChange={(e) => setDifficulty(e.target.value.toLowerCase())}
                                >
                                    <option value="">Select Difficulty</option>
                                    <option value="easy">Easy</option>
                                    <option value="medium">Medium</option>
                                    <option value="hard">Hard</option>
                                </Form.Control>
                            </Form.Group>
                        </>
                    )}
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>
                    Cancel
                </Button>
                <Button variant="primary" onClick={handleSubmit} disabled={loading}>
                    {loading ? (quiz ? 'Updating...' : 'Creating...') : (quiz ? 'Update Quiz' : 'Add Quiz')}
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default AddQuizModal;
