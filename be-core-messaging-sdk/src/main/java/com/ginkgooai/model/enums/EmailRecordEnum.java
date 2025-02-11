package com.ginkgooai.model.enums;


import lombok.Getter;

/**
 * @author Martin
 */

@Getter
public enum EmailRecordEnum {
    WAITING("WAITING", "未启动"),
    FAILED("FAILED", "失败"),
    SUCCESSFUL("SUCCESSFUL", "送达"),
    CANCELLED("CANCELLED", "退订"),
    ;

    private final String value;

    private final String description;

    EmailRecordEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

}
