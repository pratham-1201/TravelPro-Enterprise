package com.travelapp.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Booking Entity - Represents a user's booking of a travel package
 * This will create a 'bookings' table in your database
 */
@Entity
@Table(name = "bookings")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Link to User who made the booking
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // Link to Package being booked
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private TravelPackage travelPackage;
    
    @Column(nullable = false)
    private Integer numberOfTravelers = 1;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(nullable = false)
    private LocalDate travelDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;
    
    @Column(nullable = false)
    private LocalDateTime bookingDate;
    
    @Column(length = 500)
    private String specialRequests;
    
    @Column(length = 50)
    private String contactPhone;
    
    @Column(length = 100)
    private String emergencyContact;
    
    @Column(length = 15)
    private String emergencyPhone;
    
    // Payment related fields
    @Column(precision = 10, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    @Column
    private LocalDateTime paymentDate;
    
    @Column(length = 100)
    private String paymentReference;
    
    // Booking reference number (unique identifier for users)
    @Column(unique = true, length = 20)
    private String bookingReference;
    
    @Column
    private LocalDateTime updatedAt;
    
    // Enums for status
    public enum BookingStatus {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }
    
    public enum PaymentStatus {
        PENDING, PARTIAL, PAID, REFUNDED
    }
    
    // Constructors
    public Booking() {
        this.bookingDate = LocalDateTime.now();
        this.status = BookingStatus.PENDING;
        this.paymentStatus = PaymentStatus.PENDING;
        this.numberOfTravelers = 1;
        this.paidAmount = BigDecimal.ZERO;
    }
    
    public Booking(User user, TravelPackage travelPackage, Integer numberOfTravelers, 
                   LocalDate travelDate, String contactPhone) {
        this();
        this.user = user;
        this.travelPackage = travelPackage;
        this.numberOfTravelers = numberOfTravelers;
        this.travelDate = travelDate;
        this.contactPhone = contactPhone;
        this.totalAmount = travelPackage.getPrice().multiply(BigDecimal.valueOf(numberOfTravelers));
        this.bookingReference = generateBookingReference();
    }
    
    // Generate unique booking reference
    private String generateBookingReference() {
        return "TP" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public TravelPackage getTravelPackage() {
        return travelPackage;
    }
    
    public void setTravelPackage(TravelPackage travelPackage) {
        this.travelPackage = travelPackage;
    }
    
    public Integer getNumberOfTravelers() {
        return numberOfTravelers;
    }
    
    public void setNumberOfTravelers(Integer numberOfTravelers) {
        this.numberOfTravelers = numberOfTravelers;
        // Recalculate total amount when travelers change
        if (this.travelPackage != null) {
            this.totalAmount = this.travelPackage.getPrice().multiply(BigDecimal.valueOf(numberOfTravelers));
        }
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public LocalDate getTravelDate() {
        return travelDate;
    }
    
    public void setTravelDate(LocalDate travelDate) {
        this.travelDate = travelDate;
    }
    
    public BookingStatus getStatus() {
        return status;
    }
    
    public void setStatus(BookingStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getBookingDate() {
        return bookingDate;
    }
    
    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    public String getSpecialRequests() {
        return specialRequests;
    }
    
    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }
    
    public String getContactPhone() {
        return contactPhone;
    }
    
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    
    public String getEmergencyContact() {
        return emergencyContact;
    }
    
    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
    
    public String getEmergencyPhone() {
        return emergencyPhone;
    }
    
    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }
    
    public BigDecimal getPaidAmount() {
        return paidAmount;
    }
    
    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }
    
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public String getPaymentReference() {
        return paymentReference;
    }
    
    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }
    
    public String getBookingReference() {
        return bookingReference;
    }
    
    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Utility methods
    public String getFormattedTotalAmount() {
        return "₹" + String.format("%,.0f", totalAmount);
    }
    
    public String getFormattedPaidAmount() {
        return "₹" + String.format("%,.0f", paidAmount);
    }
    
    public BigDecimal getPendingAmount() {
        return totalAmount.subtract(paidAmount);
    }
    
    public String getFormattedPendingAmount() {
        return "₹" + String.format("%,.0f", getPendingAmount());
    }
    
    public boolean isFullyPaid() {
        return paidAmount.compareTo(totalAmount) >= 0;
    }
    
    public boolean canBeCancelled() {
        return status == BookingStatus.PENDING || status == BookingStatus.CONFIRMED;
    }
    
    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", bookingReference='" + bookingReference + '\'' +
                ", status=" + status +
                ", totalAmount=" + totalAmount +
                ", travelDate=" + travelDate +
                '}';
    }
}