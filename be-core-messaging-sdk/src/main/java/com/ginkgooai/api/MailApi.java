package com.ginkgooai.api;

import com.ginkgooai.core.common.config.CustomErrorDecoder;
import com.ginkgooai.core.common.config.FeignMultipartConfig;
import com.ginkgooai.model.request.SendEmailResultRequest;
import com.ginkgooai.model.response.SendEmailResultResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: david
 * @date: 17:57 2025/2/10
 */
@FeignClient(name = "core-messaging",
        configuration = {CustomErrorDecoder.class, FeignMultipartConfig.class},
        url = "${MAIL_API_URL:}")
@Component
public interface MailApi {

    @PostMapping("/messaging/mail")
    SendEmailResultResponse sendMail(@RequestBody SendEmailResultRequest request);

}
