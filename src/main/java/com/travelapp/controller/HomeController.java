package com.travelapp.controller;

import com.travelapp.entity.Payment;
import com.travelapp.repository.PaymentRepository;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.travelapp.entity.User;
import com.travelapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import com.travelapp.entity.TravelPackage;
import com.travelapp.repository.PackageRepository;
import java.math.BigDecimal;
import com.travelapp.entity.Booking;
import com.travelapp.repository.BookingRepository;
import java.util.Optional;

/**
 * HomeController handles the main pages of our website
 * 
 * @Controller tells Spring this class handles web requests
 */
@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;
    // Add this injection with your other @Autowired
    @Autowired
    private PackageRepository packageRepository;
    
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;
    /**
     * This method handles requests to the home page "/"
     * 
     * @GetMapping("/") means: when someone visits http://localhost:8080/
     * this method will run
     * 
     * @param model - Spring gives us this to pass data to the web page
     * @return - the name of the HTML template to show
     */
    @GetMapping("/")
    public String home(Model model) {
        // Add data that the web page can use
        model.addAttribute("title", "Travel Management System");
        model.addAttribute("message", "Welcome to your Travel Booking Platform!");
        model.addAttribute("features", new String[]{
            "Browse Amazing Destinations", 
            "Book Travel Packages", 
            "Manage Your Bookings",
            "24/7 Customer Support"
        });
        // Return the name of the HTML template (home.html)
        // Spring will look for this in src/main/resources/templates/
        return "home";
    }
    
    /**
     * About page
     */
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About Us");
        model.addAttribute("description", "We make travel planning easy and affordable!");
        return "about";
    }
    
    /**
 * Show registration form
 * Visit: http://localhost:8080/register
 */
@GetMapping("/register")
public String showRegisterForm(Model model) {
    return "register";
}

/**
 * Handle registration form submission
 * This method runs when user clicks "Create Account"
 */
@PostMapping("/register")
public String registerUser(
        @RequestParam String name,
        @RequestParam String email,
        @RequestParam String password,
        @RequestParam(required = false) String phone,
        @RequestParam(required = false) LocalDate birthdate,
        @RequestParam(required = false) String aadharNumber,
        Model model) {
    
    try {
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "❌ Email already registered! Please use a different email or try logging in.");
            return "register";
        }
        
        // Validate input
        if (name.trim().length() < 2) {
            model.addAttribute("error", "❌ Name must be at least 2 characters long.");
            return "register";
        }
        
        if (password.length() < 6) {
            model.addAttribute("error", "❌ Password must be at least 6 characters long.");
            return "register";
        }
        
        // Create new user
        User newUser = new User(name.trim(), email.toLowerCase(), password);
        
        // Set optional fields if provided
        if (phone != null && !phone.trim().isEmpty()) {
            newUser.setPhone(phone.trim());
        }
        
        if (birthdate != null) {
            newUser.setBirthdate(birthdate);
        }
        
        if (aadharNumber != null && !aadharNumber.trim().isEmpty()) {
            newUser.setAadharNumber(aadharNumber.trim());
        }
        
        // Save to database
        userRepository.save(newUser);
        
        // Success message
        model.addAttribute("success", "✅ Account created successfully! Redirecting to login...");
        return "register";
        
    } catch (Exception e) {
        model.addAttribute("error", "❌ Something went wrong. Please try again.");
        return "register";
    }
    }

/**
 * Show login form
 * Visit: http://localhost:8080/login
 */
@GetMapping("/login")
public String showLoginForm(Model model) {
    return "login";
}
/**
 * Handle login form submission
 * This method runs when user clicks "Sign In"
 */
