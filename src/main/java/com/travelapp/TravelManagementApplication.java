package com.travelapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is the main entry point of our Travel Management System
 * 
 * @SpringBootApplication tells Spring Boot:
 * - This is a Spring Boot app
 * - Scan for other components in this package
 * - Auto-configure everything based on dependencies in pom.xml
 */
@SpringBootApplication
public class TravelManagementApplication {

    /**
     * Main method - this starts everything
     * When you run this, Spring Boot will:
     * 1. Start an embedded Tomcat web server
     * 2. Set up all the Spring components
     * 3. Make your app available at http://localhost:8080
     */
    public static void main(String[] args) {
        SpringApplication.run(TravelManagementApplication.class, args);
        
        // These print statements will show in console when app starts
        System.out.println("\n🚀 Travel Management System is Running!");
        System.out.println("📍 Open your browser and go to: http://localhost:8080");
        System.out.println("⏹️  Press Ctrl+C to stop the application\n");
    }
}