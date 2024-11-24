import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ user, allowedRoles, redirectPath = '/login', children }) => {
    if (!user) {
        return <Navigate to={redirectPath} />;
    }

    if (allowedRoles && !allowedRoles.includes(user.usertype)) {
        return <Navigate to="/" />;
    }

    return children;
};

export default ProtectedRoute;
