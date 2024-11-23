import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';
import { getQuizQuestions } from '../services/api';

function PlayQuizPage() {
  const { quizId } = useParams();
  const [questions, setQuestions] = useState([]);
  const [userAnswers, setUserAnswers] = useState({});
  const [score, setScore] = useState(null);

  useEffect(() => {
    const fetchQuestions = async () => {
      console.log(`Fetching questions for quiz ID: ${quizId}`);
      try {
        const fetchedQuestions = await getQuizQuestions(quizId);
        console.log('Fetched Questions:', fetchedQuestions);

        const formattedQuestions = fetchedQuestions.map((item) => ({
          ...item.question,
        }));
        setQuestions(formattedQuestions);
      } catch (error) {
        console.error('Error fetching questions:', error);
      }
    };

    fetchQuestions();
  }, [quizId]);

  const handleOptionChange = (questionId, selectedOption) => {
    console.log(`Selected option for question ID ${questionId}: ${selectedOption}`);
    setUserAnswers((prevAnswers) => ({
      ...prevAnswers,
      [questionId]: selectedOption,
    }));
  };

  const handleSubmitQuiz = () => {
    console.log('Submitting quiz...');
    let calculatedScore = 0;
    questions.forEach((question) => {
      if (userAnswers[question.questionId] === question.answer) {
        calculatedScore += 1;
      }
    });
    console.log(`Final Score: ${calculatedScore}`);
    setScore(calculatedScore);
  };

  if (!questions.length) {
    return <p>Loading questions...</p>;
  }

  if (score !== null) {
    return (
      <Container className="mt-5">
        <Row className="justify-content-center">
          <Col md={8} className="text-center">
            <h2>Your Score: {score} / {questions.length}</h2>
            <Button variant="primary" onClick={() => window.location.reload()}>
              Retake Quiz
            </Button>
          </Col>
        </Row>
      </Container>
    );
  }

  return (
    <Container className="mt-5">
      <Row className="justify-content-center">
        <Col md={8}>
          <h3 className="text-center mb-4">Quiz Questions</h3>
          <Form>
            {questions.map((question, index) => (
              <Card key={question.questionId} className="mb-4">
                <Card.Body>
                  <Card.Title>
                    Question {index + 1}: {question.text}
                  </Card.Title>
                  <div className="mt-3">
                    {[...question.incorrectAnswers, question.answer].map((option, idx) => (
                      <Form.Check
                        type="radio"
                        key={idx}
                        id={`q${question.questionId}-option${idx}`}
                        name={`question-${question.questionId}`}
                        label={option}
                        value={option}
                        onChange={() => handleOptionChange(question.questionId, option)}
                        checked={userAnswers[question.questionId] === option}
                      />
                    ))}
                  </div>
                </Card.Body>
              </Card>
            ))}
            <div className="text-center mt-4">
              <Button variant="success" onClick={handleSubmitQuiz}>
                Submit Quiz
              </Button>
            </div>
          </Form>
        </Col>
      </Row>
    </Container>
  );
}

export default PlayQuizPage;
