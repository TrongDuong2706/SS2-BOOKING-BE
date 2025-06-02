# Hotel Booking Backend

This is the **Backend** project for the hotel booking website.

---

## 📅 Timeline & Progress

### 14/04 - 20/04/2025
- Designed the **database schema**:
  ![Database](./images/database.jpg)
---

### 21/04 - 27/04/2025
- Developed API endpoints for:
    - **User login**
      ![Login](./images/login.jpg)
    - **User registration**
      ![Register](./images/register.jpg)
    - **Google authentication**
    - **User logout**
---

### 28/04 - 04/05/2025
- Built API endpoints for hotel management:
    - **Retrieve all hotels**
    - **Filter hotels** by criteria (e.g., price, category, Name)
    - **Search hotels** by destination
      ![Hotel List](./images/ListHotel.jpg)
    - GetUserInformation
      ![GetUserInfor](./images/getInfor.jpg)
    - **Create Hotel**
### 05/05 - 11/05/2025
- Built API endpoints for getDetailHotel
  - Implemented the endpoint GET /hotels/{hotelId} to retrieve detailed information of a specific hotel.

  - This endpoint returns a structured ApiResponse<HotelResponse> containing the full detail of the hotel entity, including images, location, price, and category.

  - Integrated the endpoint with service layer hotelServiceImp.getOneHotel(hotelId) to encapsulate business logic and database interaction.

  - Ensured the endpoint is RESTful and supports standard HTTP status codes for success and failure scenarios.
- Handle Exception!!
  - Create a custom exception that includes predefined error codes.
    ![Exception](./images/exception.jpg)
### 12/05 - 18/05/2025
  - Built VNPAY Payment API to handle payment transactions.
    Integrated Email Sending functionality using Brevo (formerly Sendinblue).
   ![VNPAY](./images/codevnpay.jpg)
   ![Brevo](./images/brevo.jpg)
  - Work:
    Created endpoints to initiate and verify VNPAY payments.
    Sent booking confirmation emails to users upon successful transactions via Brevo SMTP.
    ![Email](./images/Email.jpg)
### 19/05 - 25/05/2025
  - Developed Comment API for users to post and fetch reviews on hotels.
  - Implemented Related Hotels API, recommending hotels based on category, location, or rating.
  - Built Profile Management API allowing users to retrieve and update their profile information.
### 26/05 - 01/06/2025
  - API List Hotel with pagination and search params
    ![List-Hotel](./images/ListHotelAdminPostMan.jpg)
  - API Create Hotel with cloudinary
    ![Create-Hotel](./images/CreateHotelAdminAPI.jpg)
  - Edit hotel API is similar to create Hotel API, but will get information by Hotel ID
### 02/06 - 08/06/2025
- API List Room with params
  ![List Room](./images/GetRoomAdmin.jpg)
- API Create Room with cloudinary
  ![Create-Room](./images/AddRoomAdmin.jpg)
- API Edit Room
  ![Edit-Room](./images/EdtRoomAdmin.jpg)
- Room APIS
  ![Room-APIS](./images/RoomAPIS.jpg)
---

## 🚀 Technologies Used
- Spring Boot
- Java 21
- MySQL
- JWT Authentication
- OAuth 2.0 (Google Sign-In)
- Maven
- Apache TomCat
---


