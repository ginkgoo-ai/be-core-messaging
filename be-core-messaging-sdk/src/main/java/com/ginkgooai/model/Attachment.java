package com.ginkgooai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

    // 附件内容
    private InputStream inputStream;

    // 附件名称
    private String name;

    // 附件类型
    private String type;
}
