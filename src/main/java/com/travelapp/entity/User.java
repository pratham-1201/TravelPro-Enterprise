package com.travelapp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * User entity - represents a user in our database
 * 
 * @Entity tells Spring: "Create a database table for this class"
 * @Table(name = "users") tells Spring: "Call the table 'users'"
 */
@Entity
@Table(name = "users")
public class User {
    
    /**
     * Primary key - unique identifier for each user
     * @Id = This is the primary key
     * @GeneratedValue = Database auto-generates the ID (1, 2, 3...)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * User's full name
     * @Column(nullable = false) = This field is required (cannot be empty)
     */
    @Column(nullable = false)
    private String name;
    
    /**
     * Email address - used for login
     * unique = true means no two users can have the same email
     */
    @Column(nullable = false, unique = true)
    private String email;
    
    /**
     * Password for authentication
     * In real apps, this should be encrypted/hashed
     */
    @Column(nullable = false)
    private String password;
    
    /**
     * Date of birth - better than storing age (which changes every year)
     */
    private LocalDate birthdate;
    
    /**
     * Phone number for contact
     */
    private String phone;
    
    /**
     * Aadhar number - optional for Indian users
     * nullable = true means this can be left empty
     */
    @Column(nullable = true)
    private String aadharNumber;
    
    /**
     * User role (CUSTOMER, ADMIN, etc.)
     * @Enumerated(EnumType.STRING) stores the enum as text in database
     */
    @Enumerated(EnumType.STRING)
    private Role role;
    
    /**
     * When the user account was created
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * Default constructor - required by JPA
     * JPA needs this to create User objects from database data
     */
    public User() {
        this.createdAt = LocalDateTime.now();  // Set creation time to now
        this.role = Role.CUSTOMER;             // Default role is customer
    }
    
    /**
     * Constructor with parameters - useful for creating new users
     */
    public User(String name, String email, String password) {
        this();  // Call the default constructor first
        this.name = name;
        this.email = email;
        this.password = password;
    }
    
    // =====================
    // GETTERS AND SETTERS
    // =====================
    // These methods allow other classes to read and modify the user data
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public LocalDate getBirthdate() {
        return birthdate;
    }
    
    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAadharNumber() {
        return aadharNumber;
    }
    
    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * toString method - useful for debugging
     * Shows user info when you print a User object
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", createdAt=" + createdAt +
                '}';
    }
}