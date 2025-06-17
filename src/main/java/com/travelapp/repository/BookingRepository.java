package com.travelapp.repository;

import com.travelapp.entity.Booking;
import com.travelapp.entity.User;
import com.travelapp.entity.TravelPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * BookingRepository - Database operations for bookings
 * Spring Boot automatically implements these methods!
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    // Find bookings by user
    List<Booking> findByUser(User user);
    List<Booking> findByUserOrderByBookingDateDesc(User user);
    
    // Find bookings by package
    List<Booking> findByTravelPackage(TravelPackage travelPackage);
    
    // Find booking by reference number
    Optional<Booking> findByBookingReference(String bookingReference);
    
    // Find bookings by status
    List<Booking> findByStatus(Booking.BookingStatus status);
    
    // Find bookings by travel date
    List<Booking> findByTravelDate(LocalDate travelDate);
    List<Booking> findByTravelDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Find upcoming bookings for a user
    @Query("SELECT b FROM Booking b WHERE b.user = :user AND b.travelDate >= :today ORDER BY b.travelDate ASC")
    List<Booking> findUpcomingBookings(@Param("user") User user, @Param("today") LocalDate today);
    
    // Find past bookings for a user
    @Query("SELECT b FROM Booking b WHERE b.user = :user AND b.travelDate < :today ORDER BY b.travelDate DESC")
    List<Booking> findPastBookings(@Param("user") User user, @Param("today") LocalDate today);
    
    // Count bookings by package (for availability)
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.travelPackage = :package AND b.status IN ('PENDING', 'CONFIRMED')")
    Long countActiveBookingsByPackage(@Param("package") TravelPackage travelPackage);
    
    // Find recent bookings (for admin dashboard)
    @Query("SELECT b FROM Booking b ORDER BY b.bookingDate DESC")
    List<Booking> findRecentBookings();
    
    // Find bookings by payment status
    List<Booking> findByPaymentStatus(Booking.PaymentStatus paymentStatus);
    
    // Find bookings that need payment reminder
    @Query("SELECT b FROM Booking b WHERE b.paymentStatus = 'PENDING' AND b.bookingDate < :cutoffDate")
    List<Booking> findBookingsNeedingPaymentReminder(@Param("cutoffDate") LocalDate cutoffDate);
    
    // Revenue calculations
    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.status = 'CONFIRMED'")
    BigDecimal calculateTotalRevenue();
    
    @Query("SELECT SUM(b.paidAmount) FROM Booking b")
    BigDecimal calculateTotalPaidAmount();
}