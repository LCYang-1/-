package com.lcy.miaosha.service;

import com.lcy.miaosha.pojo.MiaoshaUser;
import com.lcy.miaosha.pojo.OrderInfo;
import com.lcy.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    //事务
    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减库存 下订单 写入秒杀订单
        //减库存
        System.out.println("减库存");
        goodsService.reduceStock(goods);
        //order_info maiosha_order
        //下订单 写入秒杀订单
        System.out.println("添加订单");
        return orderService.createOrder(user, goods);
    }
}
