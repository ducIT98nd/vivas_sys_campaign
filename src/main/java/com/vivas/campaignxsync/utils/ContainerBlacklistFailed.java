package com.vivas.campaignxsync.utils;

import com.vivas.campaignxsync.dto.BlacklistFailed;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class ContainerBlacklistFailed {
    public HashSet<BlacklistFailed> blacklistFailedDatabase = new HashSet<>();
    public HashSet<BlacklistFailed> blacklistFailedRedis = new HashSet<>();

    public HashSet<BlacklistFailed> getBlacklistFailedDatabase() {
        return blacklistFailedDatabase;
    }

    public void setBlacklistFailedDatabase(HashSet<BlacklistFailed> blacklistFailedDatabase) {
        this.blacklistFailedDatabase = blacklistFailedDatabase;
    }

    public HashSet<BlacklistFailed> getBlacklistFailedRedis() {
        return blacklistFailedRedis;
    }

    public void setBlacklistFailedRedis(HashSet<BlacklistFailed> blacklistFailedRedis) {
        this.blacklistFailedRedis = blacklistFailedRedis;
    }
}