@PostMapping("/login")
public String loginUser(
        @RequestParam String email,
        @RequestParam String password,
        HttpSession session,
        Model model) {
    
    try {
        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(email.toLowerCase().trim());
        
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "❌ No account found with this email address.");
            model.addAttribute("email", email); // Keep email in form
            return "login";
        }
        
        User user = userOptional.get();
        
        // Check password (In real app, you'd compare hashed passwords)
        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "❌ Incorrect password. Please try again.");
            model.addAttribute("email", email); // Keep email in form
            return "login";
        }
        
        // Login successful - create session
        session.setAttribute("loggedInUser", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userRole", user.getRole().toString());
        
        // Redirect based on role
        if (user.getRole().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/dashboard";
        }
        
    } catch (Exception e) {
        System.out.println("Login error: " + e.getMessage());
        e.printStackTrace();
        
        model.addAttribute("error", "❌ Something went wrong. Please try again.");
        return "login";
    }
}

/**
 * User Dashboard - only accessible after login
 */
@GetMapping("/dashboard")
public String userDashboard(HttpSession session, Model model) {
    // Check if user is logged in
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    
    if (loggedInUser == null) {
        // Not logged in - redirect to login
        return "redirect:/login";
    }
    
    // User is logged in - show dashboard
    model.addAttribute("user", loggedInUser);
    model.addAttribute("title", "Dashboard - " + loggedInUser.getName());
    
    // You can add more data here later (real bookings, stats, etc.)
    // For now, the template shows demo data
    
    return "dashboard";
}
/**
 * Create sample packages for testing
 * Visit: http://localhost:8080/test-packages
 */
