package com.travelapp.controller;

import com.travelapp.entity.User;
import com.travelapp.entity.Role;
import com.travelapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;

/**
 * UserController - for testing our User and UserRepository
 * @RestController means this returns data (JSON) instead of HTML pages
 */
@RestController
public class UserController {
    
    /**
     * @Autowired tells Spring: "Give me an instance of UserRepository"
     * Spring automatically creates the repository and injects it here
     */
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Test endpoint to create some sample users
     * Visit: http://localhost:8080/test-users
     */
    @GetMapping("/test-users")
    public String createTestUsers() {
        // Create sample users
        User user1 = new User("John Doe", "john@example.com", "password123");
        user1.setBirthdate(LocalDate.of(1990, 5, 15));
        user1.setPhone("9876543210");
        
        User user2 = new User("Jane Smith", "jane@example.com", "password456");
        user2.setBirthdate(LocalDate.of(1985, 8, 22));
        user2.setRole(Role.ADMIN);
        user2.setPhone("9876543211");
        
        User user3 = new User("Raj Patel", "raj@example.com", "password789");
        user3.setBirthdate(LocalDate.of(1992, 3, 10));
        user3.setPhone("9876543212");
        user3.setAadharNumber("1234-5678-9012");
        
        // Save to database
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        
        return "✅ Created 3 test users! Check H2 console to see them in database.";
    }
    
    /**
     * Get all users
     * Visit: http://localhost:8080/users
     */
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Get total user count
     * Visit: http://localhost:8080/user-count
     */
    @GetMapping("/user-count")
    public String getUserCount() {
        long count = userRepository.count();
        return "Total users in database: " + count;
    }
}