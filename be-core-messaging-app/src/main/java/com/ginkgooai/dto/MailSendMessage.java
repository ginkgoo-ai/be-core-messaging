package com.ginkgooai.dto;

import com.ginkgooai.core.common.queue.QueueMessage;
import com.sun.mail.imap.IMAPMessage;
import jakarta.mail.MessagingException;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class MailSendMessage extends QueueMessage implements Serializable {

    private String[] from;
    private String[] to;
    private String subject;
    private String body;


    public static MailSendMessage fromMessage(IMAPMessage message) throws MessagingException {
        return MailSendMessage.builder().build();

    }
}
