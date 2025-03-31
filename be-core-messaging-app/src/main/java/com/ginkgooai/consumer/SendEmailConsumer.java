package com.ginkgooai.consumer;

import com.ginkgooai.core.common.constant.MessageQueue;
import com.ginkgooai.core.common.message.InnerMailSendMessage;
import com.ginkgooai.core.common.queue.QueueInterface;
import com.ginkgooai.dto.request.SendEmailResultInnerRequest;
import com.ginkgooai.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
@RequiredArgsConstructor
public class SendEmailConsumer {

    private final QueueInterface queueInterface;
    private final MailService mailService;
    private final ThreadPoolTaskExecutor emailTaskExecutor;

    @Value("${send.email.consumer.batch-size:10}")
    private int batchSize;

    @Scheduled(fixedDelayString = "${send.email.consumer.polling-interval:1000}")
    public void consumeInnerEmail() {
        List<InnerMailSendMessage> batch = queueInterface.getMessages(MessageQueue.EMAIL_SEND_QUEUE, batchSize, InnerMailSendMessage.class);
        
        if (batch.isEmpty()) {
            return;
        }

        CompletableFuture<?>[] futures = batch.stream()
                .map(mailSendMessage -> CompletableFuture.runAsync(() -> {
                    try {
                        mailSendMessage.getReceipts().forEach(receipt -> {
                            SendEmailResultInnerRequest request = SendEmailResultInnerRequest.builder()
                                    .emailType(mailSendMessage.getEmailTemplateType())
                                    .placeholders(receipt.getPlaceholders())
                                    .emailsTo(List.of(receipt.getTo()))
                                    .build();
                            mailService.sendMailInner(request);
                        });
                    } catch (Exception e) {
                        log.error("Error processing email message: {}", mailSendMessage, e);
                    }
                }, emailTaskExecutor))
                .toArray(CompletableFuture[]::new);

        try {
            CompletableFuture.allOf(futures).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error waiting for email tasks to complete", e);
            Thread.currentThread().interrupt();
        }
    }
}
