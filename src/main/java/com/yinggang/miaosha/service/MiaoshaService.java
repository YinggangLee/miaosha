package com.yinggang.miaosha.service;

import com.yinggang.miaosha.domain.MiaoshaUser;
import com.yinggang.miaosha.domain.OrderInfo;
import com.yinggang.miaosha.result.CodeMsg;
import com.yinggang.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

@Service
public class MiaoshaService {
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;


	//使用事务控制并发性，如果秒杀失败，则回滚数据，不再创建订单
	@Transactional
	public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
		//减库存 下订单 写入秒杀订单
		int a =goodsService.reduceStock(goods);
		//a=1说明成功减少库存，则可以生产订单
		//a!=1说明stock已经为0，不能生产订单
		if (a==1)
			return orderService.createOrder(user, goods);
		else
			return null;
	}


	//使用事务控制并发性，如果秒杀失败，则回滚数据，不再创建订单
	@Transactional
	public String miaoshaWithLock(Model model, MiaoshaUser user, long goodsId) {

		//判断库存
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		int stock = goods.getStockCount();
		if(stock <= 0) {
			model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
			return "miaosha_fail";
		}

//    	//判断是否已经秒杀到了
//    	MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
//    	if(order != null) {
//    		model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
//    		return "miaosha_fail";
//    	}

		//减库存 下订单 写入秒杀订单
		int a = goodsService.reduceStock(goods);

		if (a==1){
			OrderInfo orderInfo = orderService.createOrder(user, goods);
			model.addAttribute("orderInfo", orderInfo);
			model.addAttribute("goods", goods);
			return "order_detail";
		}else
		{
			model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
			return "miaosha_fail";
		}

	}

	
}
