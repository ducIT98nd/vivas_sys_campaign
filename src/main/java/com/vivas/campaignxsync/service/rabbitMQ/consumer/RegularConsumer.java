package com.vivas.campaignxsync.service.rabbitMQ.consumer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(value = "rabbitTransactionManager")
public class RegularConsumer extends BaseConsumer {
//	@RabbitListener(queues = "${regular-queuename}")
//    public void receive(Map<String, NotifyEvent> params) {
//        executor.execute(() -> {
//            doWork(params);
//        });
//    }
}
