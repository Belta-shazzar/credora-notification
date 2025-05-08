package com.credora.notification.config.kafka.dto;

public record OnboardingNotificationRequest(
        String customerFirstname,
        String customerLastname,
        String customerEmail,
        String onboardingReference
) {
}
