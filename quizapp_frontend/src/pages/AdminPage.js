import React from 'react';
import { Tab, Nav, Container } from 'react-bootstrap';
import ManageUsers from '../components/admin/ManageUsers';
import ManageQuizzes from '../components/admin/ManageQuizzes';
import ManageUserRecords from '../components/admin/ManageUserRecords';

const AdminPage = () => {
    const user = JSON.parse(localStorage.getItem('user')); // Get the logged-in admin user data

    if (!user) {
        console.error("AdminPage: No user logged in.");
        return <div>You must be logged in to view this page.</div>;
    }

    if (user.usertype !== 'ADMIN') {
        console.warn(`AdminPage: Unauthorized access attempt by user type: ${user.usertype}`);
        return <div>You do not have permission to access this page.</div>;
    }

    console.log("AdminPage: Rendering admin dashboard for user:", user);

    return (
        <Container className="mt-4">
            <h2>Admin Dashboard</h2>
            <h3>Welcome, {user.firstName} {user.lastName}!</h3>
            <p>Email: {user.email}</p>
            <p>User Type: {user.usertype}</p>
            <hr />

            <Tab.Container defaultActiveKey="manageUsers">
                <Nav variant="tabs" className="mb-3">
                    <Nav.Item>
                        <Nav.Link eventKey="manageUsers">Manage Users</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                        <Nav.Link eventKey="manageQuizzes">Manage Quizzes</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                        <Nav.Link eventKey="userRecords">User Records</Nav.Link>
                    </Nav.Item>
                </Nav>
                <Tab.Content>
                    <Tab.Pane eventKey="manageUsers">
                        <ManageUsers />
                    </Tab.Pane>
                    <Tab.Pane eventKey="manageQuizzes">
                        <ManageQuizzes />
                    </Tab.Pane>
                    <Tab.Pane eventKey="userRecords">
                        <ManageUserRecords />
                    </Tab.Pane>
                </Tab.Content>
            </Tab.Container>
        </Container>
    );
};

export default AdminPage;
