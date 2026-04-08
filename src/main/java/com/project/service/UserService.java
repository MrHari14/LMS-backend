package com.project.service;

import com.project.entity.User;
import com.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User login(String email, String name, String password, User.UserRole role) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Email and password are required");
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User existing = userOpt.get();
            System.out.println("User found: " + email + " with existing role: " + existing.getRole());
            if (!passwordEncoder.matches(password, existing.getPassword())) {
                throw new IllegalArgumentException("Invalid email or password");
            }
            if (role != null) {
                System.out.println("Updating user role from " + existing.getRole() + " to " + role);
                existing.setRole(role);
            }
            existing.setLoginCount(existing.getLoginCount() + 1);
            User saved = userRepository.save(existing);
            System.out.println("User saved with role: " + saved.getRole());
            return saved;
        }

        System.out.println("New user - creating with email: " + email + " and role: " + (role != null ? role : User.UserRole.STUDENT));
        User user = new User(name, email, passwordEncoder.encode(password), role != null ? role : User.UserRole.STUDENT);
        user.setActive(true);
        user.setLoginCount(1);
        User saved = userRepository.save(user);
        System.out.println("New user saved with role: " + saved.getRole());
        return saved;
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void recordLogin(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLoginCount(user.getLoginCount() + 1);
            userRepository.save(user);
        }
    }
}