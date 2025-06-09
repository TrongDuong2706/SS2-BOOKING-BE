package com.devteria.identityservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS(1009, "Invalid credentials, please try again.", HttpStatus.BAD_REQUEST),
    PASSWORD_EXISTED(1010, "Password Existed", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD(1011, "Your password not correct", HttpStatus.BAD_REQUEST),
    HOTEL_NOT_EXISTED(1012, "Hotel not existed ", HttpStatus.NOT_FOUND),
    ROOM_NOT_EXISTED(1013, "Room not existed", HttpStatus.NOT_FOUND),
    CANNOT_SEND_EMAIL(1014, "Can not send email", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_CORRECT(1015, "Password not correct", HttpStatus.BAD_REQUEST),
    INVALID_QR_DATA(1016, "Error QR", HttpStatus.BAD_REQUEST),
    BOOKING_NOT_FOUND(1017, "Booking not found", HttpStatus.BAD_REQUEST),
    ROOM_NOT_FOUND(1018, "Room not found", HttpStatus.BAD_REQUEST),


    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
