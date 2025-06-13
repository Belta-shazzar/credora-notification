package com.credora.notification.shared;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

@Component
public class Utils {
  public static String generateUrlSafeToken(int truncateLength) {
    SecureRandom secureRandom = new SecureRandom();
    byte[] randomBytes = new byte[32];
    secureRandom.nextBytes(randomBytes);

    String base64UrlEncoded = Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(randomBytes);

    return base64UrlEncoded.length() > truncateLength
            ? base64UrlEncoded.substring(0, truncateLength)
            : base64UrlEncoded;
  }

  public static int generateRandNumber(int length) {
    if (length <= 0) {
      throw new IllegalArgumentException("Length must be greater than 0");
    }

    Random random = new Random();
    int lowerBound = (int) Math.pow(10, length - 1);
    int upperBound = (int) Math.pow(10, length) - 1;

    return lowerBound + random.nextInt(upperBound - lowerBound + 1);
  }

}