@GetMapping("/test-packages")
public String createTestPackages() {
    // Check if packages already exist
    if (packageRepository.count() > 0) {
        return "redirect:/packages?message=Packages already exist";
    }
    
    try {
        // Create sample packages
        TravelPackage goa = new TravelPackage(
            "Goa Beach Paradise",
            "Goa",
            "Experience the pristine beaches of Goa with crystal clear waters, golden sand, and vibrant nightlife. Perfect for relaxation and adventure!",
            new BigDecimal("15000"),
            5,
            "LEISURE"
        );
        goa.setImageUrl("https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?w=800&h=600&fit=crop");
        goa.setInclusions("• 4 nights accommodation\n• Daily breakfast\n• Airport transfers\n• Sightseeing tours\n• Beach activities");
        goa.setExclusions("• Lunch and dinner\n• Personal expenses\n• Travel insurance\n• Tips and gratuities");
        goa.setAvailableFrom(LocalDate.now());
        goa.setAvailableTo(LocalDate.now().plusMonths(6));
        goa.setMaxBookings(30);
        
        TravelPackage manali = new TravelPackage(
            "Manali Hill Station Adventure",
            "Manali",
            "Escape to the snow-capped mountains of Manali. Perfect for adventure seekers with trekking, paragliding, and scenic beauty.",
            new BigDecimal("12000"),
            4,
            "ADVENTURE"
        );
        manali.setImageUrl("https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop");
        manali.setInclusions("• 3 nights accommodation\n• All meals included\n• Adventure activities\n• Local sightseeing\n• Professional guide");
        manali.setExclusions("• Transportation to Manali\n• Personal gear\n• Emergency evacuation\n• Extra activities");
        manali.setAvailableFrom(LocalDate.now());
        manali.setAvailableTo(LocalDate.now().plusMonths(4));
        manali.setMaxBookings(25);
        
        TravelPackage kerala = new TravelPackage(
            "Kerala Backwaters Bliss",
            "Kerala",
            "Cruise through the serene backwaters of Kerala in traditional houseboats. Experience God's Own Country in all its glory.",
            new BigDecimal("18000"),
            6,
            "LEISURE"
        );
        kerala.setImageUrl("https://images.unsplash.com/photo-1602216056096-3b40cc0c9944?w=800&h=600&fit=crop");
        kerala.setInclusions("• 5 nights accommodation\n• Houseboat cruise\n• All meals included\n• Ayurvedic spa session\n• Cultural performances");
        kerala.setExclusions("• Flight tickets\n• Shopping expenses\n• Extra excursions\n• Alcohol and beverages");
        kerala.setAvailableFrom(LocalDate.now());
        kerala.setAvailableTo(LocalDate.now().plusMonths(8));
        kerala.setMaxBookings(20);
        
        TravelPackage rajasthan = new TravelPackage(
            "Royal Rajasthan Heritage",
            "Rajasthan",
            "Explore the majestic palaces, forts, and desert landscapes of Rajasthan. A journey through India's royal history.",
            new BigDecimal("22000"),
            7,
            "FAMILY"
        );
        rajasthan.setImageUrl("https://images.unsplash.com/photo-1477587458883-47145ed94245?w=800&h=600&fit=crop");
        rajasthan.setInclusions("• 6 nights accommodation\n• Palace hotel stay\n• Camel safari\n• Cultural shows\n• Heritage site visits\n• Professional guide");
        rajasthan.setExclusions("• International flights\n• Visa fees\n• Personal shopping\n• Tips for drivers/guides");
        rajasthan.setAvailableFrom(LocalDate.now());
        rajasthan.setAvailableTo(LocalDate.now().plusMonths(10));
        rajasthan.setMaxBookings(35);
        
        TravelPackage kashmir = new TravelPackage(
            "Kashmir Valley Paradise",
            "Kashmir",
            "Experience the breathtaking beauty of Kashmir - the paradise on earth with Dal Lake, gardens, and snow-capped mountains.",
            new BigDecimal("20000"),
            5,
            "HONEYMOON"
        );
        kashmir.setImageUrl("https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop");
        kashmir.setInclusions("• 4 nights accommodation\n• Houseboat stay on Dal Lake\n• Shikara rides\n• Garden visits\n• Local sightseeing");
        kashmir.setExclusions("• Airfare\n• Personal expenses\n• Winter clothing\n• Extra activities");
        kashmir.setAvailableFrom(LocalDate.now());
        kashmir.setAvailableTo(LocalDate.now().plusMonths(5));
        kashmir.setMaxBookings(15);
        
        TravelPackage himachal = new TravelPackage(
            "Shimla Kullu Adventure",
            "Himachal Pradesh",
            "Discover the queen of hills - Shimla and the valley of gods - Kullu. Perfect blend of colonial charm and natural beauty.",
            new BigDecimal("14000"),
            5,
            "ADVENTURE"
        );
        himachal.setImageUrl("https://images.unsplash.com/photo-1544735716-392fe2489ffa?w=800&h=600&fit=crop");
        himachal.setInclusions("• 4 nights accommodation\n• Toy train ride\n• Adventure sports\n• Local sightseeing\n• Traditional meals");
        himachal.setExclusions("• Travel to Shimla\n• Personal gear\n• Extra meals\n• Shopping");
        himachal.setAvailableFrom(LocalDate.now());
        himachal.setAvailableTo(LocalDate.now().plusMonths(6));
        himachal.setMaxBookings(40);
        
        // Save all packages
        packageRepository.save(goa);
        packageRepository.save(manali);
        packageRepository.save(kerala);
        packageRepository.save(rajasthan);
        packageRepository.save(kashmir);
        packageRepository.save(himachal);
        
        System.out.println("✅ Sample packages created successfully!");
        
    } catch (Exception e) {
        System.out.println("❌ Error creating packages: " + e.getMessage());
        e.printStackTrace();
    }
    
    return "redirect:/packages?message=Sample packages created successfully!";
}

/**
 * Browse all packages
 * This makes the "Browse Packages" button work!
 */
@GetMapping("/packages")
public String browsePackages(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String message,
        Model model) {
    
    try {
        List<TravelPackage> packages;
        
        // Apply search and filters
        if (search != null && !search.trim().isEmpty()) {
            packages = packageRepository.searchPackages(search.trim());
            model.addAttribute("searchTerm", search.trim());
        } else if (type != null && !type.trim().isEmpty()) {
            packages = packageRepository.findByPackageType(type.trim());
            model.addAttribute("selectedType", type.trim());
        } else {
            packages = packageRepository.findByIsActiveTrue();
        }
        
        model.addAttribute("packages", packages);
        model.addAttribute("totalPackages", packages.size());
        
        // Package types for filter dropdown
        model.addAttribute("packageTypes", new String[]{"ADVENTURE", "LEISURE", "FAMILY", "HONEYMOON", "BUSINESS"});
        
        if (message != null) {
            model.addAttribute("message", message);
        }
        
        model.addAttribute("title", "Browse Travel Packages");
        
    } catch (Exception e) {
        System.out.println("❌ Error loading packages: " + e.getMessage());
        model.addAttribute("error", "Failed to load packages. Please try again.");
    }
    
    return "packages";
}

