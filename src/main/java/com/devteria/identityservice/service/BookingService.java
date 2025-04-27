package com.devteria.identityservice.service;

import com.devteria.identityservice.dto.PaymentDTO;
import com.devteria.identityservice.dto.request.BookingRequest;
import com.devteria.identityservice.dto.request.email.request.Recipient;
import com.devteria.identityservice.dto.request.email.request.SendEmailRequest;
import com.devteria.identityservice.dto.response.BookingResponse;
import com.devteria.identityservice.entity.Booking;
import com.devteria.identityservice.entity.Hotel;
import com.devteria.identityservice.entity.Room;
import com.devteria.identityservice.exception.AppException;
import com.devteria.identityservice.exception.ErrorCode;
import com.devteria.identityservice.mapper.BookingMapper;
import com.devteria.identityservice.repository.BookingRepository;
import com.devteria.identityservice.repository.HotelRepository;
import com.devteria.identityservice.repository.RoomRepository;
import com.devteria.identityservice.service.imp.BookingServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingService implements BookingServiceImp {
    BookingMapper bookingMapper;
    BookingRepository bookingRepository;
    EmailService emailService;

    PaymentService paymentService;
    HotelRepository hotelRepository;
    RoomRepository roomRepository;

    @Override
    public BookingResponse createBooking(BookingRequest request, HttpServletRequest httpRequest) {
        //Lấy thông tin hotel thông qua hotelId
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + request.getHotelId()));
        //Lấy thông tin Room
        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(() -> new RuntimeException("Room not found"));
        Booking booking = bookingMapper.toBooking(request);
        String uniqueCode = UUID.randomUUID().toString();
        booking.setCode(uniqueCode);
        bookingRepository.save(booking);

        // Tạo yêu cầu thanh toán VNPay
        PaymentDTO.VNPayResponse vnPayResponse = paymentService.createVnPayPayment(httpRequest, request);
        String paymentUrl = vnPayResponse.getPaymentUrl();
        // Tạo BookingResponse
        BookingResponse bookingResponse = bookingMapper.toBookingResponse(booking);
        bookingResponse.setPaymentUrl(paymentUrl);
        // Gửi email xác nhận
        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .to(Recipient.builder()
                        .name(request.getFirstName() + " " + request.getLastName())
                        .email(request.getEmail())
                        .build())
                .subject("Welcome to Trip.com")
                .htmlContent("<div style=\"font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #e0f7fa;\">\n" +
                        "    <div\n" +
                        "        style=\"max-width: 500px; margin: 20px auto; padding: 15px; background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);\">\n" +
                        "        <div\n" +
                        "            style=\"background-color: #00796b; color: #ffffff; padding: 10px; border-radius: 10px 10px 0 0; text-align: center;\">\n" +
                        "            <h1 style=\"margin: 0; font-size: 20px;\">Xác Nhận Đặt Phòng</h1>\n" +
                        "        </div>\n" +
                        "        <div style=\"padding: 15px; border-bottom: 1px solid #009688;\">\n" +
                        "            <h2 style=\"font-size: 16px; color: #00796b; margin-bottom: 8px;\">Thông Tin Đặt Phòng</h2>\n" +
                        "            <p style=\"margin: 8px 0; line-height: 1.6;\"><strong>Tên Khách Sạn:</strong> " + hotel.getName() + "</p>\n" +
                        "            <p style=\"margin: 8px 0; line-height: 1.6;\"><strong>Số Phòng:</strong> " + room.getRoomNumber() + "</p>\n" +
                        "            <p style=\"margin: 8px 0; line-height: 1.6;\"><strong>Check-in:</strong> " + request.getCheckInDate() + "</p>\n" +
                        "            <p style=\"margin: 8px 0; line-height: 1.6;\"><strong>Check-out:</strong> " + request.getCheckOutDate() + "</p>\n" +
                        "            <p style=\"margin: 8px 0; line-height: 1.6;\"><strong>Số Lượng Phòng:</strong> " + request.getNumberOfRoom() + "</p>\n" +
                        "            <p style=\"margin: 8px 0; line-height: 1.6;\"><strong>Số Tiền Cần Thanh Toán:</strong> " + request.getTotalPrice() + "</p>\n" +
                        "        </div>\n" +
                        "        <div style=\"padding: 15px; border-bottom: 1px solid #009688;\">\n" +
                        "            <h2 style=\"font-size: 16px; color: #00796b; margin-bottom: 8px;\">Thông Tin Khách Hàng</h2>\n" +
                        "            <p style=\"margin: 8px 0; line-height: 1.6;\"><strong>Tên Khách Hàng:</strong> " + request.getFirstName()+" "+request.getLastName()+ "</p>\n" +
                        "            <p style=\"margin: 8px 0; line-height: 1.6;\"><strong>Email:</strong> " + request.getEmail() + "</p>\n" +
                        "            <p style=\"margin: 8px 0; line-height: 1.6;\"><strong>Số Điện Thoại:</strong> " + request.getPhoneNumber() + "</p>\n" +
                        "        </div>\n" +
                        "        <div style=\"padding: 15px; border-bottom: 1px solid #009688;\">\n" +
                        "            <h2 style=\"font-size: 16px; color: #00796b; margin-bottom: 8px;\">Phương Thức Thanh Toán</h2>\n" +
                        "            <p style=\"margin: 8px 0; line-height: 1.6;\">" + request.getPaymentMethod() + "</p>\n" +
                        "        </div>\n" +
                        "        <div style=\"padding: 15px;\">\n" +
                        "            <h2 style=\"font-size: 16px; color: #00796b; margin-bottom: 8px;\">Yêu Cầu Đặc Biệt</h2>\n" +
                        "            <p style=\"margin: 8px 0; line-height: 1.6;\">" + request.getSpecialRequest() + "</p>\n" +
                        "        </div>\n" +
                        "        <div style=\"text-align: center; padding: 15px; font-size: 14px; color: #555;\">\n" +
                        "            <p style=\"margin: 0;\">Xin cảm ơn quý khách đã đặt phòng tại Trip.com .</p>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "</div>")
                .build();
        emailService.sendEmail(emailRequest);

        return bookingResponse;
    }

    public BookingResponse getOneBooking(int bookingId){
        var booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new AppException(ErrorCode.HOTEL_NOT_EXISTED)
        );
        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    public Long getTotalBooking() {
        return bookingRepository.countTotalBooking();
    }
    @Override
    public List<Object[]> getMonthlyRevenue() {
        return bookingRepository.findMonthlyRevenue();
    }
}
