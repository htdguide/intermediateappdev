import React from 'react';
import { Link } from 'react-router-dom';
import { Navbar, Nav, Container } from 'react-bootstrap';

function Header({ user, logout }) {
  return (
    <Navbar bg="dark" variant="dark" expand="lg" sticky="top">
      <Container>
        <Navbar.Brand as={Link} to="/">QuizApp</Navbar.Brand>

        <Navbar.Toggle aria-controls="basic-navbar-nav" />

        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="ml-auto">
            <Nav.Item>
              <Link to="/" className="nav-link">Home</Link>
            </Nav.Item>
            {user ? (
              <>
                <Nav.Item>
                  <Link to="/user" className="nav-link">User</Link>
                </Nav.Item>
                <Nav.Item>
                  <Link to="/" onClick={logout} className="nav-link">Logout</Link>
                </Nav.Item>
              </>
            ) : (
              <Nav.Item>
                <Link to="/login" className="nav-link">Login</Link>
              </Nav.Item>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default Header;
