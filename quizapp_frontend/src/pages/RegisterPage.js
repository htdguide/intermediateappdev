import React, { useState } from 'react';
import { Form, Button, Container, Row, Col, Alert } from 'react-bootstrap';
import { createUser } from '../services/api';
import { useNavigate } from 'react-router-dom';
import styles from '../styles/RegisterPage.module.css';

const RegisterPage = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    confirmPassword: '',
  });

  const [errors, setErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const { firstName, lastName, email, password, confirmPassword } = formData;

    let formErrors = {};

    if (!firstName) formErrors.firstName = 'First name is required.';
    if (!lastName) formErrors.lastName = 'Last name is required.';
    if (!email) {
      formErrors.email = 'Email is required.';
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      formErrors.email = 'Enter a valid email address.';
    }
    if (!password) {
      formErrors.password = 'Password is required.';
    } else if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/.test(password)) {
      formErrors.password =
        'Password must be at least 8 characters, include 1 uppercase, 1 lowercase, and 1 number.';
    }
    if (!confirmPassword) {
      formErrors.confirmPassword = 'Confirm password is required.';
    } else if (password !== confirmPassword) {
      formErrors.confirmPassword = 'Passwords do not match.';
    }

    if (Object.keys(formErrors).length > 0) {
      setErrors(formErrors);
      setSuccessMessage('');
      setErrorMessage('');
    } else {
      setErrors({});
      try {
        const user = { firstName, lastName, email, password };
        await createUser(user);
        setSuccessMessage('Registration successful! Redirecting to login...');
        setErrorMessage('');
        setFormData({
          firstName: '',
          lastName: '',
          email: '',
          password: '',
          confirmPassword: '',
        });

        // Redirect to login page after 3 seconds
        setTimeout(() => {
          navigate('/login');
        }, 3000);
      } catch (error) {
        setErrorMessage(error.message || 'Failed to register user.');
        setSuccessMessage('');
      }
    }
  };

  return (
    <div className={styles.registerContainer}>
      <Container>
        <Row className="justify-content-center">
          <Col md={6} className={styles.registerCard}>
            <h2 className={styles.title}>Create Your Account</h2>
            <p className={styles.subtitle}>Join us to start your quiz journey!</p>
            {successMessage && <Alert variant="success">{successMessage}</Alert>}
            {errorMessage && <Alert variant="danger">{errorMessage}</Alert>}
            <Form onSubmit={handleSubmit}>
              <Form.Group className="mb-3" controlId="formFirstName">
                <Form.Label>First Name</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter first name"
                  name="firstName"
                  value={formData.firstName}
                  onChange={handleChange}
                  isInvalid={!!errors.firstName}
                  className={styles.input}
                />
                <Form.Control.Feedback type="invalid">
                  {errors.firstName}
                </Form.Control.Feedback>
              </Form.Group>

              <Form.Group className="mb-3" controlId="formLastName">
                <Form.Label>Last Name</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter last name"
                  name="lastName"
                  value={formData.lastName}
                  onChange={handleChange}
                  isInvalid={!!errors.lastName}
                  className={styles.input}
                />
                <Form.Control.Feedback type="invalid">
                  {errors.lastName}
                </Form.Control.Feedback>
              </Form.Group>

              <Form.Group className="mb-3" controlId="formEmail">
                <Form.Label>Email</Form.Label>
                <Form.Control
                  type="email"
                  placeholder="Enter email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  isInvalid={!!errors.email}
                  className={styles.input}
                />
                <Form.Control.Feedback type="invalid">
                  {errors.email}
                </Form.Control.Feedback>
              </Form.Group>

              <Form.Group className="mb-3" controlId="formPassword">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="Enter password"
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  isInvalid={!!errors.password}
                  className={styles.input}
                />
                <Form.Control.Feedback type="invalid">
                  {errors.password}
                </Form.Control.Feedback>
              </Form.Group>

              <Form.Group className="mb-3" controlId="formConfirmPassword">
                <Form.Label>Confirm Password</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="Confirm password"
                  name="confirmPassword"
                  value={formData.confirmPassword}
                  onChange={handleChange}
                  isInvalid={!!errors.confirmPassword}
                  className={styles.input}
                />
                <Form.Control.Feedback type="invalid">
                  {errors.confirmPassword}
                </Form.Control.Feedback>
              </Form.Group>

              <Button variant="primary" type="submit" className={`w-100 ${styles.button}`}>
                Register
              </Button>
            </Form>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default RegisterPage;
