package com.ginkgooai.util;

import com.ginkgooai.core.common.constant.MessageQueue;
import com.ginkgooai.core.common.queue.QueueInterface;
import com.ginkgooai.dto.MailSendMessage;
import com.sun.mail.imap.IMAPMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender {

    private final QueueInterface queueInterface;

    public void send(IMAPMessage message) {
        try {
            queueInterface.send(MessageQueue.EMAIL_SEND_QUEUE, MailSendMessage.fromMessage(message));
            log.debug("Email message enqueued successfully.");
        } catch (Exception e) {
            log.error("Failed to enqueue email message.", e);
        }
    }
}