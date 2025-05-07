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
### 25/05 - 11/05/2025
- Built API endpoints for getDetailHotel
  - Implemented the endpoint GET /hotels/{hotelId} to retrieve detailed information of a specific hotel.

  - This endpoint returns a structured ApiResponse<HotelResponse> containing the full detail of the hotel entity, including images, location, price, and category.

  - Integrated the endpoint with service layer hotelServiceImp.getOneHotel(hotelId) to encapsulate business logic and database interaction.

  - Ensured the endpoint is RESTful and supports standard HTTP status codes for success and failure scenarios.
- Handle Exception!
  - Create a custom exception that includes predefined error codes.
    ![Exception](./images/exception.jpg)


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


