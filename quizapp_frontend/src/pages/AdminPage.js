import React from 'react';

const AdminPage = () => {
    const user = JSON.parse(localStorage.getItem('user')); // Get the logged-in admin user data

    if (!user) {
        return <div>You must be logged in to view this page.</div>;
    }

    return (
        <div>
            <h2>Admin Dashboard</h2>
            <h3>Welcome, {user.firstName} {user.lastName}!</h3>
            <p>Email: {user.email}</p>
            <p>User Type: {user.usertype}</p>
        </div>
    );
};

export default AdminPage;
