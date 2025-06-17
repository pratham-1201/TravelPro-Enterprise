package com.travelapp.entity;

/**
 * Enum defining different types of users in our travel management system
 * 
 * Using enum ensures only these specific roles are allowed:
 * - No typos like "customer" vs "Customer" 
 * - No invalid roles like "hacker" or "pizza"
 * - Type-safe - compiler catches mistakes
 */
public enum Role {
    CUSTOMER,       // Regular users who book travel packages
    ADMIN,          // System administrators with full access
    TRAVEL_AGENT,   // Agents who can create and manage packages
    HOTEL_PARTNER   // Hotel partners who manage their property info
}