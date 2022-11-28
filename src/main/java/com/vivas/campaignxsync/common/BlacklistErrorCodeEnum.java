package com.vivas.campaignxsync.common;

public enum BlacklistErrorCodeEnum {

    SUCCESS(1, "Thành công"),
    SYSTEM_ERROR(-1, "Thất bại"),
    MSISDN_NOT_EXISTED(0, "SDT không thuộc blacklist"),
    MSISDN_EXISTED(1, "SDT thuộc blacklist");
    private Integer code;
    private String message;

    BlacklistErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
