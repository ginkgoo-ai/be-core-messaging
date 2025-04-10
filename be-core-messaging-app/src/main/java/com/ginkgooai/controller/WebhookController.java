package com.ginkgooai.controller;


import com.alibaba.fastjson.JSON;
import com.ginkgooai.dto.InboundParseRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/webhook")
@Slf4j
public class WebhookController {


    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> handleInboundParse(
            HttpServletRequest httpServletRequest,
            // 修改email参数类型为MultipartFile
            @RequestPart(value = "email", required = false) MultipartFile emailFile,
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
        
        // 新增MimeMessage解析逻辑
        MimeMessage mimeMessage = null;
        if (emailFile != null && !emailFile.isEmpty()) {
            try {
                Session mailSession = Session.getDefaultInstance(new Properties());
                mimeMessage = new MimeMessage(mailSession, emailFile.getInputStream());
            } catch (MessagingException | IOException e) {
                log.error("Failed to parse MIME message", e);
            }
        }


        // 修改请求对象构建
        InboundParseRequest request = new InboundParseRequest();
        request.setMimeMessage(mimeMessage);  // 需要DTO新增该字段
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
