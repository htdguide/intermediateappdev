import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import Header from './components/Header';
import Footer from './components/Footer';
import Home from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import UserPage from './pages/UserPage'; // Player page
import AdminPage from './pages/AdminPage'; // Admin page
import PlayQuizPage from './pages/PlayQuizPage';

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
  };

  return (
    <Router future={{ v7_startTransition: true, v7_relativeSplatPath: true }}>
      <Header user={user} logout={logout} />
      <div className="container">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<LoginPage />} />
          <Route
            path="/user"
            element={
              user ? (
                user.usertype === 'PLAYER' ? (
                  <UserPage />
                ) : (
                  <Navigate to="/admin" />
                )
              ) : (
                <Navigate to="/login" />
              )
            }
          />
          <Route
            path="/admin"
            element={
              user ? (
                user.usertype === 'ADMIN' ? (
                  <AdminPage />
                ) : (
                  <Navigate to="/user" />
                )
              ) : (
                <Navigate to="/login" />
              )
            }
          />
          <Route path="/play-quiz/:quizId" element={<PlayQuizPage />} />
        </Routes>
      </div>
      <Footer />
    </Router>
  );
};

export default App;
