import React, { useEffect, useState } from 'react';
import { Pie } from 'react-chartjs-2';
import { getUserSummary } from '../../services/api';
import { Spinner, Alert, Card } from 'react-bootstrap';

const Dashboard = () => {
    const [summary, setSummary] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchSummary = async () => {
            try {
                const user = JSON.parse(localStorage.getItem('user'));
                if (!user) throw new Error('User not found');
                const data = await getUserSummary(user.userId);
                setSummary(data);
                setLoading(false);
            } catch (err) {
                setError(err.message || 'Failed to fetch summary data');
                setLoading(false);
            }
        };

        fetchSummary();
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

    const pieData = {
        labels: Object.keys(summary.scoreByQuiz),
        datasets: [
            {
                data: Object.values(summary.scoreByQuiz),
                backgroundColor: ['#007bff', '#28a745', '#ffc107', '#dc3545'],
            },
        ],
    };

    return (
        <div>
            <Card className="mb-4">
                <Card.Body>
                    <Card.Title>Summary</Card.Title>
                    <p><strong>Total Quizzes Attempted:</strong> {summary.totalQuizzesAttempted}</p>
                    <p><strong>Total Score:</strong> {summary.totalScore}</p>
                    <p><strong>Last Played:</strong> {new Date(summary.lastPlayed).toLocaleString()}</p>
                </Card.Body>
            </Card>
            <h4 className="text-center">Score Distribution by Quiz</h4>
            <Pie data={pieData} />
        </div>
    );
};

export default Dashboard;
