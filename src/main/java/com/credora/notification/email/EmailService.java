package com.credora.notification.email;

import com.credora.notification.shared.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor
public class EmailService {
  @Value("${spring.mail.sender}")
  private String senderMail;
  @Value("${application.onboarding.baseurl}")
  private String baseUrl;
  private final JavaMailSender mailSender;

  private void sendMail(String to, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(senderMail);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(body);

    mailSender.send(message);
  }

  public void sendOnboardingMail(String firstName, String email) {
    String token = Utils.generateUrlSafeToken(60);
//    Cache the token in Redis
    String subject = "You’re Almost In! Confirm Your Credora Account";
    String body = "Hi " + firstName + ",\n" +
            "\n" +
            "Welcome to Credora – we’re excited to have you on board!\n" +
            "\n" +
            "To activate your account and start exploring financial tools built just for you, kindly click the link to confirm your email address:\n" +
            "\n" +
            String.format("%s/api/v1/auth/verify-email/?token=%s", baseUrl, token);

    this.sendMail(email, subject, body);
  }
}
