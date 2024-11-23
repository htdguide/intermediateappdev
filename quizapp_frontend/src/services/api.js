import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

// Axios instance with basic configuration
const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Method for user login (authenticate)
export const loginUser = async (email, password) => {
    try {
        const response = await api.post('/users/login', { email, password });
        return response.data; // Return user data on successful login
    } catch (error) {
        console.error('Error logging in:', error);
        throw error.response ? error.response.data : 'Login failed';
    }
};


// Method to get all users (for admin)
export const getAllUsers = async () => {
    try {
        const response = await api.get('/users');
        return response.data; // Return list of users
    } catch (error) {
        console.error('Error fetching users:', error);
        throw error.response ? error.response.data : 'Failed to fetch users';
    }
};

// Method to create a new user (sign up)
export const createUser = async (userData) => {
    try {
        const response = await api.post('/users', userData);
        return response.data; // Return created user
    } catch (error) {
        console.error('Error creating user:', error);
        throw error.response ? error.response.data : 'Failed to create user';
    }
};

// Method to update an existing user
export const updateUser = async (userId, updatedData) => {
    try {
        const response = await api.put(`/users/${userId}`, updatedData);
        return response.data; // Return updated user data
    } catch (error) {
        console.error('Error updating user:', error);
        throw error.response ? error.response.data : 'Failed to update user';
    }
};

// Method to delete a user (admin only)
export const deleteUser = async (userId) => {
    try {
        const response = await api.delete(`/users/${userId}`);
        return response.data; // Return response or success message
    } catch (error) {
        console.error('Error deleting user:', error);
        throw error.response ? error.response.data : 'Failed to delete user';
    }
};

// Method to fetch all quizzes
export const getAllQuizzes = async () => {
    try {
        const response = await api.get('/quizzes');
        return response.data; // Return the list of quizzes
    } catch (error) {
        console.error('Error fetching quizzes:', error);
        throw error.response ? error.response.data : 'Failed to fetch quizzes';
    }
};

// Method to fetch questions for a specific quiz
export const getQuizQuestions = async (quizId) => {
    try {
        const response = await api.get(`/quiz-questions/quiz/${quizId}`);
        return response.data; // Return questions data
    } catch (error) {
        console.error(`Error fetching questions for quiz ${quizId}:`, error);
        throw error.response ? error.response.data : 'Failed to fetch quiz questions';
    }
};

// Save or update a user record
export const saveOrUpdateUserRecord = async (userId, quizId, score) => {
    try {
        const response = await api.post('/user-records', {
            user: { userId },
            quiz: { quizId },
            score,
            playedAt: new Date().toISOString(), // Current timestamp
        });
        return response.data; // Return the saved or updated record
    } catch (error) {
        console.error('Error saving/updating user record:', error);
        throw error.response ? error.response.data : 'Failed to save/update user record';
    }
};

// Get a user record by userId and quizId
export const getUserRecordByUserIdAndQuizId = async (userId, quizId) => {
    try {
        const response = await api.get(`/user-records/user/${userId}/quiz/${quizId}`);
        return response.data; // Return the user record
    } catch (error) {
        console.error('Error fetching user record:', error);
        throw error.response ? error.response.data : 'Failed to fetch user record';
    }
};