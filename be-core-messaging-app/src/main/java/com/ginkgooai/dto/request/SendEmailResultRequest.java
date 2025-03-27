package com.ginkgooai.dto.request;

import com.ginkgooai.domain.Attachment;
import com.ginkgooai.domain.EmailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author: david
 * @date: 16:48 2025/2/10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailResultRequest {

    private EmailType emailType;
    private Map<String, String> placeholders;
    private List<String> emailsTo;
    private List<String> ccList;
    private List<String> bccList;
    private String subject;
    private String content;
    private List<Attachment> attachments;
    private String senderName;
    private String ipPoolName;
    private List<String> replyList;


}