/**
 * Package Details Page
 * Visit: http://localhost:8080/packages/1 (or any package ID)
 */
@GetMapping("/packages/{id}")
public String packageDetails(@PathVariable Long id, HttpSession session, Model model) {
    try {
        // Find the package
        Optional<TravelPackage> packageOptional = packageRepository.findById(id);
        
        if (packageOptional.isEmpty()) {
            model.addAttribute("error", "Package not found");
            return "redirect:/packages";
        }
        
        TravelPackage travelPackage = packageOptional.get();
        
        // Check if user is logged in (for booking button)
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        boolean isLoggedIn = loggedInUser != null;
        
        model.addAttribute("package", travelPackage);
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("title", travelPackage.getName() + " - Package Details");
        
        // Add related packages (same destination or type)
        List<TravelPackage> relatedPackages = packageRepository
            .findByDestinationContainingIgnoreCase(travelPackage.getDestination())
            .stream()
            .filter(p -> !p.getId().equals(id)) // Exclude current package
            .limit(3) // Show max 3 related packages
            .toList();
        
        model.addAttribute("relatedPackages", relatedPackages);
        
        return "package-details";
        
    } catch (Exception e) {
        System.out.println("❌ Error loading package details: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("error", "Failed to load package details");
        return "redirect:/packages";
    }
}


/**
 * Show booking form
 * Visit: http://localhost:8080/packages/1/book
 */
@GetMapping("/packages/{id}/book")
public String showBookingForm(@PathVariable Long id, HttpSession session, Model model) {
    // Check if user is logged in
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        return "redirect:/login?message=Please login to book packages";
    }
    
    try {
        // Find the package
        Optional<TravelPackage> packageOptional = packageRepository.findById(id);
        
        if (packageOptional.isEmpty()) {
            model.addAttribute("error", "Package not found");
            return "redirect:/packages";
        }
        
        TravelPackage travelPackage = packageOptional.get();
        
        // Check if package is available
        if (!travelPackage.isAvailable()) {
            model.addAttribute("error", "This package is no longer available");
            return "redirect:/packages/" + id;
        }
        
        model.addAttribute("package", travelPackage);
        model.addAttribute("user", loggedInUser);
        model.addAttribute("title", "Book " + travelPackage.getName());
        
        // Set minimum travel date (tomorrow)
        model.addAttribute("minDate", LocalDate.now().plusDays(1));
        
        return "booking-form";
        
    } catch (Exception e) {
        System.out.println("❌ Error loading booking form: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("error", "Failed to load booking form");
        return "redirect:/packages";
    }
}

/**
 * Process booking form submission
 */
@PostMapping("/packages/{id}/book")
public String processBooking(
        @PathVariable Long id,
        @RequestParam Integer numberOfTravelers,
        @RequestParam LocalDate travelDate,
        @RequestParam String contactPhone,
        @RequestParam(required = false) String specialRequests,
        @RequestParam(required = false) String emergencyContact,
        @RequestParam(required = false) String emergencyPhone,
        HttpSession session,
        Model model) {
    
    // Check if user is logged in
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        return "redirect:/login";
    }
    
    try {
        // Find the package
        Optional<TravelPackage> packageOptional = packageRepository.findById(id);
        
        if (packageOptional.isEmpty()) {
            model.addAttribute("error", "Package not found");
            return "redirect:/packages";
        }
        
        TravelPackage travelPackage = packageOptional.get();
        
        // Validation
        if (numberOfTravelers < 1 || numberOfTravelers > 10) {
            model.addAttribute("error", "Number of travelers must be between 1 and 10");
            model.addAttribute("package", travelPackage);
            model.addAttribute("user", loggedInUser);
            return "booking-form";
        }
        
        if (travelDate.isBefore(LocalDate.now().plusDays(1))) {
            model.addAttribute("error", "Travel date must be at least 1 day from today");
            model.addAttribute("package", travelPackage);
            model.addAttribute("user", loggedInUser);
            return "booking-form";
        }
        
        // Check availability
        if (travelPackage.getAvailableSlots() < numberOfTravelers) {
            model.addAttribute("error", "Not enough slots available. Only " + 
                             travelPackage.getAvailableSlots() + " slots remaining");
            model.addAttribute("package", travelPackage);
            model.addAttribute("user", loggedInUser);
            return "booking-form";
        }
        
        // Create booking
        Booking booking = new Booking(loggedInUser, travelPackage, numberOfTravelers, 
                                    travelDate, contactPhone);
        
        if (specialRequests != null && !specialRequests.trim().isEmpty()) {
            booking.setSpecialRequests(specialRequests.trim());
        }
        
        if (emergencyContact != null && !emergencyContact.trim().isEmpty()) {
            booking.setEmergencyContact(emergencyContact.trim());
            booking.setEmergencyPhone(emergencyPhone != null ? emergencyPhone.trim() : "");
        }
        
        // Save booking
        Booking savedBooking = bookingRepository.save(booking);
        
        // Update package booking count
        travelPackage.setCurrentBookings(travelPackage.getCurrentBookings() + numberOfTravelers);
        packageRepository.save(travelPackage);
        
        System.out.println("✅ Booking created: " + savedBooking.getBookingReference());
        
        // Redirect to booking confirmation
        return "redirect:/bookings/" + savedBooking.getId() + "?success=true";
        
    } catch (Exception e) {
        System.out.println("❌ Error processing booking: " + e.getMessage());
        e.printStackTrace();
        
        model.addAttribute("error", "Failed to process booking: " + e.getMessage());
        return "redirect:/packages/" + id;
    }
}

