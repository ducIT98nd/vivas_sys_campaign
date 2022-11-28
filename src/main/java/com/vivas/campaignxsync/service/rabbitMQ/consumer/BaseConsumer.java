package com.vivas.campaignxsync.service.rabbitMQ.consumer;


import com.vivas.campaignxsync.configuration.RabbitmqConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

@Service
public class BaseConsumer {
	protected final Logger logger = LogManager.getLogger(this.getClass().getName());
	@Autowired
	@Qualifier("worker")
	protected Executor executor;
	@Autowired
	protected RabbitAdmin rabbitAdmin;
	@Autowired
	protected RabbitTemplate template;
	@Autowired
	RabbitmqConfig rabbitmqConfig;

//	protected void doWork(Map<String, NotifyEvent> params) {
//		StopWatch duration = new StopWatch("duration");
//		duration.start();
//		NotifyEvent obj = params.get("event");
//		logger.info("notify regis: {}", obj.toString());
//		String key = obj.getServiceCode() + "_" + obj.getPackageCode();
//		HashMap<String, List<String>> subPackageMap = subpackageService.getSubPackageCodeInService();
//		Set<String> setService =  subPackageMap.keySet();
//		if (setService.contains(key)){
//			if (obj.getRegType().equals("1")) {
//				List<String> vdmSubpackageCode = subPackageMap.get(key);
//				if (vdmSubpackageCode.contains(obj.getSubpackageCode())){
//					NotifyVascloud obj1 = new NotifyVascloud(obj.getErrorDesc(), obj.getQueueID(), obj.getResultCode(),
//							obj.getStartTime(), obj.getStartTimeCP(), obj.getCpURL(), obj.getRegID(), obj.getMsisdn(),
//							obj.getRegType(), obj.getChannel(), obj.getService_id(), obj.getPackage_id(),
//							obj.getOriginalprice(), obj.getPrice(), obj.getCommandcode(), obj.getServiceCode(),
//							obj.getPackageCode(), obj.getSubpackageCode(), obj.getAutoRenew(), obj.getSubcribeTime(),
//							obj.getExpiredTime(), obj.getUpdateTime(), new Date());
//
//					if (obj1.getChannel().equalsIgnoreCase("SMS")){
//						obj1 = channelFindingService.findChannelForSmsNotify(obj1);
//					} else {
//						obj1.setVdmRegChannel(convertVDMRegChannel(obj1.getChannel()));
//					}
//
//					notifyEventService.saveNotify(obj1);
//					if (subcriberService.countActiveUserByMsisdnAndSubpackageCode(obj.getMsisdn(),
//							obj.getSubpackageCode()) <= 0) {
//						Subcriber sub = new Subcriber();
//						sub.setMsisdn(obj.getMsisdn());
//						sub.setPackageCode(obj.getPackageCode());
//						sub.setRegDate(new Date());
//						sub.setRegId(obj.getRegID());
//						sub.setRenewDate(new Date());
//						sub.setServiceCode(obj.getServiceCode());
//						sub.setStatus("1");
//						sub.setSubpackageCode(obj.getSubpackageCode());
//						sub.setSystem("Vascloud");
//						subcriberService.save(sub);
//					} else {
//						logger.info("phone number registered for the service");
//					}
//					duration.stop();
//					logger.info("finish save notify event to DB {}, time processed: {}s", obj.toString(),
//							duration.getTotalTimeSeconds());
//				}
//			} else if (obj.getRegType().equals("2")) {
//				logger.info("notify cancel: ");
//				if (notifyEventService.checkRegis(obj.getRegID())) {
//					NotifyVascloud obj1 = new NotifyVascloud(obj.getErrorDesc(), obj.getQueueID(), obj.getResultCode(),
//							obj.getStartTime(), obj.getStartTimeCP(), obj.getCpURL(), obj.getRegID(), obj.getMsisdn(),
//							obj.getRegType(), obj.getChannel(), obj.getService_id(), obj.getPackage_id(),
//							obj.getOriginalprice(), obj.getPrice(), obj.getCommandcode(), obj.getServiceCode(),
//							obj.getPackageCode(), obj.getSubpackageCode(), obj.getAutoRenew(), obj.getSubcribeTime(),
//							obj.getExpiredTime(), obj.getUpdateTime(), new Date());
//					//
//					notifyEventService.saveNotify(obj1);
//				}
//			}
//		}
//	}
	private Long convertVDMRegChannel (String channel) {

		switch (channel) {
			case "SMS": return 1l;
			case "USSD": return 2l;
			case "IVR": return 3l;
			case "MMS": return 4l;
			case "WAP": return 5l;
			default: return 1l;
		}
	}
}
