package com.lcy.miaosha.controller;

import com.lcy.miaosha.service.SendSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SendSmsController {

    @Autowired
    SendSmsService sendSmsService;
    @RequestMapping("/login/SendSms")
    @ResponseBody
    public String SendSms_login(String u_phone) {
        return "发送成功"+sendSmsService.authcode_get(u_phone);
    }

    @RequestMapping("/login/yzmSms")
    @ResponseBody
    public String yzmSms_login(String u_phone, String code) {
        return sendSmsService.authcode_select(u_phone,code);

    }

}