/**
 * Show user's bookings
 */
@GetMapping("/bookings")
public String userBookings(HttpSession session, Model model) {
    // Check if user is logged in
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        return "redirect:/login";
    }
    
    try {
        // Get user's bookings
        List<Booking> allBookings = bookingRepository.findByUserOrderByBookingDateDesc(loggedInUser);
        List<Booking> upcomingBookings = bookingRepository.findUpcomingBookings(loggedInUser, LocalDate.now());
        List<Booking> pastBookings = bookingRepository.findPastBookings(loggedInUser, LocalDate.now());
        
        model.addAttribute("allBookings", allBookings);
        model.addAttribute("upcomingBookings", upcomingBookings);
        model.addAttribute("pastBookings", pastBookings);
        model.addAttribute("user", loggedInUser);
        model.addAttribute("title", "My Bookings");
        
        return "user-bookings";
        
    } catch (Exception e) {
        System.out.println("❌ Error loading bookings: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("error", "Failed to load bookings");
        return "dashboard";
    }
}

/**
 * Show specific booking details
 */
@GetMapping("/bookings/{id}")
public String bookingDetails(@PathVariable Long id, 
                           @RequestParam(required = false) String success,
                           HttpSession session, Model model) {
    // Check if user is logged in
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        return "redirect:/login";
    }
    
    try {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        
        if (bookingOptional.isEmpty()) {
            model.addAttribute("error", "Booking not found");
            return "redirect:/bookings";
        }
        
        Booking booking = bookingOptional.get();
        
        // Check if booking belongs to logged-in user
        if (!booking.getUser().getId().equals(loggedInUser.getId())) {
            model.addAttribute("error", "Access denied");
            return "redirect:/bookings";
        }
        
        model.addAttribute("booking", booking);
        model.addAttribute("user", loggedInUser);
        model.addAttribute("title", "Booking Details - " + booking.getBookingReference());
        
        if (success != null) {
            model.addAttribute("success", "Booking created successfully! Your booking reference is: " + 
                             booking.getBookingReference());
        }
        
        return "booking-details";
        
    } catch (Exception e) {
        System.out.println("❌ Error loading booking details: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("error", "Failed to load booking details");
        return "redirect:/bookings";
    }
}


