package com.credora.notification.sms;

import com.credora.notification.config.redis.RedisService;
import com.credora.notification.shared.Utils;
import com.credora.notification.sms.dto.TermiiSmsRequest;
import com.credora.notification.sms.dto.TermiiSmsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor
public class SmsService {
  @Value("${application.termii.api-key}")
  private String termiiApiKey;

  @Value("${application.termii.sender}")
  private String termiiSmsSender;

  private final TermiiClient termiiClient;
  private final RedisService redisService;
  private final ObjectMapper objectMapper;

  private void sendOtp(String phoneNumber, String message) {
    TermiiSmsRequest request = new TermiiSmsRequest(
            phoneNumber,
            termiiSmsSender,
            message,
            "plain",
            "dnd",
            termiiApiKey
    );

    TermiiSmsResponse response = termiiClient.sendSms(request);
    System.out.println("Response from Termii: " + response);
  }

  public void sendOtpToPhoneNumber(String phoneNumber, String email) throws JsonProcessingException {
    Map<String, String> cacheData = new HashMap<>();
    int otp = Utils.generateRandNumber(6);
    cacheData.put("otp", String.valueOf(otp));
    cacheData.put("email", email);
    String redisKey = String.format("onboarding-otp:%s", phoneNumber);

    String jsonString = objectMapper.writeValueAsString(cacheData);

    redisService.saveMessage(redisKey, jsonString, 300000); // Save OTP for 5 minutes
    String message = String.format("Your account verification code is: %s", otp);
    sendOtp(phoneNumber, message);
    log.info("OTP: {}", otp);
    log.info("OTP sent to phone number: {}", phoneNumber);
  }
}
