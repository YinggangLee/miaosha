package com.yinggang.miaosha.rabbitmq;

import com.yinggang.miaosha.redis.RedisService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate ;

    public void sendMiaoshaMessage(MiaoshaMeesage miaoshaMeesage){
        String msg = RedisService.beanToString(miaoshaMeesage);
        amqpTemplate.convertAndSend(MQConfige.MIAOSHA_QUEUE,msg);
    }

}
