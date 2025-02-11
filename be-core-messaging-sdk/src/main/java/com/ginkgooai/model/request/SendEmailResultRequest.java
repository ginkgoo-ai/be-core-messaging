package com.ginkgooai.model.request;

import com.ginkgooai.model.Attachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: david
 * @date: 16:48 2025/2/10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailResultRequest {

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
