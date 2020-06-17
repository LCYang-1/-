package com.lcy.miaosha.controller;

import com.lcy.miaosha.exception.GlobalException;
import com.lcy.miaosha.redis.RedisService;
import com.lcy.miaosha.result.CodeMsg;
import com.lcy.miaosha.result.Result;
import com.lcy.miaosha.service.MiaoshaUserService;
import com.lcy.miaosha.util.RandomValidateCodeUtil;
import com.lcy.miaosha.vo.LoginVo;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@Api(tags = "用户管理相关接口")
@RequestMapping("/login")
public class LoginController {


    private static Logger log= LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @ApiOperation(value = "访问登录页",httpMethod = "GET")
    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }




    public void yam(String verifyInput,HttpSession session){
        String inputStr = verifyInput;
        String random = (String) session.getAttribute("RANDOMVALIDATECODEKEY");
        if (random == null) {
            throw new GlobalException(CodeMsg.YANZHENGMA_EMPTY);
        }
        if (!random.equals(inputStr)) {
            throw new GlobalException(CodeMsg.YANZHENGMA_ERROR);
        }
    }
    public String doLogin2(HttpServletResponse response,LoginVo loginVo){
        log.info(loginVo.toString());
//        参数校验

        //登录
        String token=userService.login(response,loginVo);
        return token;

    }

    /**
     *     //@ApiOperation(value = "登录验证", httpMethod = "POST",notes = "登录验证，需要判断手机号，密码，图形验证码")
     *     @RequestMapping("/doo_login")//异步
     * //    @ApiImplicitParams({
     * //            @ApiImplicitParam(name="mobile",value="手机号",required=true,paramType="form",dataType="String",defaultValue = "13012400230"),
     * //            @ApiImplicitParam(name="password",value="密码",required=true,paramType="form",dataType="String"),
     * //            @ApiImplicitParam(name="verifyInput",value="验证码",required= false ,paramType="form",dataType="String"),
     * //            @ApiImplicitParam(name="ifyzm",value="是否验证码",required= false ,paramType="query",dataType="int")
     * //    })
     */



    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo,
                                   @RequestParam(defaultValue="1234") String verifyInput,
                                   @RequestParam(defaultValue="0") int ifyzm, HttpSession session) throws IOException {

        if (ifyzm==0)
            yam(verifyInput,session);


        String token = doLogin2(response, loginVo);
        if (ifyzm==1)
            response.sendRedirect("http://localhost:8080/goods/to_list");
        //System.out.println(".........");
        return Result.success(token);
    }

    /**
     * 生成验证码
     */
    @ApiIgnore
    @RequestMapping(value = "/getVerify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
        } catch (Exception e) {
            log.error("获取验证码失败>>>>   ", e);
        }
    }

    /**
     * 校验验证码
     */
    @ApiIgnore
    @RequestMapping(value = "/checkVerify", method = RequestMethod.POST,headers = "Accept=application/json")
    public boolean checkVerify(@RequestParam String verifyInput, HttpSession session) {
        try{
            //从session中获取随机数
            String inputStr = verifyInput;
            String random = (String) session.getAttribute("RANDOMVALIDATECODEKEY");
            if (random == null) {
                return false;
            }
            if (random.equals(inputStr)) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            log.error("验证码校验失败", e);
            return false;
        }
    }


}
