import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

// Axios instance with basic configuration
const adminApi = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Fetch all users
export const getAllUsers = async () => {
    try {
        const response = await adminApi.get('/users');
        console.log('Admin API: Fetched all users', response.data);
        return response.data;
    } catch (error) {
        console.error('Admin API: Error fetching all users:', error);
        throw error.response ? error.response.data : 'Failed to fetch users';
    }
};

// Fetch user by email
export const getUserByEmail = async (email) => {
    try {
        const response = await adminApi.get(`/users/email/${email}`);
        console.log(`Admin API: Fetched user by email: ${email}`, response.data);
        return response.data;
    } catch (error) {
        console.error(`Admin API: Error fetching user by email ${email}:`, error);
        throw error.response ? error.response.data : 'Failed to fetch user by email';
    }
};

// Fetch user by ID
export const getUserById = async (id) => {
    try {
        const response = await adminApi.get(`/users/${id}`);
        console.log(`Admin API: Fetched user by ID: ${id}`, response.data);
        return response.data;
    } catch (error) {
        console.error(`Admin API: Error fetching user by ID ${id}:`, error);
        throw error.response ? error.response.data : 'Failed to fetch user by ID';
    }
};

// Create or update a user
export const saveUser = async (user) => {
    try {
        const response = await adminApi.post('/users', user);
        console.log('Admin API: User saved successfully', response.data);
        return response.data;
    } catch (error) {
        console.error('Admin API: Error saving user:', error);
        throw error.response ? error.response.data : 'Failed to save user';
    }
};

// Update user by ID
export const updateUserById = async (id, updatedUser) => {
    try {
        const response = await adminApi.put(`/users/${id}`, updatedUser);
        console.log(`Admin API: Updated user with ID: ${id}`, response.data);
        return response.data;
    } catch (error) {
        console.error(`Admin API: Error updating user with ID ${id}:`, error);
        throw error.response ? error.response.data : 'Failed to update user';
    }
};

// Delete user by ID
export const deleteUserById = async (id) => {
    try {
        const response = await adminApi.delete(`/users/${id}`);
        console.log(`Admin API: Deleted user with ID: ${id}`, response.data);
        return response.data;
    } catch (error) {
        console.error(`Admin API: Error deleting user with ID ${id}:`, error);
        throw error.response ? error.response.data : 'Failed to delete user';
    }
};

// Create a quiz
export const createQuiz = async ({ title, startDate, endDate, category, difficulty }) => {
    try {
        const response = await adminApi.post('/quizcreation/create', null, {
            params: {
                title,
                startDate,
                endDate,
                category,
                difficulty,
            },
        });
        console.log('Admin API: Quiz created successfully', response.data);
        return response.data;
    } catch (error) {
        console.error('Admin API: Error creating quiz:', error);
        throw error.response ? error.response.data : 'Failed to create quiz';
    }
};