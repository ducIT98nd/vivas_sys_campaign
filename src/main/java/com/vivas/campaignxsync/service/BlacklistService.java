package com.vivas.campaignxsync.service;

import com.vivas.campaignxsync.common.AppException;
import com.vivas.campaignxsync.common.BlacklistErrorCodeEnum;
import com.vivas.campaignxsync.dto.BlacklistUpdateDTO;
import com.vivas.campaignxsync.dto.ResponseDTO;
import com.vivas.campaignxsync.utils.AppUtils;
import com.vivas.campaignxsync.utils.ContainerBlacklistFailed;
import com.vivas.campaignxsync.entity.Blacklist;
import com.vivas.campaignxsync.repository.BlacklistRepository;
import com.vivas.campaignxsync.repository.RedisRepository;
import com.vivas.campaignxsync.dto.BlacklistFailed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class BlacklistService {
    private static final Logger logger = LoggerFactory.getLogger(BlacklistService.class);

    @Value("${redis-config.key-prefix-blacklist}")
    private String keyPrefixBlacklist;

    @Value("${schedule-config.retry-config}")
    private int retryConfig;

    @Autowired
    private BlacklistRepository blacklistRepository;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private ContainerBlacklistFailed containerBlacklistFailed;

    public ResponseDTO checkBlacklist(String msisdn) {
        ResponseDTO responseDTO = new ResponseDTO();
        boolean result =  redisRepository.isSetMember(msisdn, keyPrefixBlacklist);
        if (result == false){
            responseDTO.setCode(BlacklistErrorCodeEnum.MSISDN_NOT_EXISTED.getCode());
            responseDTO.setMessage(BlacklistErrorCodeEnum.MSISDN_NOT_EXISTED.getMessage());
        } else {
            responseDTO.setCode(BlacklistErrorCodeEnum.MSISDN_EXISTED.getCode());
            responseDTO.setMessage(BlacklistErrorCodeEnum.MSISDN_EXISTED.getMessage());
        }
        return responseDTO;
    }

    public ResponseDTO updateBlacklist(BlacklistUpdateDTO blacklistUpdateDTO) throws AppException {
        ResponseDTO responseDTO = new ResponseDTO();
        BlacklistFailed blacklistFailed = null;
        try {
            updateToDatabase(blacklistUpdateDTO);
            updateToRedis(blacklistUpdateDTO);

            responseDTO.setCode(BlacklistErrorCodeEnum.SUCCESS.getCode());
            responseDTO.setMessage(BlacklistErrorCodeEnum.SUCCESS.getMessage());
            return responseDTO;
        } catch (Exception ex) {
            logger.error("Exception when update blacklist {}", blacklistUpdateDTO.toString(), ex);
            Throwable rootcause = appUtils.getrootcause(ex);
            responseDTO = new ResponseDTO();
            if (rootcause instanceof AppException) {
                throw ex;
            } else {
                throw new AppException(BlacklistErrorCodeEnum.SYSTEM_ERROR.getCode(), BlacklistErrorCodeEnum.SYSTEM_ERROR.getMessage());
            }
        }
    }

    public void updateToDatabase(BlacklistUpdateDTO blacklistUpdateDTO) throws AppException {
        try {
            Optional<Blacklist> temp = blacklistRepository.findByMsisdn(blacklistUpdateDTO.getMsisdn());
            Timestamp dateCurrent = new Timestamp(new java.util.Date().getTime());
            Blacklist blacklist;

            if (temp.isPresent()) {
                if (blacklistUpdateDTO.getAction().equals(1)){
                    if (temp.isPresent()) {
                        blacklist = temp.get();
                        blacklist.setUpdatedDate(dateCurrent);
                    } else {
                        blacklist = new Blacklist();
                        blacklist.setMsisdn(blacklistUpdateDTO.getMsisdn());
                        blacklist.setCreatedDate(dateCurrent);
                        blacklist.setUpdatedDate(dateCurrent);
                    }
                    blacklistRepository.save(blacklist);

                } else if (blacklistUpdateDTO.getAction().equals(0)) {
                    blacklist = temp.get();
                    blacklist.setUpdatedDate(dateCurrent);
                    blacklistRepository.delete(blacklist);
                }
            } else {
                logger.info("Unknow action, msisdn {}", blacklistUpdateDTO.getMsisdn());
            }
            logger.info("save success blacklist msisdn: {}, action: {}", blacklistUpdateDTO.getMsisdn(), blacklistUpdateDTO.getAction());
        } catch (Exception ex){
            if (retryConfig > 0) {
                BlacklistFailed blacklistFailed = new BlacklistFailed(blacklistUpdateDTO, 0);
                containerBlacklistFailed.blacklistFailedDatabase.add(blacklistFailed);
                logger.info("add to list failed insert database blacklist");
            }
        }
    }


    public void updateToRedis(BlacklistUpdateDTO blacklistUpdateDTO) {
        String redisKey = keyPrefixBlacklist;

        boolean result = updateRedisBlacklist(blacklistUpdateDTO, redisKey);
        logger.info("update blacklist redis, msisdn = {}, key = {}, result ={}", blacklistUpdateDTO.getMsisdn(), redisKey, result);
        if (!result && retryConfig > 0) {
            logger.info("update blacklist redis fail, add to retry, msisdn = {}, result ={}", blacklistUpdateDTO.getMsisdn(), result);
            BlacklistFailed blacklistFailed = new BlacklistFailed(blacklistUpdateDTO, 0);
            containerBlacklistFailed.blacklistFailedRedis.add(blacklistFailed);
        }
    }

    public boolean updateRedisBlacklist(BlacklistUpdateDTO blacklistUpdateDTO, String redisKey) {
        return redisRepository.updateRedisSet(blacklistUpdateDTO, redisKey);
    }

    public void reloadBlacklist(){
        logger.info("Start reload Blacklist..");
        boolean result = redisRepository.deleteAllKey(keyPrefixBlacklist);
        logger.info("Finished remove all blacklist key redis, result={}", result);
        loadAllBlacklist();
    }

    private void loadAllBlacklist(){
        Page<Blacklist> page = blacklistRepository.findAll(PageRequest.of(0, 50000));
        Integer totalPages = page.getTotalPages();
        logger.info("Start reload all black to redis, total pages={}", totalPages);
        boolean result = false;
        for (int i = 0; i < totalPages; i++) {
            result = redisRepository.bulkSaveBlacklist(page.toList(), keyPrefixBlacklist);
            page = blacklistRepository.findAll(PageRequest.of(i + 1, 50000));
        }
        logger.info("Finished load all MNP to redis, result={}", result);
    }
}
