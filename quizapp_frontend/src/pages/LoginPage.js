import React, { useState } from 'react';
import { loginUser } from '../services/api';
import { useNavigate } from 'react-router-dom';

const LoginPage = ({ setUser }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setErrorMessage('');

        if (!validateEmail(email)) {
            setErrorMessage('Please enter a valid email address.');
            return;
        }

        setIsLoading(true);
        try {
            const user = await loginUser(email, password);

            // Store user data in localStorage and state
            localStorage.setItem('user', JSON.stringify(user));
            setUser(user);

            // Redirect based on usertype
            if (user.usertype === 'ADMIN') {
                navigate('/admin');
            } else if (user.usertype === 'PLAYER') {
                navigate('/user');
            } else {
                navigate('/'); // Fallback route
            }
        } catch (error) {
            setErrorMessage(error.message || 'Login failed. Please try again.');
        } finally {
            setIsLoading(false);
        }
    };

    const validateEmail = (email) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    };

    return (
        <div className="login-container">
            <h2>Login</h2>
            {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
            <form onSubmit={handleLogin}>
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Email"
                    required
                />
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Password"
                    required
                />
                <button type="submit" disabled={isLoading}>
                    {isLoading ? 'Logging in...' : 'Login'}
                </button>
                <a href="/reset-password">Forgot Password?</a>
            </form>
        </div>
    );
};

export default LoginPage;
