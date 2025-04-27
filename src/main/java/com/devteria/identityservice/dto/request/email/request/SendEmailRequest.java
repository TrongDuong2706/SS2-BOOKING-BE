package com.devteria.identityservice.dto.request.email.request;

import com.devteria.identityservice.dto.request.email.request.Recipient;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendEmailRequest {
    Recipient to;
    String subject;
    String htmlContent;
}
