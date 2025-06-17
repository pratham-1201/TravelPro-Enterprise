package com.travelapp.repository;

import com.travelapp.entity.Payment;
import com.travelapp.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * PaymentRepository - Database operations for payments
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    // Find payments by booking
    List<Payment> findByBooking(Booking booking);
    List<Payment> findByBookingOrderByPaymentDateDesc(Booking booking);
    
    // Find payment by transaction ID
    Optional<Payment> findByTransactionId(String transactionId);
    Optional<Payment> findByGatewayTransactionId(String gatewayTransactionId);
    
    // Find payments by status
    List<Payment> findByStatus(Payment.PaymentStatus status);
    
    // Find payments by method
    List<Payment> findByPaymentMethod(Payment.PaymentMethod paymentMethod);
    
    // Find payments by date range
    List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find successful payments for a booking
    @Query("SELECT p FROM Payment p WHERE p.booking = :booking AND p.status = 'SUCCESS' ORDER BY p.paymentDate DESC")
    List<Payment> findSuccessfulPaymentsByBooking(@Param("booking") Booking booking);
    
    // Calculate total paid amount for a booking
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.booking = :booking AND p.status = 'SUCCESS'")
    BigDecimal calculateTotalPaidForBooking(@Param("booking") Booking booking);
    
    // Find pending payments (for cleanup/retry)
    @Query("SELECT p FROM Payment p WHERE p.status IN ('PENDING', 'PROCESSING') AND p.paymentDate < :cutoffTime")
    List<Payment> findStalePayments(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    // Revenue calculations
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'SUCCESS'")
    BigDecimal calculateTotalRevenue();
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'SUCCESS' AND p.paymentDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateRevenueForPeriod(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);
    
    // Payment statistics
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = 'SUCCESS' AND p.paymentDate BETWEEN :startDate AND :endDate")
    Long countSuccessfulPaymentsForPeriod(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    // Find recent payments (for admin dashboard)
    @Query("SELECT p FROM Payment p ORDER BY p.paymentDate DESC")
    List<Payment> findRecentPayments();
}