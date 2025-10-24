# 🧪 Movie Booking System - Testing Guide

## 🚀 How to Run and Test the Complete System

### Step 1: Start the Backend
```bash
# In project root directory
mvn spring-boot:run
```
**Expected**: Backend starts on http://localhost:8080

### Step 2: Start the Frontend
```bash
# In new terminal, navigate to frontend directory
cd frontend
npm install  # First time only
npm start
```
**Expected**: Frontend starts on http://localhost:3000

### Step 3: Open Application
Visit: http://localhost:3000

## ✅ Complete Testing Checklist

### 🔐 Authentication Testing

#### User Registration
1. **Navigate to**: http://localhost:3000/register
2. **Fill form with**:
   - First Name: John
   - Last Name: Doe
   - Email: john.doe@example.com
   - Phone: +1234567890
   - Password: password123
   - Confirm Password: password123
3. **Click**: "Create Account"
4. **Expected**: Successful registration and automatic login

#### User Login
1. **Navigate to**: http://localhost:3000/login
2. **Fill form with**:
   - Email: john.doe@example.com
   - Password: password123
3. **Click**: "Sign In"
4. **Expected**: Successful login and redirect to home page

#### Admin Registration (API Test)
```bash
curl -X POST http://localhost:8080/api/auth/admin/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "admin123",
    "firstName": "Admin",
    "lastName": "User"
  }'
```

### 🎬 Movie Management Testing

#### View Movies (Public)
1. **Navigate to**: http://localhost:3000/movies
2. **Expected**: List of movies (empty initially)

#### Add Movie (Admin Only)
```bash
# First login as admin to get token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "admin123"
  }'

# Use the token from response
curl -X POST http://localhost:8080/api/admin/movies \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -F 'movie={
    "title": "Avengers: Endgame",
    "description": "The epic conclusion to the Infinity Saga",
    "genre": "Action",
    "duration": 181,
    "director": "Russo Brothers",
    "language": "English",
    "rating": "PG-13",
    "imdbRating": 8.4
  }; type=application/json' \
  -F 'image=@path/to/movie-poster.jpg'
```

### 🎭 Showtime Management Testing

#### Add Showtime (Admin Only)
```bash
curl -X POST http://localhost:8080/api/admin/showtimes \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "movieId": "MOVIE_ID_FROM_PREVIOUS_STEP",
    "startTime": "2024-12-25T19:00:00",
    "endTime": "2024-12-25T22:01:00",
    "screenNumber": 1,
    "totalSeats": 100,
    "ticketPrice": 15.99
  }'
```

### 🪑 Seat Selection & Booking Testing

#### Check Seat Availability
1. **Navigate to**: Movie details page
2. **Click**: "Book Now" on a showtime
3. **Expected**: Seat selection interface with available seats

#### Create Booking
1. **Select seats**: Click on available seats (green)
2. **Click**: "Proceed to Payment"
3. **Expected**: Booking created with PENDING_PAYMENT status

### 💳 Payment Testing

#### Test Successful Payment
1. **Fill payment form**:
   - Card Number: 4111111111111111 (Visa test card)
   - Cardholder Name: John Doe
   - Expiry Month: 12
   - Expiry Year: 2025
   - CVV: 123
2. **Click**: "Pay Now"
3. **Expected**: Payment success, booking confirmed, ticket generated

#### Test Failed Payment
1. **Fill payment form**:
   - Card Number: 2111111111111111 (Test failure card)
   - Other details: Same as above
2. **Click**: "Pay Now"
3. **Expected**: Payment failure message

### 🎫 Ticket Management Testing

#### View Ticket
1. **After successful payment**: Navigate to "My Bookings"
2. **Click**: "View Ticket" on confirmed booking
3. **Expected**: Ticket with QR code displayed

#### Validate Ticket (Admin)
```bash
curl -X POST http://localhost:8080/api/tickets/validate \
  -H "Authorization: Bearer ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"qrCodeData": "QR_CODE_DATA_FROM_TICKET"}'
```

### 📊 Reporting Testing (Admin Only)

#### Sales Report
```bash
curl -X GET "http://localhost:8080/api/admin/reports/sales?startDate=2024-01-01&endDate=2024-12-31" \
  -H "Authorization: Bearer ADMIN_TOKEN"
```

#### Occupancy Report
```bash
curl -X GET http://localhost:8080/api/admin/reports/occupancy \
  -H "Authorization: Bearer ADMIN_TOKEN"
```

#### Dashboard Statistics
```bash
curl -X GET http://localhost:8080/api/admin/reports/dashboard \
  -H "Authorization: Bearer ADMIN_TOKEN"
```

## 🔍 Frontend UI Testing

### Navigation Testing
1. **Test responsive navigation**: Resize browser window
2. **Test mobile menu**: Use browser dev tools mobile view
3. **Test user menu**: Click on user avatar when logged in

### Form Validation Testing
1. **Registration form**: Try submitting with invalid data
2. **Login form**: Try wrong credentials
3. **Payment form**: Try invalid card numbers

### State Management Testing
1. **Login state**: Verify user info persists on page refresh
2. **Booking flow**: Verify selected seats persist during payment
3. **Error handling**: Check error messages display correctly

## 🐛 Common Issues & Solutions

### Backend Issues
- **Port 8080 in use**: Change port in application.yml
- **MongoDB connection failed**: Check internet connection and credentials
- **JWT token expired**: Re-login to get new token

### Frontend Issues
- **Blank page**: Check browser console for errors
- **API calls failing**: Verify backend is running on port 8080
- **Styling issues**: Clear browser cache

### Database Issues
- **Collections not created**: They're created automatically on first use
- **Data not persisting**: Check MongoDB Atlas connection

## 📋 Test Data Examples

### Sample Movies
```json
{
  "title": "Spider-Man: No Way Home",
  "description": "Peter Parker's identity is revealed",
  "genre": "Action",
  "duration": 148,
  "director": "Jon Watts",
  "language": "English",
  "rating": "PG-13",
  "imdbRating": 8.2
}
```

### Sample Showtimes
```json
{
  "startTime": "2024-12-25T14:00:00",
  "endTime": "2024-12-25T16:28:00",
  "screenNumber": 1,
  "totalSeats": 100,
  "ticketPrice": 12.99
}
```

### Test Payment Cards
- **Success**: 4111111111111111, 5555555555554444, 378282246310005
- **Failure**: 2111111111111111, 1111111111111111

## ✅ Success Criteria

### Backend API
- ✅ All endpoints respond correctly
- ✅ JWT authentication works
- ✅ Database operations succeed
- ✅ File uploads to Cloudinary work
- ✅ QR codes generate properly

### Frontend UI
- ✅ All pages load without errors
- ✅ Forms submit and validate correctly
- ✅ Navigation works on all devices
- ✅ State management functions properly
- ✅ Error handling displays appropriately

### Integration
- ✅ Frontend communicates with backend
- ✅ Authentication flow works end-to-end
- ✅ Booking process completes successfully
- ✅ Payment simulation functions correctly
- ✅ Admin features accessible with proper roles

## 🎯 Performance Testing

### Load Testing
- Test with multiple concurrent users
- Verify seat availability updates in real-time
- Check database performance with large datasets

### Browser Compatibility
- Test on Chrome, Firefox, Safari, Edge
- Verify mobile responsiveness
- Check accessibility features

---

**🎉 If all tests pass, your Movie Booking System is fully functional!**
