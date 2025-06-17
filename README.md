# 🌍 TravelPro - Travel Management System

A comprehensive travel booking and management system built with Spring Boot, offering seamless package browsing, booking, and payment processing.
## 🚀 Features

### 🔐 User Management
- **User Registration & Authentication** - Secure signup and login system
- **Session Management** - Persistent user sessions with automatic logout
- **Profile Management** - Personal information and contact details

### 🏖️ Travel Packages
- **Package Browsing** - Beautiful grid layout with search and filters
- **Detailed Package Views** - Comprehensive information with inclusions/exclusions
- **Search & Filter** - By destination, package type, and keywords
- **Related Packages** - Smart recommendations based on destination

### 📅 Booking System
- **Interactive Booking Form** - Easy-to-use booking interface
- **Real-time Validation** - Form validation and availability checking
- **Booking Management** - View all bookings with status tracking
- **Travel Date Selection** - Calendar-based date picker with restrictions

### 💳 Payment Processing
- **Multiple Payment Methods** - Credit Card, Debit Card, UPI, Net Banking
- **Secure Payment Forms** - SSL-secured payment processing
- **Payment Receipts** - Detailed transaction receipts with download option
- **Partial Payments** - Support for installment payments
- **Payment History** - Complete transaction tracking

### 📊 Dashboard & Analytics
- **User Dashboard** - Personalized overview with booking stats
- **Booking Analytics** - Total bookings, spending, and upcoming trips
- **Quick Actions** - Easy access to recent bookings and new packages

## 🛠️ Technology Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.2** - Application framework
- **Spring Data JPA** - Database abstraction layer
- **Spring Security** - Authentication and authorization
- **Hibernate** - ORM for database operations

### Frontend
- **Thymeleaf** - Server-side templating engine
- **HTML5 & CSS3** - Modern web standards
- **JavaScript** - Client-side interactivity
- **Responsive Design** - Mobile-friendly interface

### Database
- **PostgreSQL** - Primary database
- **JPA/Hibernate** - Database mapping and queries

### Build Tools
- **Maven** - Dependency management and build automation

## 📋 Prerequisites

Before running this application, make sure you have:

- **Java 17** or higher installed
- **Maven 3.6+** installed
- **PostgreSQL 12+** installed and running
- **Git** for version control

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/travel-management-system.git
cd travel-management-system
```

### 2. Database Setup
```sql
-- Create database
CREATE DATABASE travel_management;

-- Create user (optional)
CREATE USER travel_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE travel_management TO travel_user;
```

### 3. Configure Application
Update `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/travel_management
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Server Configuration
server.port=8080
```

### 4. Build and Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### 5. Access the Application
Open your browser and navigate to: `http://localhost:8080`

## 📱 Usage Guide

### Getting Started
1. **Register** - Create a new account at `/register`
2. **Login** - Sign in with your credentials at `/login`
3. **Browse Packages** - Explore travel destinations at `/packages`
4. **Book a Trip** - Select a package and complete booking
5. **Make Payment** - Process payment through secure gateway
6. **Manage Bookings** - View and track your trips at `/bookings`

### Sample Test Data
The application includes sample travel packages. Generate them by visiting:
```
http://localhost:8080/test-packages
```

### Test Payment Details
For testing payments, use these sample details:
- **Credit Card**: 4111 1111 1111 1111
- **CVV**: 123
- **Expiry**: Any future date
- **UPI ID**: test@paytm

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/travelapp/
│   │   ├── controller/          # REST controllers
│   │   ├── entity/              # JPA entities
│   │   ├── repository/          # Data repositories
│   │   └── TravelApplication.java
│   └── resources/
│       ├── static/              # CSS, JS, images
│       ├── templates/           # Thymeleaf templates
│       └── application.properties
└── test/                        # Unit tests
```

## 🔧 Key Components

### Entities
- **User** - User account information
- **TravelPackage** - Travel package details
- **Booking** - Booking information and status
- **Payment** - Payment transactions and history

### Controllers
- **HomeController** - Main application routes
- **UserController** - User management (if implemented)
- **PackageController** - Package operations (if implemented)

### Templates
- **Dashboard** - User dashboard and overview
- **Package Listing** - Browse and search packages
- **Package Details** - Detailed package information
- **Booking Form** - Create new bookings
- **Payment Form** - Process payments
- **User Bookings** - Manage existing bookings

## 🔐 Security Features

- **Password Encryption** - BCrypt password hashing
- **Session Management** - Secure session handling
- **SQL Injection Prevention** - Parameterized queries
- **XSS Protection** - Output escaping in templates
- **CSRF Protection** - Built-in Spring Security features

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### Manual Testing Checklist
- [ ] User registration and login
- [ ] Package browsing and search
- [ ] Booking creation and management
- [ ] Payment processing
- [ ] Dashboard functionality

## 🚢 Deployment

### Production Configuration
1. Update database credentials for production
2. Set `spring.jpa.hibernate.ddl-auto=validate`
3. Configure SSL certificates
4. Set up environment variables for sensitive data

### Docker Deployment (Optional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/travel-management-system.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- LinkedIn: [Your LinkedIn](https://linkedin.com/in/yourprofile)
- Email: your.email@example.com

## 🙏 Acknowledgments

- Spring Boot team for the amazing framework
- Thymeleaf team for the templating engine
- PostgreSQL community
- All contributors and testers

## 📞 Support

If you encounter any issues or have questions:

1. Check the [Issues](https://github.com/yourusername/travel-management-system/issues) page
2. Create a new issue with detailed description
3. Contact the maintainer

---

⭐ **Star this repository if you found it helpful!**

## 🔮 Future Enhancements

- [ ] Email notifications for bookings
- [ ] Admin panel for package management
- [ ] Package reviews and ratings
- [ ] Mobile app using React Native
- [ ] Real-time chat support
- [ ] Integration with real payment gateways
- [ ] Multi-language support
- [ ] Advanced reporting and analytics
