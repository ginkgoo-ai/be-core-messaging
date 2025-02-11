package com.ginkgooai.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ginkgooai.core.common.exception.GinkgooRunTimeException;
import com.ginkgooai.core.common.exception.enums.CustomErrorEnum;
import com.ginkgooai.model.Attachment;
import com.ginkgooai.model.enums.EmailRecordEnum;
import com.ginkgooai.model.response.SendEmailResultResponse;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author: david
 * @date: 15:02 2025/2/10
 */

@Slf4j
@Component
public class SendGridUtil {

    private static final Integer STATUS_FAILED_CODES = 300;

    @Value("${SENDGRID_API_KEY:}")
    private String sendGridApiKey;

    @Value("${SENDGRID_SENDER_MAIL:}")
    private String sendGridSenderMail;

    @Value("${SENDGRID_SENDER_NAME:}")
    private String sendGridSenderName;


    public SendEmailResultResponse sendMail(List<String> emailsTo, List<String> ccList,List<String> replyList, String subject, String mailContent, List<Attachment> attachments, String senderName, String ipPoolName) throws GinkgooRunTimeException {
        SendEmailResultResponse sendEmailResultResponse = new SendEmailResultResponse();

        senderName = ObjectUtils.isEmpty(senderName) ? sendGridSenderName : senderName;

        Email from = new Email(sendGridSenderMail, senderName);
        Mail mail = new Mail();

        Personalization personalization = null;
        for (String s : emailsTo) {
            personalization = new Personalization();
            Email to = new Email(s);
            personalization.addTo(to);
            mail.addPersonalization(personalization);

        }
        if (personalization != null && !ObjectUtils.isEmpty(ccList)) {
            for (String s : ccList) {
                if (s.equalsIgnoreCase(personalization.getTos().getFirst().getEmail()))
                    continue;   // SendGrid set a constraint: Each email address in the personalization block should be unique between to, cc, and bcc.
                Email to = new Email(s);
                personalization.addCc(to);
            }
        }
        if (!ObjectUtils.isEmpty(attachments)) {
            attachments.forEach(attachment -> {
                Attachments.Builder builder = new Attachments.Builder(attachment.getName(), attachment.getInputStream());
                if (!StringUtils.isEmpty(attachment.getType())) {
                    builder.withType(attachment.getType());
                }
                mail.addAttachments(builder.build());
            });
        }

        Content content = new Content("text/html", mailContent);
        mail.setFrom(from);
        mail.setSubject(subject);
        mail.addContent(content);

        if(StringUtils.isNotEmpty(ipPoolName)) {
            mail.setIpPoolId(ipPoolName);
        }

        if(!CollectionUtils.isEmpty(replyList)) {
            mail.setReplyTo(new Email(replyList.getFirst()));
        }


        doSend(sendGridApiKey,mail,sendEmailResultResponse);
        return sendEmailResultResponse;
    }


    private boolean doSend(String apiKey,Mail mail,SendEmailResultResponse sendEmailResultResponse) throws GinkgooRunTimeException{
        log.info("Use api key ****{} , ip pool {}",apiKey.substring(apiKey.length()-4),mail.getIpPoolId());
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            if (response.getStatusCode() >= STATUS_FAILED_CODES) {
                log.warn("Send email error:{}",response.getBody());
                if(sendEmailResultResponse!=null){
                    sendEmailResultResponse.setStatus(EmailRecordEnum.FAILED);
                    JSONObject body= JSON.parseObject(response.getBody());
                    JSONArray errors=body.getJSONArray("errors");
                    sendEmailResultResponse.setReason(CollectionUtils.isEmpty(errors) ?body.toJSONString():errors.getJSONObject(0).getString("message"));
                    return false;
                }else{
                    throw new GinkgooRunTimeException(CustomErrorEnum.EMAIL_SEND_EXCEPTION);
                }

            } else {
                if(sendEmailResultResponse!=null){
                    sendEmailResultResponse.setXMessageId(response.getHeaders().get("X-Message-Id"));
                    sendEmailResultResponse.setStatus(EmailRecordEnum.SUCCESSFUL);
                }
                return true;
            }
        } catch (IOException ex) {
            log.error("send mail error", ex);
            throw new GinkgooRunTimeException(CustomErrorEnum.EMAIL_SEND_EXCEPTION);
        }
    }

}
