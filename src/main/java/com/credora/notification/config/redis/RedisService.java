package com.credora.notification.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Service
@RequiredArgsConstructor
public class RedisService {
  private final RedisTemplate<String, Object> redisTemplate;

  public void saveMessage(String key, String message) {
    long defaultTtl = 1800000;
    saveMessage(key, message, defaultTtl);
  }

  public void saveMessage(String key, String message, long ttl) {
    redisTemplate.opsForValue().set(key, message, ttl, MILLISECONDS);
  }

  public String getMessage(String key) {
    return (String) redisTemplate.opsForValue().get(key);
  }
}
