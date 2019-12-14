package com.soecode.lyf.vo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author Administrator
 */
public class BaseVo {
    @NotBlank(message = "appKey不能为空")
    private String appKey;
    @NotNull(message = "timestamp必传")
    private Long timestamp;
    @NotBlank(message = "签名不能为空")
    private String sign;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
