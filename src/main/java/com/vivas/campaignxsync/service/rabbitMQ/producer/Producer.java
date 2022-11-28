package com.vivas.campaignxsync.service.rabbitMQ.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;



@Service
public class Producer {
	private static Logger logger = LoggerFactory.getLogger(Producer.class);
    @Autowired
    private RabbitTemplate template;

    @Autowired
    @Qualifier(value = "regularQueue")
    private Queue regularQ;
    
//	public void publish(Map<String, NotifyEvent> params) {
//
//		try {
//			template.convertAndSend(regularQ.getName(), params);
//		} catch (Exception ex) {
//			logger.error(ex.getMessage(), ex);
//		}
//
//	}
}
