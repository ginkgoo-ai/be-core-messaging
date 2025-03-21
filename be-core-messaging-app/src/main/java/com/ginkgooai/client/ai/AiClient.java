package com.ginkgooai.client.ai;

import com.ginkgooai.core.common.config.FeignConfig;
import jakarta.mail.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ai-service", url="${core-ai-uri}", configuration = FeignConfig.class)
public interface AiClient {
    @PostMapping("/v1/email")
    String generation(@RequestParam Message emailMessage, @RequestParam(required = false, defaultValue = "simple-email-trace") String emailTrace);
}

