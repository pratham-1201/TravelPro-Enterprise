package com.travelapp.repository;

import com.travelapp.entity.User;
import com.travelapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * UserRepository - handles all database operations for User entity
 * 
 * @Repository tells Spring this is a database access class
 * JpaRepository<User, Long> gives us FREE methods:
 * - save() - saves a user
 * - findById() - finds user by ID
 * - findAll() - gets all users
 * - deleteById() - deletes a user
 * - count() - counts total users
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by email - useful for login
     * Spring Boot automatically creates the SQL query from the method name!
     * Method name "findByEmail" becomes "SELECT * FROM users WHERE email = ?"
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if email already exists - useful for registration validation
     * Method name "existsByEmail" becomes "SELECT COUNT(*) FROM users WHERE email = ?"
     */
    boolean existsByEmail(String email);
    
    /**
     * Find all users with a specific role
     * Method name "findByRole" becomes "SELECT * FROM users WHERE role = ?"
     */
    List<User> findByRole(Role role);
    
    /**
     * Find users by name containing some text (case insensitive)
     * Useful for search functionality
     * "findByNameContainingIgnoreCase" becomes 
     * "SELECT * FROM users WHERE LOWER(name) LIKE LOWER('%searchText%')"
     */
    List<User> findByNameContainingIgnoreCase(String name);
}