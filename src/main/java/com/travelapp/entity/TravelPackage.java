package com.travelapp.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Package Entity - Represents a travel package/destination
 * This will create a 'packages' table in your database
 */
@Entity
@Table(name = "packages")
public class TravelPackage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, length = 100)
    private String destination;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(nullable = false)
    private Integer durationDays;
    
    @Column(length = 50)
    private String packageType; // ADVENTURE, LEISURE, BUSINESS, HONEYMOON, FAMILY
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @Column
    private String imageUrl;
    
    @Column
    private LocalDate availableFrom;
    
    @Column
    private LocalDate availableTo;
    
    @Column
    private Integer maxBookings = 50;
    
    @Column
    private Integer currentBookings = 0;
    
    @Column(columnDefinition = "TEXT")
    private String inclusions; // What's included in the package
    
    @Column(columnDefinition = "TEXT")
    private String exclusions; // What's not included
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime updatedAt;
    
    // Constructors
    public TravelPackage() {
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
        this.currentBookings = 0;
    }
    
    public TravelPackage(String name, String destination, String description, 
                   BigDecimal price, Integer durationDays, String packageType) {
        this();
        this.name = name;
        this.destination = destination;
        this.description = description;
        this.price = price;
        this.durationDays = durationDays;
        this.packageType = packageType;
    }
    
    // Getters and Setters
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
    
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getDurationDays() {
        return durationDays;
    }
    
    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }
    
    public String getPackageType() {
        return packageType;
    }
    
    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public LocalDate getAvailableFrom() {
        return availableFrom;
    }
    
    public void setAvailableFrom(LocalDate availableFrom) {
        this.availableFrom = availableFrom;
    }
    
    public LocalDate getAvailableTo() {
        return availableTo;
    }
    
    public void setAvailableTo(LocalDate availableTo) {
        this.availableTo = availableTo;
    }
    
    public Integer getMaxBookings() {
        return maxBookings;
    }
    
    public void setMaxBookings(Integer maxBookings) {
        this.maxBookings = maxBookings;
    }
    
    public Integer getCurrentBookings() {
        return currentBookings;
    }
    
    public void setCurrentBookings(Integer currentBookings) {
        this.currentBookings = currentBookings;
    }
    
    public String getInclusions() {
        return inclusions;
    }
    
    public void setInclusions(String inclusions) {
        this.inclusions = inclusions;
    }
    
    public String getExclusions() {
        return exclusions;
    }
    
    public void setExclusions(String exclusions) {
        this.exclusions = exclusions;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Utility methods
    public boolean isAvailable() {
        LocalDate today = LocalDate.now();
        return isActive && 
               currentBookings < maxBookings &&
               (availableFrom == null || !today.isBefore(availableFrom)) &&
               (availableTo == null || !today.isAfter(availableTo));
    }
    
    public int getAvailableSlots() {
        return maxBookings - currentBookings;
    }
    
    public String getFormattedPrice() {
        return "₹" + String.format("%,.0f", price);
    }
    
    @Override
    public String toString() {
        return "TravelPackage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", destination='" + destination + '\'' +
                ", price=" + price +
                ", durationDays=" + durationDays +
                ", packageType='" + packageType + '\'' +
                '}';
    }
}