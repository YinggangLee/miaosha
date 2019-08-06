package com.yinggang.miaosha.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfige {

    public static final String MIAOSHA_QUEUE = "miaosha.queue";

    @Bean
    public Queue getQueue(){
        return new Queue(MIAOSHA_QUEUE,true);
    }






}
