package com.credora.notification.config.kafka.consumers;

import com.credora.notification.config.kafka.dto.OnboardingNotificationRequest;
import com.credora.notification.email.EmailService;
import com.credora.notification.sms.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationsConsumer {
//  private final EmailService emailService;
private final SmsService smsService;
  @KafkaListener(
          topics = "onboarding-confirmation",
          groupId = "${spring.kafka.consumer.group-id:notification-service}"
          // No need to specify containerFactory when using Spring Boot default
  )
  public void consumeOnboardingNotification(OnboardingNotificationRequest request) {
    log.info("Received onboarding notification request: {}", request);

    try {
//      emailService.sendOnboardingMail(request.customerFirstname(), request.customerEmail());
      smsService.sendOtpToPhoneNumber(request.phoneNumber(), request.customerEmail());
      log.info("Successfully processed onboarding notification for user: {}", request.customerEmail());
    } catch (Exception e) {
      log.error("Failed to process onboarding notification: {}", e.getMessage(), e);
      // Consider implementing retry logic or dead letter queue here
    }
  }
}
