package com.lcy.miaosha.controller;

import com.lcy.miaosha.pojo.MiaoshaUser;
import com.lcy.miaosha.pojo.User;
import com.lcy.miaosha.result.Result;
import com.lcy.miaosha.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class SwaggerDemoController {
    @Autowired
    private UserService userService;

    private static final Logger logger= LoggerFactory.getLogger(SwaggerDemoController.class);


//    @ApiOperation(value = "根据id查询用户信息", notes = "查询数据库中某个的用户信息")
//    @ApiImplicitParam(name = "id", value = "用户ID", paramType = "path", required = true, dataType = "Integer")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getStudent(@PathVariable int id) {
        logger.info("开始查询某个学生信息");
        return userService.getById(id);
    }

    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(Model model,MiaoshaUser user){
        return Result.success(user);
    }
}
