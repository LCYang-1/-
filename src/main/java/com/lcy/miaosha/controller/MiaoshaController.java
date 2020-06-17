package com.lcy.miaosha.controller;

import com.lcy.miaosha.pojo.MiaoshaOrder;
import com.lcy.miaosha.pojo.MiaoshaUser;
import com.lcy.miaosha.pojo.OrderInfo;
import com.lcy.miaosha.redis.RedisService;
import com.lcy.miaosha.result.CodeMsg;
import com.lcy.miaosha.result.Result;
import com.lcy.miaosha.service.GoodsService;
import com.lcy.miaosha.service.MiaoshaService;
import com.lcy.miaosha.service.MiaoshaUserService;
import com.lcy.miaosha.service.OrderService;
import com.lcy.miaosha.vo.GoodsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


//@Api(tags = "秒杀商品")

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {


    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

//    @ApiOperation(value = "商品详情",httpMethod = "GET")
//    @RequestMapping("/do_miaosha")
//    @ApiImplicitParam(name = "goodsId", value = "商品ID", paramType = "query",required = true, dataType = "long")




    @RequestMapping(value = "/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> hellowrold(Model model, MiaoshaUser user,
                             @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            System.out.println(CodeMsg.SESSION_ERROR);
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0) {
            System.out.println(CodeMsg.MIAO_SHA_OVER);
            return Result.error(CodeMsg.MIAO_SHA_OVER);

        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            System.out.println(CodeMsg.REPEATE_MIAOSHA);
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        return Result.success(orderInfo);

    }


//    @RequestMapping(value = "/do_miaosha")
//    public String hellowrold(Model model, MiaoshaUser user,
//                             @RequestParam("goodsId")long goodsId) {
//        model.addAttribute("user", user);
//        if(user == null) {
//            return "login";
//        }
//        //判断库存
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        int stock = goods.getStockCount();
//        if(stock <= 0) {
//            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
//            return "miaosha_fail";
//        }
//        //判断是否已经秒杀到了
//        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
//        if(order != null) {
//            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
//            return "miaosha_fail";
//        }
//        //减库存 下订单 写入秒杀订单
//        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
//        model.addAttribute("orderInfo", orderInfo);
//        model.addAttribute("goods", goods);
//        return "order_detail";
//    }
}
