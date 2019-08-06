package com.yinggang.miaosha.redis;

public class GoodsKey extends BasePrefix{

	private GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static GoodsKey getGoodsList = new GoodsKey(0, "gl");
	public static GoodsKey getGoodsDetail = new GoodsKey(0, "GoodsDetail");
	public static GoodsKey getMiaoshaGoodsStock= new GoodsKey(0, "GoodsStock");
}
