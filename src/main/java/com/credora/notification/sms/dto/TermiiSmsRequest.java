package com.credora.notification.sms.dto;

public record TermiiSmsRequest(
        String to,
        String from,
        String sms,
        String type,
        String channel,
        String api_key
) {
}
