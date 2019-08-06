package com.yinggang.miaosha.dao;

import java.util.List;

import com.yinggang.miaosha.domain.MiaoshaGoods;
import com.yinggang.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface GoodsDao {
	
	@Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")
	public List<GoodsVo> listGoodsVo();
	//使用排他锁，控制超卖现象，在一个事务查询数据库的时候，获得排它锁，其他事务都要等待才行。接近武串行化。
	//@Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price " +
	//		"from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId} for update")

	@Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price " +
					"from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId} ")
	public GoodsVo getGoodsVoByGoodsId(@Param("goodsId")long goodsId);

	@Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0 ")
	//@Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} ")
	public int reduceStock(MiaoshaGoods g);
	
}
