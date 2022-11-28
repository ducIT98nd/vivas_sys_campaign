package com.vivas.campaignxsync.dto;

public class BlacklistUpdateDTO {

    String msisdn;
    Integer action;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }
}
