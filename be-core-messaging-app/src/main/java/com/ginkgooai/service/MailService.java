package com.ginkgooai.service;

import com.ginkgooai.domain.Attachment;
import com.ginkgooai.dto.request.SendEmailResultRequest;
import com.ginkgooai.model.response.SendEmailResultResponse;

import java.util.List;

/**
 * @author: david
 * @date: 14:27 2025/2/10
 */
public interface MailService {

    SendEmailResultResponse sendMail(List<String> emailsTo, List<String> ccList, List<String> replyList, String subject, String mailContent, List<Attachment> attachments, String senderName, String ipPoolName);

    SendEmailResultResponse sendMail(SendEmailResultRequest request);

}
