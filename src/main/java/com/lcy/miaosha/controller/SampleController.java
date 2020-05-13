package com.lcy.miaosha.controller;

import com.lcy.miaosha.pojo.User;
import com.lcy.miaosha.redis.RedisService;
import com.lcy.miaosha.redis.UserKey;
import com.lcy.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.transform.Result;

@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    RedisService redisService;

    @Autowired
    UserService userService;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","Joshua");
        return "hello";
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public String dbGet(){
        User user=userService.getById(1);
        return user.toString();
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public String redisGet(){
        User v1=redisService.get(UserKey.getById,""+1,User.class);
        return v1.toString();
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public boolean redisSet(){
        User user = new User(1, "1111");
        boolean b = redisService.set(UserKey.getById, "" + 1, user);

        return b;
    }
}
