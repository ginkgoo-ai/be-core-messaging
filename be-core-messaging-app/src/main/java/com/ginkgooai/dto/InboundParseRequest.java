package com.ginkgooai.dto;

// 新增字段
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundParseRequest {
    private MimeMessage mimeMessage;

    private String headers;

    private String dkim;

    private String email;

    private List<String> contentIds;

    private String to;

    private String from;

    private String html;

    private String text;

    @JsonProperty("sender_ip")
    private String senderIp;

    @JsonProperty("spam_report")
    private String spamReport;

    private String envelope;

    private List<MultipartFile> attachments;

    private String subject;

    @JsonProperty("spam_score")
    private String spamScore;

    @JsonProperty("attachment-info")
    private String attachmentInfo;

    private String charsets;

    private String SPF;
}
