# 🎬 Movie Booking System

A comprehensive full-stack movie booking system built with Spring Boot, MongoDB, and React.

## 🚀 Quick Start

### Prerequisites
- **Java 17+** - [Download here](https://www.oracle.com/java/technologies/downloads/)
- **Node.js 16+** - [Download here](https://nodejs.org/)
- **Maven 3.6+** - [Download here](https://maven.apache.org/download.cgi)

### 🔧 Running the Application

#### Option 1: Using Batch Files (Windows)
1. **Start Backend**: Double-click `run-backend.bat`
2. **Start Frontend**: Double-click `frontend/run-frontend.bat`

#### Option 2: Manual Commands

**Backend (Terminal 1):**
```bash
mvn spring-boot:run
```

**Frontend (Terminal 2):**
```bash
cd frontend
npm install
npm start
```

### 🌐 Access URLs
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api
- **API Documentation**: http://localhost:8080/swagger-ui.html

## 🎯 Features

### ✅ User Features
- **Authentication**: Register, Login, Profile Management
- **Movie Browsing**: View movies, search, filter by genre
- **Seat Selection**: Interactive seat selection with real-time availability
- **Booking Management**: Create, view, and cancel bookings
- **Payment Simulation**: Test payment processing with card validation
- **Ticket Generation**: QR code tickets for confirmed bookings

### ✅ Admin Features
- **Movie Management**: Add, edit, delete movies with image upload
- **Showtime Management**: Schedule and manage movie showtimes
- **User Management**: View and manage user accounts
- **Booking Oversight**: Monitor all bookings and payments
- **Analytics**: Sales reports and occupancy analytics

## 🧪 Testing the System

### 1. Test User Registration
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

### 2. Test User Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

### 3. Test Movie Listing
```bash
curl -X GET http://localhost:8080/api/movies
```

### 4. Payment Simulation Test Cards
- **Success**: Cards starting with 4, 5, or 3 (e.g., 4111111111111111)
- **Failed**: Cards starting with 2 or 1 (e.g., 2111111111111111)

## 🏗️ Architecture

### Backend (Spring Boot)
```
src/main/java/com/moviebooking/
├── config/          # Configuration classes
├── controller/      # REST API controllers
├── dto/            # Data Transfer Objects
├── exception/      # Exception handling
├── model/          # MongoDB documents
├── repository/     # Data access layer
├── security/       # JWT & Security config
└── service/        # Business logic
```

### Frontend (React)
```
frontend/src/
├── components/     # Reusable UI components
├── pages/         # Page components
├── store/         # Redux state management
├── types/         # TypeScript definitions
├── theme/         # Material-UI theme
└── config/        # API configuration
```

## 🗄️ Database Schema

### Core Collections
- **users**: User accounts and authentication
- **movies**: Movie information and metadata
- **showtimes**: Movie scheduling and pricing
- **bookings**: Seat reservations and status
- **payments**: Transaction records
- **tickets**: QR code tickets

## 🔐 Security Features
- **JWT Authentication**: Stateless token-based auth
- **Role-based Access**: USER and ADMIN roles
- **Password Hashing**: BCrypt encryption
- **CORS Configuration**: Cross-origin request handling
- **Input Validation**: Request data validation

## 🎨 UI Features
- **Dark Theme**: Professional Netflix-inspired design
- **Responsive Design**: Mobile and desktop optimized
- **Material-UI**: Modern component library
- **Real-time Updates**: Live seat availability
- **Form Validation**: Client-side validation

## 📊 API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/admin/register` - Admin registration

### Movies
- `GET /api/movies` - List all movies
- `GET /api/movies/{id}` - Get movie details
- `GET /api/movies/{id}/showtimes` - Get movie showtimes

### Bookings
- `GET /api/bookings/seats/{showtimeId}` - Seat availability
- `POST /api/bookings` - Create booking
- `GET /api/bookings/my-bookings` - User bookings

### Admin
- `POST /api/admin/movies` - Add movie
- `PUT /api/admin/movies/{id}` - Update movie
- `DELETE /api/admin/movies/{id}` - Delete movie
- `GET /api/admin/reports/sales` - Sales reports

## 🔧 Configuration

### MongoDB Connection
```yaml
spring:
  data:
    mongodb:
      uri: mongodb+srv://username:password@cluster.mongodb.net/
      database: movie_booking_system
```

### Cloudinary Setup
```yaml
cloudinary:
  cloud-name: your-cloud-name
  upload-preset: your-upload-preset
```

## 🐛 Troubleshooting

### Backend Issues
1. **Port 8080 in use**: Change port in `application.yml`
2. **MongoDB connection**: Check connection string and network
3. **Java version**: Ensure Java 17+ is installed

### Frontend Issues
1. **Port 3000 in use**: React will prompt for alternative port
2. **API connection**: Verify backend is running on port 8080
3. **Dependencies**: Run `npm install` in frontend directory

## 📝 Development Notes

### Adding New Features
1. **Backend**: Create model → repository → service → controller
2. **Frontend**: Create types → API calls → Redux slice → components
3. **Testing**: Add unit tests and integration tests

### Database Seeding
The application will create collections automatically. For sample data:
1. Register admin user via API
2. Add movies through admin panel
3. Create showtimes for movies

## 🤝 Contributing
1. Fork the repository
2. Create feature branch
3. Make changes with tests
4. Submit pull request

## 📄 License
This project is licensed under the MIT License.

---

**Built with ❤️ using Spring Boot, MongoDB, React, and Material-UI**
