import React, { useEffect, useState } from 'react';
import { Pie } from 'react-chartjs-2';
import { getUserRecordsByUserId } from '../../services/api';
import { Spinner, Alert, Card, Container, Row, Col, Button } from 'react-bootstrap';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import styles from './Dashboard.module.css';

// Register Chart.js components
ChartJS.register(ArcElement, Tooltip, Legend);

const Dashboard = ({ setActiveTab }) => {
    const [records, setRecords] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchRecords = async () => {
            try {
                const user = JSON.parse(localStorage.getItem('user'));
                if (!user) throw new Error('User not found');
                const data = await getUserRecordsByUserId(user.userId);
                setRecords(data);
                setLoading(false);
            } catch (err) {
                setError(err.message || 'Failed to fetch user records');
                setLoading(false);
            }
        };

        fetchRecords();
    }, []);

    if (loading) {
        return (
            <div className={styles.loadingContainer}>
                <Spinner animation="border" variant="primary" />
                <p className={`mt-3 ${styles.loadingMessage}`}>Fetching your dashboard...</p>
            </div>
        );
    }

    if (error) {
        return <Alert variant="danger">{error}</Alert>;
    }

    // Compute summary data from records
    const totalQuizzesAttempted = records.length;

    const scoreByQuiz = records.reduce((acc, record) => {
        acc[record.quiz.title] = (acc[record.quiz.title] || 0) + record.score;
        return acc;
    }, {});

    const lastPlayed = records.reduce((latest, record) => {
        const playedAt = new Date(record.playedAt);
        return playedAt > latest ? playedAt : latest;
    }, new Date(0));

    const pieData = {
        labels: Object.keys(scoreByQuiz),
        datasets: [
            {
                data: Object.values(scoreByQuiz),
                backgroundColor: [
                    '#007bff', '#28a745', '#ffc107', '#dc3545', '#17a2b8', '#6610f2',
                ],
                hoverBackgroundColor: [
                    '#0056b3', '#218838', '#e0a800', '#c82333', '#138496', '#520dcb',
                ],
                borderColor: '#ffffff',
                borderWidth: 2,
            },
        ],
    };

    return (
        <Container className={styles.dashboardContainer}>
            <h1 className={`text-center mb-4 ${styles.dashboardTitle}`}>Your Dashboard</h1>
            <Row>
                <Col md={4}>
                    <Card className={`mb-4 ${styles.summaryCard}`}>
                        <Card.Body>
                            <Card.Title className={styles.cardTitle}>Summary</Card.Title>
                            <p>
                                <strong>Total Quizzes Attempted:</strong> {totalQuizzesAttempted}
                            </p>
                            <p>
                                <strong>Last Played:</strong>{' '}
                                {lastPlayed.toLocaleDateString()}{' '}
                                at {lastPlayed.toLocaleTimeString()}
                            </p>
                            <Button
                                variant="primary"
                                className={styles.playQuizButton}
                                onClick={() => setActiveTab('playQuiz')} // Navigate to PlayQuiz tab
                            >
                                Go to Play Quiz
                            </Button>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={8}>
                    <div className={`p-3 ${styles.chartContainer}`}>
                        <h4 className={styles.chartTitle}>Score Distribution</h4>
                        <Pie data={pieData} className={styles.chart} />
                    </div>
                </Col>
            </Row>
        </Container>
    );
};

export default Dashboard;
