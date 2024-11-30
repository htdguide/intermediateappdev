import React, { useState } from 'react';
import { Tab, Tabs } from 'react-bootstrap';
import Dashboard from '../components/user/dashboard';
import PlayQuiz from '../components/user/PlayQuiz';
import { useSearchParams } from 'react-router-dom';

const UserPage = () => {
    const user = JSON.parse(localStorage.getItem('user')); // Fetch user data from localStorage
    const [searchParams] = useSearchParams();
    const defaultTab = searchParams.get('tab') || 'dashboard'; // Default to 'dashboard'
    const [key, setKey] = useState(defaultTab);
    const [loading, setLoading] = useState(false);

    const handleTabSelect = (k) => {
        setLoading(true);
        setKey(k);
        setTimeout(() => setLoading(false), 500); // Simulate loading time
    };

    if (!user) {
        return (
            <div className="container mt-4">
                <div className="alert alert-danger text-center">
                    User not found. Please log in.
                </div>
            </div>
        );
    }

    return (
        <div className="container mt-4">
            <h2 className="text-center mb-4">Welcome, {user.firstName}!</h2>
            <Tabs
                id="user-tabs"
                activeKey={key}
                onSelect={handleTabSelect}
                className="mb-3 custom-tabs"
            >
                <Tab eventKey="dashboard" title={<span className="tab-title">📊 Dashboard</span>}>
                    {loading ? (
                        <div className="text-center mt-4">Loading Dashboard...</div>
                    ) : (
                        <Dashboard user={user} />
                    )}
                </Tab>
                <Tab eventKey="playQuiz" title={<span className="tab-title">🎮 Play Quiz</span>}>
                    {loading ? (
                        <div className="text-center mt-4">Loading Play Quiz...</div>
                    ) : (
                        <PlayQuiz user={user} />
                    )}
                </Tab>
            </Tabs>
        </div>
    );
};

export default UserPage;
