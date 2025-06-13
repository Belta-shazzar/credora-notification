package com.credora.notification.sms.dto;

public record TermiiSmsResponse(
        String message_id,
        String message,
        int balance,
        String user
) {
}
