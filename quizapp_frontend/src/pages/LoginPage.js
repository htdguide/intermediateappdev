import React, { useState } from 'react';
import { loginUser } from '../services/api';
import { useNavigate } from 'react-router-dom';
import styles from '../styles/LoginPage.module.css';

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
        <div className={styles.loginContainer}>
            <div className={styles.loginCard}>
                <h2 className={styles.title}>Welcome Back!</h2>
                <p className={styles.subtitle}>Log in to access your account</p>
                {errorMessage && <p className={styles.error}>{errorMessage}</p>}
                <form onSubmit={handleLogin} className={styles.form}>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="Email"
                        className={styles.input}
                        required
                    />
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Password"
                        className={styles.input}
                        required
                    />
                    <button type="submit" className={styles.button} disabled={isLoading}>
                        {isLoading ? 'Logging in...' : 'Login'}
                    </button>
                    <a href="/reset-password" className={styles.forgotPassword}>
                        Forgot Password?
                    </a>
                </form>
            </div>
        </div>
    );
};

export default LoginPage;
