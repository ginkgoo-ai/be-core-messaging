package com.ginkgooai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundParseRequest {

    private String headers;

    private String dkim;

    @JsonProperty("content-ids")
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

    private String attachments;

    private String subject;

    @JsonProperty("spam_score")
    private String spamScore;

    @JsonProperty("attachment-info")
    private String attachmentInfo;

    private String charsets;

    private String SPF;
}
