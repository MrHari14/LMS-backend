package com.project.Controller;

import com.project.entity.User;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:5175"})
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            user.setRole(userDetails.getRole());
            user.setActive(userDetails.getActive());
            return ResponseEntity.ok(userService.updateUser(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Login attempt: " + (loginRequest != null ? loginRequest.getEmail() : "null request"));
        
        if (loginRequest == null || loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.status(400).body("Email and password are required.");
        }
        try {
            String email = loginRequest.getEmail().trim();
            String password = loginRequest.getPassword();
            User user = userService.login(email, loginRequest.getName(), password, loginRequest.getRole());
            System.out.println("Login successful for: " + email);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException ex) {
            System.out.println("Login failed: " + ex.getMessage());
            return ResponseEntity.status(401).body(ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Login error: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Login failed: " + ex.getMessage());
        }
    }

    public static class LoginRequest {
        private String name;
        private String email;
        private String password;
        private User.UserRole role;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public User.UserRole getRole() { return role; }
        public void setRole(User.UserRole role) { this.role = role; }
    }}