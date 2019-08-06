package com.yinggang.miaosha.rabbitmq;

import com.yinggang.miaosha.domain.MiaoshaUser;

public class MiaoshaMeesage {
    private MiaoshaUser user;
    private long goodId;

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public long getGoodId() {
        return goodId;
    }

    public void setGoodId(long goodId) {
        this.goodId = goodId;
    }
}
