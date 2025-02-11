package com.ginkgooai.model.response;

import com.ginkgooai.model.enums.EmailRecordEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailResultResponse  {

    private String xMessageId;

    private String reason;

    private EmailRecordEnum status;

}
