package com.credora.notification.config.kafka.consumers;

import com.credora.notification.config.kafka.dto.OnboardingNotificationRequest;
import com.credora.notification.sms.SmsService;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationsConsumer {

  private final SmsService smsService;
  private final Tracer tracer;
  private final Propagator propagator;

  @KafkaListener(
          topics = "onboarding-confirmation",
          groupId = "${spring.kafka.consumer.group-id:notification-service}"
  )
  public void consumeOnboardingNotification(ConsumerRecord<String, OnboardingNotificationRequest> record) {

    Span span = propagator.extract(record.headers(), (headers, key) -> {
              Header header = headers.lastHeader(key);
              return header != null ? new String(header.value(), StandardCharsets.UTF_8) : null;
            }).name("onboarding-confirmation-consumer")
            .start();

    try (Tracer.SpanInScope ignored = tracer.withSpan(span)) {
      OnboardingNotificationRequest request = record.value();
      log.info("Received onboarding notification request: {}", request);

      smsService.sendOtpToPhoneNumber(request.phoneNumber(), request.customerEmail());
      log.info("Successfully processed onboarding notification for user: {}", request.customerEmail());

      span.tag("notification.type", "onboarding");
      span.tag("customer.email", request.customerEmail());
    } catch (Exception e) {
      log.error("Failed to process onboarding notification: {}", e.getMessage(), e);
      span.error(e);
    } finally {
      span.end();
    }
  }
}
