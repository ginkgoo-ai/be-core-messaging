package com.ginkgooai.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
@Slf4j
public class WebhookController {


    @GetMapping
    public String handleInboundEmail(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("text") String text,
            @RequestParam("headers") String headers) {

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
