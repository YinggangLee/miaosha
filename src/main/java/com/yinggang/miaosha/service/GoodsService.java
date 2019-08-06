package com.yinggang.miaosha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yinggang.miaosha.dao.GoodsDao;
import com.yinggang.miaosha.domain.MiaoshaGoods;
import com.yinggang.miaosha.vo.GoodsVo;

@Service
public class GoodsService {
	
	@Autowired
	GoodsDao goodsDao;
	
	public List<GoodsVo> listGoodsVo(){
		return goodsDao.listGoodsVo();
	}

	public GoodsVo getGoodsVoByGoodsId(long goodsId) {
		return goodsDao.getGoodsVoByGoodsId(goodsId);
	}

	public int  reduceStock(GoodsVo goods) {
		MiaoshaGoods g = new MiaoshaGoods();
		g.setGoodsId(goods.getId());

		//返回1说明更新了数据，成功减少了stock，影响了1行
		//返回0说明没有更新数据，因为stock已经为0，防止了超卖
		int a = goodsDao.reduceStock(g);

        return a;
    }
	
	
	
}
