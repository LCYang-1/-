package com.lcy.miaosha.controller;

import com.lcy.miaosha.pojo.MiaoshaUser;
import com.lcy.miaosha.redis.GoodsKey;
import com.lcy.miaosha.redis.RedisService;
import com.lcy.miaosha.result.Result;
import com.lcy.miaosha.service.GoodsService;
import com.lcy.miaosha.service.MiaoshaUserService;
import com.lcy.miaosha.vo.GoodsDetailVo;
import com.lcy.miaosha.vo.GoodsVo;

import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@Api(tags = "商品页面")
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    //@ApiOperation(value = "商品列表",httpMethod = "GET")
    @RequestMapping(value="/to_list", produces="text/html")
    @ResponseBody
    public String list(Model model, MiaoshaUser user, HttpServletRequest request, HttpServletResponse response){
//        System.out.println(user);
        model.addAttribute("user",user);
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if(!StringUtils.isEmpty(html)) {
            System.out.println(html);
            return html;
        }
        //取数据库
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsList);
        //return "goods_list";

        //手动渲染
        //https://blog.csdn.net/ouzhuangzhuang/article/details/84839266
        IWebContext ctx =new WebContext(request,response, request.getServletContext(),request.getLocale(),model.asMap());

        //渲染模板
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    //@ApiOperation(value = "商品详情",httpMethod = "GET")
    @RequestMapping(value="/to_detail2/{goodsId}",produces="text/html;charset=UTF-8")
    @ResponseBody
    //@ApiImplicitParam(name = "goodsId", value = "商品ID", paramType = "path", required = true, dataType = "long")
    public String detai2(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user,
                         @PathVariable("goodsId")long goodsId){


        //showflake   主键最好不用自增，不用UUID
        model.addAttribute("user",user);


        //取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }

        //手动渲染
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        //System.out.println(goods.toString());
        model.addAttribute("goods",goods);
        
        //
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        //秒杀状态
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        //秒杀倒计时
        model.addAttribute("remainSeconds", remainSeconds);
        //return "goods_detail";

        //手动渲染
        IWebContext ctx =new WebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
        }
        return html;
    }

    @RequestMapping(value="/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user,
                                        @PathVariable("goodsId")long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(vo);
    }

//    @RequestMapping("/to_list")
//    public String list(HttpServletResponse response, Model model,
//                         @CookieValue(value = MiaoshaUserService.COOKI_NAME_TOKEN,required = false) String cookieToken,
//                         @RequestParam(value = MiaoshaUserService.COOKI_NAME_TOKEN,required = false) String paramToken){
//        if (StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken))
//            return "login";
//        String token=StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//        MiaoshaUser user=userService.getByToken(response,token);
//        model.addAttribute("user",user);
//        return "goods_list";
//    }


}
