package com.travelapp.repository;

import com.travelapp.entity.TravelPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * PackageRepository - Database operations for travel packages
 * Spring Boot automatically implements these methods!
 */
@Repository
public interface PackageRepository extends JpaRepository<TravelPackage, Long> {
    
    // Find all active packages
    List<TravelPackage> findByIsActiveTrue();
    
    // Find packages by destination
    List<TravelPackage> findByDestinationContainingIgnoreCase(String destination);
    
    // Find packages by type
    List<TravelPackage> findByPackageType(String packageType);
    
    // Find packages within price range
    List<TravelPackage> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    // Find packages by duration
    List<TravelPackage> findByDurationDays(Integer days);
    List<TravelPackage> findByDurationDaysLessThanEqual(Integer maxDays);
    
    // Find available packages (custom query)
    @Query("SELECT p FROM TravelPackage p WHERE p.isActive = true AND p.currentBookings < p.maxBookings")
    List<TravelPackage> findAvailablePackages();
    
    // Search packages by name or destination
    @Query("SELECT p FROM TravelPackage p WHERE p.isActive = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.destination) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<TravelPackage> searchPackages(@Param("keyword") String keyword);
    
    // Find popular packages (most bookings)
    @Query("SELECT p FROM TravelPackage p WHERE p.isActive = true ORDER BY p.currentBookings DESC")
    List<TravelPackage> findPopularPackages();
    
    // Find packages by multiple criteria
    @Query("SELECT p FROM TravelPackage p WHERE p.isActive = true " +
           "AND (:packageType IS NULL OR p.packageType = :packageType) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "AND (:maxDuration IS NULL OR p.durationDays <= :maxDuration)")
    List<TravelPackage> findPackagesWithFilters(@Param("packageType") String packageType,
                                         @Param("maxPrice") BigDecimal maxPrice,
                                         @Param("maxDuration") Integer maxDuration);
}