/**
 * Show payment page for a booking
 * Visit: http://localhost:8080/bookings/1/pay
 */
@GetMapping("/bookings/{bookingId}/pay")
public String showPaymentPage(@PathVariable Long bookingId, HttpSession session, Model model) {
    // Check if user is logged in
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        return "redirect:/login";
    }
    
    try {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        
        if (bookingOptional.isEmpty()) {
            model.addAttribute("error", "Booking not found");
            return "redirect:/bookings";
        }
        
        Booking booking = bookingOptional.get();
        
        // Check if booking belongs to logged-in user
        if (!booking.getUser().getId().equals(loggedInUser.getId())) {
            model.addAttribute("error", "Access denied");
            return "redirect:/bookings";
        }
        
        // Check if booking needs payment
        BigDecimal paidAmount = paymentRepository.calculateTotalPaidForBooking(booking);
        BigDecimal pendingAmount = booking.getTotalAmount().subtract(paidAmount);
        
        if (pendingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            model.addAttribute("message", "This booking is already fully paid");
            return "redirect:/bookings/" + bookingId;
        }
        
        model.addAttribute("booking", booking);
        model.addAttribute("user", loggedInUser);
        model.addAttribute("paidAmount", paidAmount);
        model.addAttribute("pendingAmount", pendingAmount);
        model.addAttribute("title", "Payment - " + booking.getBookingReference());
        
        return "payment-form";
        
    } catch (Exception e) {
        System.out.println("❌ Error loading payment page: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("error", "Failed to load payment page");
        return "redirect:/bookings";
    }
}

/**
 * Process payment
 */
@PostMapping("/bookings/{bookingId}/pay")
public String processPayment(
        @PathVariable Long bookingId,
        @RequestParam String paymentMethod,
        @RequestParam BigDecimal amount,
        @RequestParam(required = false) String cardNumber,
        @RequestParam(required = false) String cardHolderName,
        @RequestParam(required = false) String expiryMonth,
        @RequestParam(required = false) String expiryYear,
        @RequestParam(required = false) String cvv,
        @RequestParam(required = false) String upiId,
        HttpSession session,
        Model model) {
    
    // Check if user is logged in
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        return "redirect:/login";
    }
    
    try {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        
        if (bookingOptional.isEmpty()) {
            model.addAttribute("error", "Booking not found");
            return "redirect:/bookings";
        }
        
        Booking booking = bookingOptional.get();
        
        // Check if booking belongs to logged-in user
        if (!booking.getUser().getId().equals(loggedInUser.getId())) {
            model.addAttribute("error", "Access denied");
            return "redirect:/bookings";
        }
        
        // Validate payment amount
        BigDecimal paidAmount = paymentRepository.calculateTotalPaidForBooking(booking);
        BigDecimal pendingAmount = booking.getTotalAmount().subtract(paidAmount);
        
        if (amount.compareTo(pendingAmount) > 0) {
            model.addAttribute("error", "Payment amount cannot exceed pending amount");
            return "redirect:/bookings/" + bookingId + "/pay";
        }
        
        if (amount.compareTo(BigDecimal.valueOf(10)) < 0) {
            model.addAttribute("error", "Minimum payment amount is ₹10");
            return "redirect:/bookings/" + bookingId + "/pay";
        }
        
        // Create payment record
        Payment payment = new Payment(booking, amount, Payment.PaymentMethod.valueOf(paymentMethod));
        
        // Store card details (last 4 digits only)
        if (cardNumber != null && !cardNumber.trim().isEmpty()) {
            payment.setCardNumber(cardNumber);
            payment.setCardHolderName(cardHolderName);
        }
        
        // Save payment in PENDING status
        Payment savedPayment = paymentRepository.save(payment);
        
        // Simulate payment processing
        boolean paymentSuccess = processPaymentWithGateway(savedPayment, cardNumber, cvv, upiId);
        
        if (paymentSuccess) {
            // Update payment status
            savedPayment.setStatus(Payment.PaymentStatus.SUCCESS);
            savedPayment.setGatewayTransactionId("GTW" + System.currentTimeMillis());
            paymentRepository.save(savedPayment);
            
            // Update booking payment status
            BigDecimal newPaidAmount = paidAmount.add(amount);
            booking.setPaidAmount(newPaidAmount);
            
            if (newPaidAmount.compareTo(booking.getTotalAmount()) >= 0) {
                booking.setPaymentStatus(Booking.PaymentStatus.PAID);
                booking.setStatus(Booking.BookingStatus.CONFIRMED);
            } else {
                booking.setPaymentStatus(Booking.PaymentStatus.PARTIAL);
            }
            
            bookingRepository.save(booking);
            
            System.out.println("✅ Payment successful: " + savedPayment.getTransactionId());
            
            return "redirect:/payments/" + savedPayment.getId() + "?success=true";
            
        } else {
            // Payment failed
            savedPayment.setStatus(Payment.PaymentStatus.FAILED);
            savedPayment.setFailureReason("Payment declined by gateway");
            paymentRepository.save(savedPayment);
            
            model.addAttribute("error", "Payment failed. Please try again with different payment method.");
            return "redirect:/bookings/" + bookingId + "/pay";
        }
        
    } catch (Exception e) {
        System.out.println("❌ Error processing payment: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("error", "Payment processing failed: " + e.getMessage());
        return "redirect:/bookings/" + bookingId + "/pay";
    }
}

