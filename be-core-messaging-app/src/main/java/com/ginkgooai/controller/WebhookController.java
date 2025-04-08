package com.ginkgooai.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@Slf4j
public class WebhookController {


    @PostMapping
    public String handleInboundEmail(
            @RequestParam(value = "from",required = false) String from,
            @RequestParam(value = "to",required = false) String to,
            @RequestParam(value = "subject",required = false) String subject,
            @RequestParam(value = "text",required = false) String text,
            @RequestParam(value = "headers",required = false) String headers) {

        // 1. 验证签名
        if (!verifySignature(headers)) {
            return "Signature verification failed!";
        }

        log.info("from: {}, to: {}, subject: {}, text: {}", from, to, subject, text);

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
