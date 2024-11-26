import React from 'react';
import { NavLink } from 'react-router-dom';
import { Navbar, Nav, Container } from 'react-bootstrap';

function Header({ user, logout }) {
  return (
    <Navbar bg="dark" variant="dark" expand="lg" sticky="top">
      <Container>
        <Navbar.Brand as={NavLink} to="/">QuizApp</Navbar.Brand>

        <Navbar.Toggle aria-controls="basic-navbar-nav" />

        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="ml-auto">
            <Nav.Item>
              <NavLink to="/" className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
                Home
              </NavLink>
            </Nav.Item>
            {user ? (
              <>
                {user.usertype === 'PLAYER' && (
                  <Nav.Item>
                    <NavLink to="/user" className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
                      User
                    </NavLink>
                  </Nav.Item>
                )}
                {user.usertype === 'ADMIN' && (
                  <Nav.Item>
                    <NavLink to="/admin" className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
                      Admin
                    </NavLink>
                  </Nav.Item>
                )}
                <Nav.Item>
                  <NavLink
                    to="/"
                    onClick={(e) => {
                      e.preventDefault();
                      logout();
                    }}
                    className="nav-link"
                  >
                    Logout
                  </NavLink>
                </Nav.Item>
              </>
            ) : (
              <>
                <Nav.Item>
                  <NavLink to="/login" className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
                    Login
                  </NavLink>
                </Nav.Item>
                <Nav.Item>
                  <NavLink to="/register" className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
                    Register
                  </NavLink>
                </Nav.Item>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default Header;
