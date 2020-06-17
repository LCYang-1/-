package com.lcy.miaosha.service;

import com.lcy.miaosha.pojo.MiaoshaUser;
import com.lcy.miaosha.redis.MiaoshaUserKey;
import com.lcy.miaosha.redis.RedisService;
import com.lcy.miaosha.send.SendSmsUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendSmsService {

    @Autowired
    SendSmsUtil sendSmsUtil;

    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService miaoshaUserService;

    public String authcode_get(String u_phone) {
        String authcode = "1"+ RandomStringUtils.randomNumeric(5);//生成随机数,我发现生成5位随机数时，如果开头为0，发送的短信只有4位，这里开头加个1，保证短信的正确性
        redisService.set(MiaoshaUserKey.mobile,u_phone,authcode);//将验证码存入缓存
        sendSmsUtil.SendSms(u_phone,authcode);//发送短息
        return authcode;
    }

    public String authcode_select(String u_phone,String u_phone1) {
        String s = redisService.get(MiaoshaUserKey.mobile, u_phone, String.class);
        if(!s.equals(u_phone1)) {
            return "验证码错误";
        }
        return "登录成功";
        //
        //成功后注册+登录+跳转到商品页面
        //
        //
    }

}
