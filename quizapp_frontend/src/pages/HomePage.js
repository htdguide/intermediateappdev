import React, { useState, useEffect } from 'react';
import { Button, Row, Col, Container, Carousel } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import styles from '../styles/HomePage.module.css';
import carousel1 from "../assets/HomePage/carousel1.jpg";
import carousel2 from "../assets/HomePage/carousel2.jpg";
import carousel3 from "../assets/HomePage/carousel3.jpg";
import teamwork from "../assets/HomePage/teamwork.jpg";
import leaderboard from "../assets/HomePage/leaderboard.jpg";
import certificate from "../assets/HomePage/certificate.jpg";

function HomePage() {
  const [animatedText, setAnimatedText] = useState('');
  const textToAnimate = 'Discover Quizzes, Challenge Yourself, Master Knowledge!';

  useEffect(() => {
    let index = 0;
    const interval = setInterval(() => {
      if (index < textToAnimate.length) {
        setAnimatedText((prev) => prev + textToAnimate[index]);
        index++;
      } else {
        clearInterval(interval);
      }
    }, 100); // Adjust the speed of animation here
    return () => clearInterval(interval);
  }, []);

  return (
    <div className={styles.background}>
      {/* Hero Section */}
      <Container fluid className={styles.heroSection}>
        <Row className="justify-content-center text-center">
          <Col md={12} className={styles.col}>
            <div className={styles.textContainer}>
              <h1 className={`text-primary ${styles.title}`}>{animatedText}</h1>
              <p className={`text-secondary ${styles.subtitle}`}>
                Your ultimate quiz platform to create, manage, and play quizzes. Test your
                knowledge and improve your skills!
              </p>
              <div className={`mt-4 ${styles.buttonContainer}`}>
                <Button
                  as={Link}
                  to="/user"
                  variant="primary"
                  className={`mx-2 ${styles.button}`}
                >
                  Play Quizzes
                </Button>
                <Button
                  as={Link}
                  to="/login"
                  variant="secondary"
                  className={`mx-2 ${styles.button}`}
                >
                  Login
                </Button>
                <Button
                  as={Link}
                  to="/register"
                  variant="success"
                  className={`mx-2 ${styles.button}`}
                >
                  Register
                </Button>
              </div>
            </div>
          </Col>

        </Row>
      </Container>

      {/* Carousel Section */}
      <Container fluid className={`mt-5 ${styles.carouselContainer}`}>
        <Carousel>
          <Carousel.Item>
            <img
              className="d-block w-100"
              src={carousel1}
              alt="Quiz Fun"
            />
            <Carousel.Caption>
              <h3>Interactive Quizzes</h3>
              <p>Engage with our user-friendly quizzes and learn effectively.</p>
            </Carousel.Caption>
          </Carousel.Item>
          <Carousel.Item>
            <img
              className="d-block w-100"
              src={carousel2}
              alt="Knowledge Game"
            />
            <Carousel.Caption>
              <h3>Challenge Your Friends</h3>
              <p>Compete in tournaments and become the ultimate quiz master.</p>
            </Carousel.Caption>
          </Carousel.Item>
          <Carousel.Item>
            <img
              className="d-block w-100"
              src={carousel3}
              alt="Test Yourself"
            />
            <Carousel.Caption>
              <h3>Track Your Progress</h3>
              <p>Analyze your performance and keep improving.</p>
            </Carousel.Caption>
          </Carousel.Item>
        </Carousel>
      </Container>

      {/* Featured Sections */}
      <Container className={`mt-5 ${styles.featuredSection}`}>
        <Row>
          <Col className={styles.featuredItem}>
            <img
              src={teamwork}
              alt="Create Quizzes"
              className={styles.featuredImage}
            />
            <h4>Create Quizzes</h4>
            <p>Design your own quizzes to test others' knowledge.</p>
          </Col>
          <Col className={styles.featuredItem}>
            <img
              src={leaderboard}
              alt="Leaderboard"
              className={styles.featuredImage}
            />
            <h4>Leaderboard</h4>
            <p>See how you rank among your peers and friends.</p>
          </Col>
          <Col className={styles.featuredItem}>
            <img
              src={certificate}
              alt="Earn Certificates"
              className={styles.featuredImage}
            />
            <h4>Earn Certificates</h4>
            <p>Show off your achievements with our certification system.</p>
          </Col>
        </Row>
      </Container>

      {/* Testimonials Section */}
      <Container className={`mt-5 ${styles.testimonials}`}>
        <h2 className="text-center mb-4">What Our Users Say</h2>
        <Row className="justify-content-center">
          <Col md={6} className={styles.testimonialItem}>
            <p>
              "QuizApp has been a game-changer for me. The quizzes are engaging, and I
              love the leaderboard feature!" - <strong>Jane Doe</strong>
            </p>
          </Col>
          <Col md={6} className={styles.testimonialItem}>
            <p>
              "This platform is perfect for anyone looking to learn and have fun at the
              same time!" - <strong>John Smith</strong>
            </p>
          </Col>
        </Row>
      </Container>

      {/* Footer Section */}
      <footer className={styles.footer}>
        <p>&copy; {new Date().getFullYear()} QuizApp. All rights reserved.</p>
      </footer>
    </div>
  );
}

export default HomePage;
