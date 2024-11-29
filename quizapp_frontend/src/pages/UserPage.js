import React, { useState } from 'react';
import { Tab, Tabs } from 'react-bootstrap';
import Dashboard from '../components/user/Dashboard';
import PlayQuiz from './PlayQuiz';

const UserPage = () => {
    const [key, setKey] = useState('dashboard');

    return (
        <div className="container mt-4">
            <Tabs
                id="user-tabs"
                activeKey={key}
                onSelect={(k) => setKey(k)}
                className="mb-3"
            >
                <Tab eventKey="dashboard" title="Dashboard">
                    <Dashboard />
                </Tab>
                <Tab eventKey="playQuiz" title="Play Quiz">
                    <PlayQuiz />
                </Tab>
            </Tabs>
        </div>
    );
};

export default UserPage;