/**
 * Simulate payment gateway processing
 * In real world, this would call actual payment gateway APIs
 */
private boolean processPaymentWithGateway(Payment payment, String cardNumber, String cvv, String upiId) {
    try {
        // Simulate processing delay
        Thread.sleep(2000);
        
        // Simulate payment gateway logic
        Payment.PaymentMethod method = payment.getPaymentMethod();
        
        switch (method) {
            case CREDIT_CARD:
            case DEBIT_CARD:
                // Simulate card validation
                if (cardNumber == null || cardNumber.length() < 16) {
                    return false;
                }
                if (cvv == null || cvv.length() != 3) {
                    return false;
                }
                // 90% success rate for card payments
                return Math.random() > 0.1;
                
            case UPI:
                // Simulate UPI validation
                if (upiId == null || !upiId.contains("@")) {
                    return false;
                }
                // 95% success rate for UPI
                return Math.random() > 0.05;
                
            case NET_BANKING:
            case WALLET:
                // 98% success rate for other methods
                return Math.random() > 0.02;
                
            default:
                return false;
        }
        
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return false;
    }
}

/**
 * Show payment success/failure page
 */
@GetMapping("/payments/{paymentId}")
public String paymentResult(@PathVariable Long paymentId,
                          @RequestParam(required = false) String success,
                          HttpSession session, Model model) {
    // Check if user is logged in
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
        return "redirect:/login";
    }
    
    try {
        Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
        
        if (paymentOptional.isEmpty()) {
            model.addAttribute("error", "Payment not found");
            return "redirect:/bookings";
        }
        
        Payment payment = paymentOptional.get();
        
        // Check if payment belongs to logged-in user
        if (!payment.getBooking().getUser().getId().equals(loggedInUser.getId())) {
            model.addAttribute("error", "Access denied");
            return "redirect:/bookings";
        }
        
        model.addAttribute("payment", payment);
        model.addAttribute("booking", payment.getBooking());
        model.addAttribute("user", loggedInUser);
        model.addAttribute("title", "Payment Receipt - " + payment.getTransactionId());
        
        if (success != null) {
            model.addAttribute("success", "Payment processed successfully!");
        }
        
        return "payment-result";
        
    } catch (Exception e) {
        System.out.println("❌ Error loading payment result: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("error", "Failed to load payment details");
        return "redirect:/bookings";
    }
}

/**
 * Logout
 */
@GetMapping("/logout")
public String logout(HttpSession session) {
    session.invalidate(); // Clear all session data
    return "redirect:/?logout=true";
}


}