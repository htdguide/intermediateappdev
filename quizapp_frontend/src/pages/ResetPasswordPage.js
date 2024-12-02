import React, { useState } from 'react';
import { requestPasswordReset, verifyResetCode, resetPassword } from '../services/api';
import { useNavigate } from 'react-router-dom';
import styles from '../styles/ResetPasswordPage.module.css';

const ResetPasswordPage = () => {
    const [email, setEmail] = useState('');
    const [code, setCode] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [step, setStep] = useState(1); // 1: Request code, 2: Verify code, 3: Reset password
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleRequestCode = async (e) => {
        e.preventDefault();
        setErrorMessage('');
        setIsLoading(true);

        try {
            await requestPasswordReset(email);
            setSuccessMessage('Verification code sent to your email.');
            setStep(2); // Move to verification step
        } catch (error) {
            setErrorMessage(error.message || 'Failed to send verification code.');
        } finally {
            setIsLoading(false);
        }
    };

    const handleVerifyCode = async (e) => {
        e.preventDefault();
        setErrorMessage('');
        setIsLoading(true);

        try {
            await verifyResetCode(email, code);
            setSuccessMessage('Code verified. You can now reset your password.');
            setStep(3); // Move to reset password step
        } catch (error) {
            setErrorMessage(error.message || 'Invalid or expired verification code.');
        } finally {
            setIsLoading(false);
        }
    };

    const handleResetPassword = async (e) => {
        e.preventDefault();
        setErrorMessage('');
        setIsLoading(true);

        try {
            await resetPassword(email, code, newPassword);
            setSuccessMessage('Password has been reset successfully. Redirecting to login...');
            setTimeout(() => navigate('/login'), 3000); // Redirect to login after 3 seconds
        } catch (error) {
            setErrorMessage(error.message || 'Failed to reset password.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className={styles.resetPasswordContainer}>
            <div className={styles.card}>
                <h2 className={styles.title}>Reset Password</h2>
                {errorMessage && <p className={styles.error}>{errorMessage}</p>}
                {successMessage && <p className={styles.success}>{successMessage}</p>}

                {step === 1 && (
                    <form onSubmit={handleRequestCode} className={styles.form}>
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="Enter your email"
                            required
                            className={styles.input}
                        />
                        <button type="submit" disabled={isLoading} className={styles.button}>
                            {isLoading ? 'Sending code...' : 'Send Verification Code'}
                        </button>
                    </form>
                )}

                {step === 2 && (
                    <form onSubmit={handleVerifyCode} className={styles.form}>
                        <input
                            type="text"
                            value={code}
                            onChange={(e) => setCode(e.target.value)}
                            placeholder="Enter verification code"
                            required
                            className={styles.input}
                        />
                        <button type="submit" disabled={isLoading} className={styles.button}>
                            {isLoading ? 'Verifying...' : 'Verify Code'}
                        </button>
                    </form>
                )}

                {step === 3 && (
                    <form onSubmit={handleResetPassword} className={styles.form}>
                        <input
                            type="password"
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                            placeholder="Enter new password"
                            required
                            className={styles.input}
                        />
                        <button type="submit" disabled={isLoading} className={styles.button}>
                            {isLoading ? 'Resetting...' : 'Reset Password'}
                        </button>
                    </form>
                )}
            </div>
        </div>
    );
};

export default ResetPasswordPage;
