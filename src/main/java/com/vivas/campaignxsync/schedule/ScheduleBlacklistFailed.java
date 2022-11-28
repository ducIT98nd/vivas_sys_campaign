package com.vivas.campaignxsync.schedule;

import com.vivas.campaignxsync.utils.ContainerBlacklistFailed;
import com.vivas.campaignxsync.service.BlacklistService;
import com.vivas.campaignxsync.dto.BlacklistFailed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ScheduleBlacklistFailed {
    private static Logger logger = LoggerFactory.getLogger(ScheduleBlacklistFailed.class);

    @Value("${redis-config.key-prefix-blacklist}")
    private String keyPrefixBlacklist;

    @Value("${schedule-config.retry-config}")
    private int retryConfig;

    @Autowired
    private ContainerBlacklistFailed containerBlacklistFailed;

    @Autowired
    private BlacklistService blacklistService;

    @Scheduled(cron = "${schedule-config.blacklist-failed-retry}")
    public void handleBlacklistDatabaseFailed() {
        HashSet<BlacklistFailed> list = containerBlacklistFailed.blacklistFailedDatabase;
        logger.info("have {} msisdns insert failed to database blacklist", list.size());
        boolean ck;
        for (BlacklistFailed blacklistFailed : list) {
            try {
                logger.info("Retry update database blacklist {}", blacklistFailed.getBlacklistUpdateDTO().toString());
                blacklistService.updateToDatabase(blacklistFailed.getBlacklistUpdateDTO());
                containerBlacklistFailed.blacklistFailedDatabase.remove(blacklistFailed);
            } catch (Exception ex){
                logger.error("Error retry blacklist", ex);
                int count = blacklistFailed.getRetryCount();
                count++;
                logger.info("retry count={} of msisdn={}", count, blacklistFailed.getBlacklistUpdateDTO().getMsisdn());
                if (count < retryConfig) {
                    blacklistFailed.setRetryCount(count);
                    containerBlacklistFailed.blacklistFailedDatabase.add(blacklistFailed);
                } else {
                    logger.error("Exceeded retry count, retry count current is {}, cannot update blacklist of msisdn: {}, service number: {} to database"
                            , count, blacklistFailed.getBlacklistUpdateDTO().getMsisdn());
                    containerBlacklistFailed.blacklistFailedDatabase.remove(blacklistFailed);
                }

            }
        }
    }

    @Scheduled(cron = "${schedule-config.blacklist-failed-retry}")
    public void handleMnpRedis() {
        HashSet<BlacklistFailed> list = containerBlacklistFailed.blacklistFailedRedis;
        Set<BlacklistFailed> listDone = new HashSet<>();
        logger.info("have {} msisdn insert failed to redis blacklist", list.size());
        boolean ck;
        for (BlacklistFailed blacklistFailed : list) {
            logger.info("Retry update redis blacklist {}", blacklistFailed.getBlacklistUpdateDTO().toString());
            ck = blacklistService.updateRedisBlacklist(blacklistFailed.getBlacklistUpdateDTO(), keyPrefixBlacklist);
            if (ck) {
                listDone.add(blacklistFailed);
            } else {
                int count = blacklistFailed.getRetryCount();
                count++;
                if (count < retryConfig) {
                    blacklistFailed.setRetryCount(count);
                    containerBlacklistFailed.blacklistFailedRedis.add(blacklistFailed);
                } else {
                    logger.error("Exceeded retry count, retry count current is {}, cannot update blacklist of msisdn: {}, service number: {} to redis"
                            , count, blacklistFailed.getBlacklistUpdateDTO().getMsisdn());
                    listDone.add(blacklistFailed);
                }
            }
        }
        containerBlacklistFailed.blacklistFailedRedis.removeAll(listDone);
    }
}
