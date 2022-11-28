package com.vivas.campaignxsync.dto;

public class BlacklistFailed {

    private BlacklistUpdateDTO blacklistUpdateDTO;
    private int retryCount;

    public BlacklistFailed(BlacklistUpdateDTO blacklistUpdateDTO, int retryCount) {
        this.blacklistUpdateDTO = blacklistUpdateDTO;
        this.retryCount = retryCount;
    }

    public BlacklistUpdateDTO getBlacklistUpdateDTO() {
        return blacklistUpdateDTO;
    }

    public void setBlacklistUpdateDTO(BlacklistUpdateDTO blacklistUpdateDTO) {
        this.blacklistUpdateDTO = blacklistUpdateDTO;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public String toString() {
        return "BlacklistFailed{" +
                "blacklistUpdateDTO=" + blacklistUpdateDTO +
                ", retryCount=" + retryCount +
                '}';
    }
}
