package com.ginkgooai.controller;


import com.ginkgooai.dto.InboundParseRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@Slf4j
public class WebhookController {


    @PostMapping
    public String handleInboundEmail(
            @RequestBody InboundParseRequest inboundParseRequest) {

        log.info("inbound email receive request:{}", inboundParseRequest);

//        // 2. 解析业务逻辑（例如提取工单ID）
//        String ticketId = extractTicketId(to);
//
//        // 3. 更新数据库或触发后续操作
//        System.out.println("Received email for ticket: " + ticketId);

        return "Success";
    }


    // 验证SendGrid签名（HMAC-SHA256）
    private boolean verifySignature(String headers) {
        log.info("headers:{}", headers);
        return true;
    }
}
