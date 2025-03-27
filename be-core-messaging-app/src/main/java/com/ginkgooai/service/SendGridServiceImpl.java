package com.ginkgooai.service;

import com.ginkgooai.core.common.exception.ResourceNotFoundException;
import com.ginkgooai.domain.Attachment;
import com.ginkgooai.domain.EmailTemplate;
import com.ginkgooai.dto.request.SendEmailResultRequest;
import com.ginkgooai.model.response.SendEmailResultResponse;
import com.ginkgooai.repository.EmailTemplateRepository;
import com.ginkgooai.util.SendGridUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Implementation of MailService using SendGrid for email delivery
 * @author: david
 * @date: 14:29 2025/2/10
 */
@Service
@RequiredArgsConstructor
public class SendGridServiceImpl implements MailService {

    private final SendGridUtil sendGridUtil;
    private final EmailTemplateRepository emailTemplateRepository;

    /**
     * Replace placeholders in the template with actual values
     * @param template The template string containing placeholders
     * @param placeholders Map of placeholder keys and their values
     * @return The processed string with all placeholders replaced
     */
    private String replacePlaceholders(String template, Map<String, String> placeholders) {
        if (template == null || placeholders == null) {
            return template;
        }
        String result = template;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

    @Override
    public SendEmailResultResponse sendMail(List<String> emailsTo, List<String> ccList, List<String> replyList, String subject, String mailContent, List<Attachment> attachments, String senderName, String ipPoolName) {
        return sendGridUtil.sendMail(emailsTo, ccList, replyList, subject, mailContent, attachments, senderName, ipPoolName);
    }

    @Override
    public SendEmailResultResponse sendMail(SendEmailResultRequest request) {
        // Find the email template by emailType
        EmailTemplate template = emailTemplateRepository.findByEmailType(request.getEmailType())
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Email Template", "emailType", request.getEmailType().toString()));

        // Replace placeholders in both content and subject
        String content = replacePlaceholders(template.getContent(), request.getPlaceholders());
        String subject = StringUtils.hasText(request.getSubject()) 
                ? replacePlaceholders(request.getSubject(), request.getPlaceholders())
                : replacePlaceholders(template.getSubject(), request.getPlaceholders());

        // Call the original sendMail method to send the email
        return sendMail(
                request.getEmailsTo(),
                request.getCcList(),
                request.getReplyList(),
                subject,
                content,
                request.getAttachments(),
                request.getSenderName(),
                request.getIpPoolName()
        );
    }
}
