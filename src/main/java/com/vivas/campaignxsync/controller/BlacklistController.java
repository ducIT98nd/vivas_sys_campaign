package com.vivas.campaignxsync.controller;

import com.vivas.campaignxsync.common.AppException;
import com.vivas.campaignxsync.common.BlacklistErrorCodeEnum;
import com.vivas.campaignxsync.dto.BlacklistUpdateDTO;
import com.vivas.campaignxsync.dto.MsisdnDTO;
import com.vivas.campaignxsync.dto.ResponseDTO;
import com.vivas.campaignxsync.service.BlacklistService;
import com.vivas.campaignxsync.utils.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api")
public class BlacklistController {
    private static final Logger logger = LoggerFactory.getLogger(BlacklistController.class);

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    private AppUtils appUtils;

    @GetMapping(value = "/reload-blacklist")
    public ResponseEntity<String> reloadmnp() {
        logger.info("recieved request reload-blacklist");
        new Thread(new Runnable() {
            @Override
            public void run() {
                blacklistService.reloadBlacklist();
            }
        }).start();
        return ResponseEntity.ok("Recieved....");
    }

    @PostMapping(value = "/check-blacklist")
    public ResponseEntity check(@RequestBody MsisdnDTO msisdnDTO) {
        logger.info("recieved request check-blacklist");
        ResponseDTO responseDTO;
        try {
            responseDTO = blacklistService.checkBlacklist(msisdnDTO.getMsisdn());
            logger.info("Check blacklist {} finish, response {}", msisdnDTO.getMsisdn(), responseDTO.toString());
        } catch (Exception ex) {
            logger.error("Error while check blacklist: ", ex);
            Throwable rootcause = appUtils.getrootcause(ex);
            responseDTO = new ResponseDTO();
            if (rootcause instanceof AppException) {
                AppException apex = (AppException) rootcause;
                responseDTO.setCode(apex.getCode());
                responseDTO.setMessage(apex.getMessage());
            } else {
                responseDTO.setMessage(BlacklistErrorCodeEnum.SYSTEM_ERROR.getMessage());
                responseDTO.setCode(BlacklistErrorCodeEnum.SYSTEM_ERROR.getCode());
            }
        }
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping(value = "/update-blacklist", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity blacklistUpdate(@RequestBody BlacklistUpdateDTO blacklistUpdateDTO) {
        logger.info("recieved request check-blacklist");
        ResponseDTO responseDTO;
        try {
            responseDTO = blacklistService.updateBlacklist(blacklistUpdateDTO);
            logger.info("update blacklist {} finish, response {}", blacklistUpdateDTO.getMsisdn(), responseDTO.toString());
        } catch (Exception ex) {
            logger.error("Error while check blacklist: ", ex);
            Throwable rootcause = appUtils.getrootcause(ex);
            responseDTO = new ResponseDTO();
            if (rootcause instanceof AppException) {
                AppException apex = (AppException) rootcause;
                responseDTO.setCode(apex.getCode());
                responseDTO.setMessage(apex.getMessage());
            } else {
                responseDTO.setMessage(BlacklistErrorCodeEnum.SYSTEM_ERROR.getMessage());
                responseDTO.setCode(BlacklistErrorCodeEnum.SYSTEM_ERROR.getCode());
            }
        }
        return ResponseEntity.ok(responseDTO);
    }

}
