package com.devteria.identityservice.service;

import com.devteria.identityservice.dto.request.email.request.EmailRequest;
import com.devteria.identityservice.dto.request.email.request.SendEmailRequest;
import com.devteria.identityservice.dto.request.email.request.Sender;
import com.devteria.identityservice.dto.request.email.response.EmailResponse;
import com.devteria.identityservice.exception.AppException;
import com.devteria.identityservice.exception.ErrorCode;
import com.devteria.identityservice.repository.httpclient.EmailClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;
    @NonFinal
    @Value("${email.email_key}")
    protected String EMAIL_KEY;

    public EmailResponse sendEmail(SendEmailRequest request){
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name("Trip Dot Com")
                        .email("vojungkook209@gmail.com")
                        .build())
                .to(List.of(request.getTo()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        try{
            log.info("Using EMAIL_KEY: {}", EMAIL_KEY);
            return emailClient.sendEmail(EMAIL_KEY,emailRequest);
        }
        catch (FeignException e){
            log.error("Failed to send email: {}", e.contentUTF8(), e);
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }


    }
}
