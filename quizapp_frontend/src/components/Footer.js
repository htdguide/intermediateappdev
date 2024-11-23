import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';

function Footer() {
  return (
    <footer className="bg-dark text-white py-3">
      <Container>
        <Row>
          <Col className="text-center">
            <p className="mb-0">&copy; 2024 QuizApp. All rights reserved.</p>
          </Col>
        </Row>
      </Container>
    </footer>
  );
}

export default Footer;
