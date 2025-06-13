package com.credora.notification.sms;

import com.credora.notification.sms.dto.TermiiSmsRequest;
import com.credora.notification.sms.dto.TermiiSmsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "termiiClient",
        url = "${application.termii.base-url}"
)
public interface TermiiClient {
  @PostMapping("/api/sms/send")
  TermiiSmsResponse sendSms(@RequestBody TermiiSmsRequest termiiSmsRequest);

}
