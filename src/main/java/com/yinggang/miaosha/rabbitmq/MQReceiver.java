package com.yinggang.miaosha.rabbitmq;

import com.yinggang.miaosha.domain.MiaoshaOrder;
import com.yinggang.miaosha.domain.MiaoshaUser;
import com.yinggang.miaosha.redis.RedisService;
import com.yinggang.miaosha.service.GoodsService;
import com.yinggang.miaosha.service.MiaoshaService;
import com.yinggang.miaosha.vo.GoodsVo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    @Autowired
    GoodsService goodsService;

    @Autowired
    MiaoshaService miaoshaService;





    @RabbitListener(queues = MQConfige.MIAOSHA_QUEUE)
    public void miaoshaReceive(String message){
        MiaoshaMeesage miaoshaMeesage = RedisService.stringToBean(message,MiaoshaMeesage.class);
        MiaoshaUser user = miaoshaMeesage.getUser();
        long goodId = miaoshaMeesage.getGoodId();

        //操作数据库
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodId);
        int stock = goods.getStockCount();
        if(stock <= 0) {
            return;
        }
        //减库存 下订单 写入秒杀订单
        miaoshaService.miaosha(user, goods);

        //System.out.println(message);
    }




}
