import React from 'react';
import { Button, Row, Col, Container } from 'react-bootstrap';
import { Link } from 'react-router-dom';

function HomePage() {
  return (
    <Container className="mt-5">
      <Row className="justify-content-center text-center">
        <Col md={8}>
          <h1 className="text-primary">Welcome to QuizApp</h1>
          <p className="text-secondary">
            Your ultimate quiz platform to create, manage, and play quizzes.
            Test your knowledge and improve your skills.
          </p>
          <div className="mt-4">
            <Button as={Link} to="/user" variant="primary" className="mx-2">
              Play Quizzes
            </Button>
            <Button as={Link} to="/login" variant="secondary" className="mx-2">
              Login
            </Button>
            <Button as={Link} to="/register" variant="success" className="mx-2">
              Register
            </Button>
          </div>
        </Col>
      </Row>
    </Container>
  );
}

export default HomePage;
