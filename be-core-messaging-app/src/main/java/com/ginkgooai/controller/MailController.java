package com.ginkgooai.controller;

import com.ginkgooai.core.common.exception.GinkgooRunTimeException;
import com.ginkgooai.model.request.SendEmailResultRequest;
import com.ginkgooai.model.response.SendEmailResultResponse;
import com.ginkgooai.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author: david
 * @date: 16:46 2025/2/10
 */

@RestController
@RequestMapping("/messaging/mail")
@EnableFeignClients
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    /**
     * Sends an email based on the provided request.
     *
     * @param request The request object containing the details of the email to be sent.
     * @return A response object indicating the result of the email sending operation.
     * @throws GinkgooRunTimeException If an error occurs during the email sending process.
     */

    @PostMapping
    public ResponseEntity<SendEmailResultResponse> sendMail(@RequestBody SendEmailResultRequest request) {
        return ResponseEntity.ok(mailService.sendMail(request.getEmailsTo(),
                request.getCcList(),
                request.getReplyList(),
                request.getSubject(),
                request.getContent(),
                request.getAttachments(),
                request.getSenderName(),
                request.getIpPoolName()));
    }

}
