package com.devteria.identityservice.dto;

import lombok.Builder;
import lombok.Getter;

public abstract class PaymentDTO {
    @Getter
    @Builder
    public static class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;

        // Constructor công khai
        public VNPayResponse(String code, String message, String paymentUrl) {
            this.code = code;
            this.message = message;
            this.paymentUrl = paymentUrl;
        }
    }
}
