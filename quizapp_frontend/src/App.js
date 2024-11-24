import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import Header from './components/Header';
import Footer from './components/Footer';
import Home from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import UserPage from './pages/UserPage';
import AdminPage from './pages/AdminPage';
import PlayQuizPage from './pages/PlayQuizPage';
import ProtectedRoute from './components/ProtectedRoute';

const App = () => {
    const [user, setUser] = useState(null);

    useEffect(() => {
        const storedUser = JSON.parse(localStorage.getItem('user'));
        if (storedUser) {
            setUser(storedUser);
        }
    }, []);

    const logout = () => {
        localStorage.removeItem('user');
        setUser(null);
        window.location.href = '/'; // Redirect to homepage
    };

    return (
        <Router>
            <Header user={user} logout={logout} />
            <div className="container mt-4">
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/login" element={<LoginPage setUser={setUser} />} />
                    <Route
                        path="/user"
                        element={
                            <ProtectedRoute user={user} allowedRoles={['PLAYER']}>
                                <UserPage />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/admin"
                        element={
                            <ProtectedRoute user={user} allowedRoles={['ADMIN']}>
                                <AdminPage />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/play-quiz/:quizId"
                        element={
                            <ProtectedRoute user={user} allowedRoles={['PLAYER']}>
                                <PlayQuizPage />
                            </ProtectedRoute>
                        }
                    />
                </Routes>
            </div>
            <Footer />
        </Router>
    );
};

export default App;