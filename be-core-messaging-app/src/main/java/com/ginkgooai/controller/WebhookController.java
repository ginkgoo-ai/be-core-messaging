package com.ginkgooai.controller;


import com.alibaba.fastjson.JSON;
import com.ginkgooai.dto.InboundParseRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Enumeration;
import java.util.List;

@RestController
@RequestMapping("/webhook")
@Slf4j
public class WebhookController {


    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> handleInboundParse(HttpServletRequest httpServletRequest,
                                                     // 解析表单字段（使用@RequestPart绑定到对象属性）
                                                     @RequestPart(value = "headers", required = false) String headers,
                                                     @RequestPart(value = "dkim", required = false) String dkim,
                                                     @RequestPart(value = "email", required = false) String email,
                                                     @RequestPart(value = "to",required = false) String to,
                                                     @RequestPart(value = "from",required = false) String from,
                                                     @RequestPart(value = "sender_ip", required = false) String senderIp,
                                                     @RequestPart(value = "spam_report", required = false) String spamReport,
                                                     @RequestPart(value = "envelope", required = false) String envelope,
                                                     @RequestPart(value = "subject",required = false) String subject,
                                                     @RequestPart(value = "spam_score", required = false) String spamScore,
                                                     @RequestPart(value = "charsets", required = false) String charsets,
                                                     @RequestPart(value = "SPF", required = false) String spf,
                                                     @RequestPart(value = "text", required = false) String text,
                                                     @RequestPart(value = "html", required = false) String html,
                                                     @RequestPart(value = "content-ids", required = false) List<String> contentIds,
                                                     @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments,
                                                     @RequestPart(value = "attachment-info", required = false) String attachmentInfo
                                                     ) {

        // 构建请求对象
        InboundParseRequest request = new InboundParseRequest();
        request.setHeaders(headers);
        request.setEmail(email);
        request.setDkim(dkim);
        request.setContentIds(contentIds);
        request.setTo(to);
        request.setFrom(from);
        request.setHtml(html);
        request.setText(text);
        request.setSenderIp(senderIp);
        request.setSpamReport(spamReport);
        request.setEnvelope(envelope);
        request.setAttachments(attachments);
        request.setSubject(subject);
        request.setSpamScore(spamScore);
        request.setAttachmentInfo(attachmentInfo);
        request.setCharsets(charsets);
        request.setSPF(spf);

        log.info("Inbound parse request received:{}", JSON.toJSONString(request));

        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = httpServletRequest.getHeader(headerName);
            log.info("Header: {} = {}", headerName, headerValue);
        }

        return ResponseEntity.ok("Success");
    }


    // 验证SendGrid签名（HMAC-SHA256）
    private boolean verifySignature(String headers) {
        log.info("headers:{}", headers);
        return true;
    }
}
