package com.ginkgooai.service;

import com.ginkgooai.model.Attachment;
import com.ginkgooai.model.response.SendEmailResultResponse;
import com.ginkgooai.util.SendGridUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: david
 * @date: 14:29 2025/2/10
 */
@Service
@RequiredArgsConstructor
public class SendGridServiceImpl implements MailService{

    private final SendGridUtil sendGridUtil;

    @Override
    public SendEmailResultResponse sendMail(List<String> emailsTo, List<String> ccList, List<String> replyList, String subject, String mailContent, List<Attachment> attachments, String senderName, String ipPoolName) {

        return sendGridUtil.sendMail(emailsTo, ccList, replyList, subject, mailContent, attachments, senderName, ipPoolName);
    }
}
