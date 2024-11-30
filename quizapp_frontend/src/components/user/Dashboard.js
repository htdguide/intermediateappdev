import React, { useEffect, useState } from 'react';
import { Pie } from 'react-chartjs-2';
import { getUserRecordsByUserId } from '../../services/api';
import { Spinner, Alert, Card } from 'react-bootstrap';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';

// Register Chart.js components
ChartJS.register(ArcElement, Tooltip, Legend);

const Dashboard = () => {
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
            <div className="text-center my-4">
                <Spinner animation="border" variant="primary" />
                <p className="text-muted mt-2">Loading dashboard...</p>
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
                    '#007bff', // Blue
                    '#28a745', // Green
                    '#ffc107', // Yellow
                    '#dc3545', // Red
                    '#17a2b8', // Teal
                ],
            },
        ],
    };

    return (
        <div>
            <Card className="mb-4">
                <Card.Body>
                    <Card.Title>Summary</Card.Title>
                    <p>
                        <strong>Total Quizzes Attempted:</strong> {totalQuizzesAttempted}
                    </p>
                    <p>
                        <strong>Last Played:</strong> {lastPlayed.toLocaleString()}
                    </p>
                </Card.Body>
            </Card>
            <h4 className="text-center">Score Distribution by Quiz</h4>
            <Pie data={pieData} />
        </div>
    );
};

export default Dashboard;
