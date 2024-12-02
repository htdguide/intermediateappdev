import React from 'react';
import { Tab, Nav, Container } from 'react-bootstrap';
import ManageUsers from '../components/admin/ManageUsers';
import ManageQuizzes from '../components/admin/ManageQuizzes';
import styles from '../styles/AdminPage.module.css';

const AdminPage = () => {
    const user = JSON.parse(localStorage.getItem('user')); // Get the logged-in admin user data

    if (!user) {
        console.error("AdminPage: No user logged in.");
        return <div className={styles.fullHeight}>You must be logged in to view this page.</div>;
    }

    if (user.usertype !== 'ADMIN') {
        console.warn(`AdminPage: Unauthorized access attempt by user type: ${user.usertype}`);
        return <div className={styles.fullHeight}>You do not have permission to access this page.</div>;
    }

    return (
        <div className={styles.adminContainer}>
            <Container fluid>
                <h2 className={styles.adminTitle}>Admin Dashboard</h2>
                <h4 className={styles.greeting}>
                    Welcome, {user.firstName} {user.lastName}!
                </h4>
                <p className="text-muted">Email: {user.email}</p>
                <hr />
                <Tab.Container defaultActiveKey="manageUsers">
                    <Nav variant="tabs" className={styles.topNav}>
                        <Nav.Item>
                            <Nav.Link eventKey="manageUsers" className={styles.tabLink}>
                                Manage Users
                            </Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link eventKey="manageQuizzes" className={styles.tabLink}>
                                Manage Quizzes
                            </Nav.Link>
                        </Nav.Item>
                    </Nav>
                    <Tab.Content className={styles.tabContent}>
                        <Tab.Pane eventKey="manageUsers">
                            <ManageUsers />
                        </Tab.Pane>
                        <Tab.Pane eventKey="manageQuizzes">
                            <ManageQuizzes />
                        </Tab.Pane>
                    </Tab.Content>
                </Tab.Container>
            </Container>
        </div>
    );
};

export default AdminPage;
