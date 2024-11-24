import React, { useState, useEffect } from 'react';
import { Modal, Button, Form, Alert } from 'react-bootstrap';

const UserModal = ({ show, onHide, onSubmit, userData, mode }) => {
    const [formValues, setFormValues] = useState({
        firstName: '',
        lastName: '',
        email: '',
        usertype: 'PLAYER', // Default user type
        password: '', // For new user creation
    });
    const [error, setError] = useState('');

    useEffect(() => {
        if (mode === 'edit' && userData) {
            setFormValues((prevValues) => ({
                ...prevValues,
                firstName: userData.firstName || '',
                lastName: userData.lastName || '',
                email: userData.email || '',
                usertype: userData.usertype || 'PLAYER',
                password: '', // Blank for updates
            }));
        } else if (mode === 'create') {
            setFormValues({
                firstName: '',
                lastName: '',
                email: '',
                usertype: 'PLAYER',
                password: '', // Default to blank
            });
        }
    }, [userData, mode]);
    

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormValues((prevValues) => ({
            ...prevValues,
            [name]: value,
        }));
    };

    const handleSubmit = () => {
        if (!formValues.firstName || !formValues.lastName || !formValues.email || !formValues.usertype) {
            setError('All fields except password are required.');
            return;
        }
        if (mode === 'create' && !formValues.password) {
            setError('Password is required for new users.');
            return;
        }
        setError('');

        // Ensure `userId` is included for edit mode
        const dataToSubmit = {
            ...formValues,
            ...(mode === 'edit' && { userId: userData.userId }), // Use `userId` for updates
        };

        onSubmit(dataToSubmit); // Pass data to parent component
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>{mode === 'create' ? 'Create User' : 'Edit User'}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {error && <Alert variant="danger">{error}</Alert>}
                <Form>
                    <Form.Group className="mb-3">
                        <Form.Label>First Name</Form.Label>
                        <Form.Control
                            type="text"
                            name="firstName"
                            value={formValues.firstName}
                            onChange={handleChange}
                            placeholder="Enter first name"
                            required
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Last Name</Form.Label>
                        <Form.Control
                            type="text"
                            name="lastName"
                            value={formValues.lastName}
                            onChange={handleChange}
                            placeholder="Enter last name"
                            required
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Email</Form.Label>
                        <Form.Control
                            type="email"
                            name="email"
                            value={formValues.email}
                            onChange={handleChange}
                            placeholder="Enter email"
                            required
                            disabled={mode === 'edit'} // Disable email editing
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>User Type</Form.Label>
                        <Form.Select
                            name="usertype"
                            value={formValues.usertype}
                            onChange={handleChange}
                            required
                        >
                            <option value="PLAYER">PLAYER</option>
                            <option value="ADMIN">ADMIN</option>
                        </Form.Select>
                    </Form.Group>
                    {mode === 'create' && (
                        <Form.Group className="mb-3">
                            <Form.Label>Password</Form.Label>
                            <Form.Control
                                type="password"
                                name="password"
                                value={formValues.password}
                                onChange={handleChange}
                                placeholder="Enter password"
                                required
                            />
                        </Form.Group>
                    )}
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>
                    Close
                </Button>
                <Button variant="primary" onClick={handleSubmit}>
                    {mode === 'create' ? 'Create User' : 'Save Changes'}
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default UserModal;
