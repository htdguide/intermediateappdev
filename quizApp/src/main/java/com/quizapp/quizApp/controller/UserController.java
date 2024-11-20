package com.quizapp.quizApp.controller;

import com.quizapp.quizApp.model.User;
import com.quizapp.quizApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get a user by email
    @GetMapping("/email/{email}")
    public Optional<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    // Get a user by ID
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Get a user by first and last name
    @GetMapping("/name")
    public Optional<User> getUserByFirstAndLastName(
            @RequestParam String firstName,
            @RequestParam String lastName) {
        return userService.getUserByFirstAndLastName(firstName, lastName);
    }

    // Create or update a user
    @PostMapping
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // Update a user by ID
    @PutMapping("/{id}")
    public Optional<User> updateUserById(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUserById(id, updatedUser);
    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}
