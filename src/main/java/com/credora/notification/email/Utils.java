package com.credora.notification.email;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

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
}
