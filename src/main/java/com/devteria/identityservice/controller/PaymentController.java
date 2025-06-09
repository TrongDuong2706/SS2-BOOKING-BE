package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.PaymentDTO;
import com.devteria.identityservice.dto.request.ApiResponse;
import com.devteria.identityservice.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @GetMapping("/vn-pay-callback")
    public ApiResponse<PaymentDTO.VNPayResponse> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            return ApiResponse.<PaymentDTO.VNPayResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Success")
                    .result(new PaymentDTO.VNPayResponse("00", "Successful", ""))
                    .build();
        } else {
            return ApiResponse.<PaymentDTO.VNPayResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Failed")
                    .build();
        }
    }
}
