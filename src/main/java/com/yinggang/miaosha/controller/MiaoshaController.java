package com.yinggang.miaosha.controller;

import com.yinggang.miaosha.domain.MiaoshaUser;
import com.yinggang.miaosha.domain.OrderInfo;
import com.yinggang.miaosha.rabbitmq.MQSender;
import com.yinggang.miaosha.rabbitmq.MiaoshaMeesage;
import com.yinggang.miaosha.redis.GoodsKey;
import com.yinggang.miaosha.redis.RedisService;
import com.yinggang.miaosha.result.Result;
import com.yinggang.miaosha.service.GoodsService;
import com.yinggang.miaosha.service.MiaoshaService;
import com.yinggang.miaosha.service.MiaoshaUserService;
import com.yinggang.miaosha.service.OrderService;
import com.yinggang.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yinggang.miaosha.result.CodeMsg;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean{

	@Autowired
    MiaoshaUserService userService;
	
	@Autowired
    RedisService redisService;
	
	@Autowired
    GoodsService goodsService;
	
	@Autowired
    OrderService orderService;
	
	@Autowired
    MiaoshaService miaoshaService;

	@Autowired
	MQSender miaoshaSender;

	//商品是否秒杀完，直接存到map里，省去反复查询redis
	private HashMap<Long, Boolean> localOverMap =  new HashMap<Long, Boolean>();

	//系统初始化
	@Override
	public void afterPropertiesSet() throws Exception {
		//查询所有的秒杀数据，然后将库存写入到redis中
		List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
		if (goodsVoList == null) return;
		for (GoodsVo good : goodsVoList){
			//将good所有的数据写入redis
			redisService.set(GoodsKey.getGoodsDetail,""+good.getId(),good);
			//将good的库存写入redis中
			redisService.set(GoodsKey.getMiaoshaGoodsStock,""+good.getId(),good.getStockCount());
			//初始化localOverMap
			localOverMap.put(good.getId(),false);
		}

	}


	
	/**
	 * QPS:25
	 * 1000 * 10
     *
     * 情况1：在1000*10的并发的情况下 iphonex的库存为100000个，成功减少10000个，QPS:25
     * 情况2：在1000*10的并发的情况下 huawei的库存为20个，减少26个，发生严重超卖错误，QPS:26
     *
     *
	 * */

    @RequestMapping("/do_miaosha")
    public String list(Model model,MiaoshaUser user, @RequestParam("goodsId")long goodsId) {

        model.addAttribute("user", user);
    	if(user == null) {
    		return "login";
    	}


    	//从redis中预减库存，判断用户是否可以秒杀货品，可以的话就生成订单，不可以的话就返回
		//内存标记
//		boolean over = localOverMap.get(goodsId);
//    	if (over){
//    		model.addAttribute("errmsg",CodeMsg.MIAO_SHA_OVER);
//    		return "miaosha_fail";
//		}

		//预减库存

		long stock = redisService.decrStock(GoodsKey.getMiaoshaGoodsStock,""+goodsId);
		if (stock == -1){
			//该产品已经秒杀完了
			localOverMap.put(goodsId,true);
			model.addAttribute("errmsg",CodeMsg.MIAO_SHA_OVER);
			return "miaosha_fail";
		}

		//入队列
		MiaoshaMeesage miaoshaMessage = new MiaoshaMeesage();
		miaoshaMessage.setGoodId(goodsId);
		miaoshaMessage.setUser(user);
		miaoshaSender.sendMiaoshaMessage(miaoshaMessage);

		model.addAttribute("errmsg",CodeMsg.MIAOSHA_ING);
		return "miaosha_fail";



		//直接操作数据库
        //return miaoshaService.miaoshaWithLock(model,user,goodsId);




    }





	@RequestMapping("/reSet")

	public String reSet(Model model) throws Exception {
		//查询所有的秒杀数据，然后将库存写入到redis中
		List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
		if (goodsVoList == null) return "miaosha_fail";
		for (GoodsVo good : goodsVoList){
			//将good所有的数据写入redis
			redisService.set(GoodsKey.getGoodsDetail,""+good.getId(),good);
			//将good的库存写入redis中
			redisService.set(GoodsKey.getMiaoshaGoodsStock,""+good.getId(),good.getStockCount());
			//初始化localOverMap
			localOverMap.put(good.getId(),false);
		}
		model.addAttribute("errmsg",CodeMsg.RESET);
		return "miaosha_fail";

	}

}
