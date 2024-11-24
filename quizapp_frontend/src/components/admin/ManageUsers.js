import React, { useState, useEffect } from 'react';
import { Table, Button, Spinner, Alert } from 'react-bootstrap';
import { getAllUsers, saveUser, updateUserById, deleteUserById } from '../../services/adminApi';
import UserModal from './UserModal';

const ManageUsers = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [showModal, setShowModal] = useState(false);
    const [selectedUser, setSelectedUser] = useState(null);
    const [modalMode, setModalMode] = useState('create');

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            setLoading(true);
            const data = await getAllUsers();
            setUsers(data);
        } catch (err) {
            setError('Failed to fetch users. Please try again later.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleCreateUser = async (userData) => {
        try {
            await saveUser(userData);
            setShowModal(false);
            fetchUsers();
        } catch (err) {
            setError('Failed to create user.');
            console.error(err);
        }
    };

    const handleEditUser = async (userData) => {
        try {
            await updateUserById(selectedUser.userId, userData); // Use `userId` instead of `id`
            setShowModal(false);
            fetchUsers(); // Refresh the user list after update
        } catch (err) {
            setError('Failed to update user.');
            console.error(err);
        }
    };

    const handleDeleteUser = async (userId) => {
        if (window.confirm('Are you sure you want to delete this user?')) {
            try {
                await deleteUserById(userId);
                fetchUsers();
            } catch (err) {
                setError('Failed to delete user.');
                console.error(err);
            }
        }
    };

    const openCreateModal = () => {
        setSelectedUser(null);
        setModalMode('create');
        setShowModal(true);
    };

    const openEditModal = (user) => {
        setSelectedUser(user);
        setModalMode('edit');
        setShowModal(true);
    };

    return (
        <div>
            <h3>Manage Users</h3>
            {error && <Alert variant="danger">{error}</Alert>}
            {loading ? (
                <Spinner animation="border" />
            ) : (
                <div>
                    <Button variant="primary" className="mb-3" onClick={openCreateModal}>
                        Add User
                    </Button>
                    <Table bordered hover>
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>First Name</th>
                                <th>Last Name</th>
                                <th>Email</th>
                                <th>User Type</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {users.map((user, index) => (
                                <tr key={user.userId}> {/* Use `userId` as the key */}
                                    <td>{index + 1}</td>
                                    <td>{user.firstName}</td>
                                    <td>{user.lastName}</td>
                                    <td>{user.email}</td>
                                    <td>{user.usertype}</td>
                                    <td>
                                        <Button
                                            variant="info"
                                            size="sm"
                                            className="me-2"
                                            onClick={() => openEditModal(user)}
                                        >
                                            Edit
                                        </Button>
                                        <Button
                                            variant="danger"
                                            size="sm"
                                            onClick={() => handleDeleteUser(user.userId)}
                                        >
                                            Delete
                                        </Button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                </div>
            )}
            <UserModal
                show={showModal}
                onHide={() => setShowModal(false)}
                onSubmit={modalMode === 'create' ? handleCreateUser : handleEditUser}
                userData={modalMode === 'edit' ? selectedUser : null}
                mode={modalMode}
            />
        </div>
    );
};

export default ManageUsers;
