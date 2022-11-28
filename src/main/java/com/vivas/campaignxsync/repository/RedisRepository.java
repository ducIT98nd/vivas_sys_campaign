package com.vivas.campaignxsync.repository;

import com.vivas.campaignxsync.configuration.RedisPool;
import com.vivas.campaignxsync.dto.BlacklistUpdateDTO;
import com.vivas.campaignxsync.entity.Blacklist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.List;
import java.util.Map;

@Component
public class RedisRepository {

    private static final Logger logger = LoggerFactory.getLogger(RedisRepository.class);

    @Autowired
    private RedisPool redisPool;

    public boolean saveToRedisMNP(Map<String, String> instanceProp, int expired, String redis_key) {
        boolean result = true;
        try (Jedis jedis = redisPool.getRedisConnection()) {
            jedis.hmset(redis_key, instanceProp);
            jedis.expire(redis_key, expired);
        } catch (Exception e) {
            logger.error("exception when insert to redis with key: {}", redis_key, e);
            result = false;
        }
        return result;
    }

    public boolean saveToRedisMNP(Map<String, String> instanceProp, String redis_key) {
        boolean result = true;
        try (Jedis jedis = redisPool.getRedisConnection()) {
            jedis.hmset(redis_key, instanceProp);
        } catch (Exception e) {
            logger.error("exception when insert to redis with key: {}", redis_key, e);
            result = false;
        }
        return result;
    }

    public boolean isSetMember(String value, String redisKey) {
        boolean result;
        try (Jedis jedis = redisPool.getRedisConnection()) {
            result = jedis.sismember(redisKey, value);
            logger.info("Call redis {} done, return {}", value, result);
        } catch (Exception e) {
            logger.error("exception when check is member key {}, value {}", redisKey, value, e);
            result = false;
        }
        return result;
    }

    public boolean updateRedisSet(BlacklistUpdateDTO blacklistUpdateDTO, String redis_key) {
        boolean result = true;
        long redisReturn;
        try (Jedis jedis = redisPool.getRedisConnection()) {
            if (blacklistUpdateDTO.getAction().equals(1)){
                redisReturn = jedis.sadd(redis_key, blacklistUpdateDTO.getMsisdn());
                logger.info("Action = 1, add {} to blacklist, redis return {}", blacklistUpdateDTO.getMsisdn(), redisReturn);
            } else if (blacklistUpdateDTO.getAction().equals(0)) {
                redisReturn = jedis.srem(redis_key, blacklistUpdateDTO.getMsisdn());
                logger.info("Action = 0, remove {} to blacklist, redis return {}", blacklistUpdateDTO.getMsisdn(), redisReturn);
            } else {
                logger.info("Unknow action, msisdn {}", blacklistUpdateDTO.getMsisdn());
            }
        } catch (Exception e) {
            logger.error("exception when insert to redis with key: {}", redis_key, e);
            result = false;
        }
        return result;
    }


    public boolean bulkSaveBlacklist(List<Blacklist> listBlacklist, String keyPrefix) {
        boolean result = true;
        logger.info("Start save blacklist to redis, size={}", listBlacklist.size());
        try (Jedis jedis = redisPool.getRedisConnection()) {
            Pipeline pipeline = jedis.pipelined();
            for (Blacklist blacklist : listBlacklist) {
                String key = keyPrefix ;
                pipeline.sadd(key, blacklist.getMsisdn());
            }
        } catch (Exception e) {
            logger.error("exception when load all CX to redis with key", e);
            result = false;
        }
        return result;
    }

    public boolean deleteAllKey (String keyPrefix){
        String key = keyPrefix + "*";
        logger.debug("Delete all MNP, key pattern=" + keyPrefix);
        ScanParams scanParams = new ScanParams().count(5);
        scanParams.match(key);
        boolean result = true;
        String cur = ScanParams.SCAN_POINTER_START;
        try (Jedis jedis = redisPool.getRedisConnection()) {
            do {
                ScanResult<String> scanResult = jedis.scan(cur, scanParams);
                //store list keys
                for (String redis_key : scanResult.getResult()) {
                    jedis.del(redis_key);
                }
                cur = scanResult.getStringCursor();
            } while (!cur.equals(ScanParams.SCAN_POINTER_START));
        } catch (Exception e) {
            logger.error("exception when load all MNP to redis with key", e);
            result = false;
        }
        return result;
    }
}
