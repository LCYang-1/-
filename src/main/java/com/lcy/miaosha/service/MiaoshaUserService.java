package com.lcy.miaosha.service;

import com.lcy.miaosha.controller.LoginController;
import com.lcy.miaosha.dao.MiaoshaUserDao;
import com.lcy.miaosha.entity.QQUserInfo;
import com.lcy.miaosha.exception.GlobalException;
import com.lcy.miaosha.pojo.MiaoshaUser;
import com.lcy.miaosha.redis.MiaoshaUserKey;
import com.lcy.miaosha.redis.RedisService;
import com.lcy.miaosha.result.CodeMsg;
import com.lcy.miaosha.util.MD5Util;
import com.lcy.miaosha.util.UUIDUtil;
import com.lcy.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {
    private static Logger log= LoggerFactory.getLogger(LoginController.class);

    public static final String COOKI_NAME_TOKEN="token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    public MiaoshaUser getById(long id){
        //取缓存
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getById, ""+id, MiaoshaUser.class);
        if(user != null) {
            return user;
        }
        //取数据库
        user = miaoshaUserDao.getById(id);
        //加缓存
        if(user != null) {
            redisService.set(MiaoshaUserKey.getById, ""+id, user);
        }
        return user;
    }

    // http://blog.csdn.net/tTU1EvLDeLFq5btqiK/article/details/78693323
    public boolean updatePassword(String token, long id, String formPass) {
        //取user
        MiaoshaUser user = getById(id);
        if(user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);
        //处理缓存
        redisService.delete(MiaoshaUserKey.getById, ""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token, token, user);
        return true;
    }

    public String login( HttpServletResponse response,LoginVo loginVo){
        if (loginVo==null)
            throw new GlobalException(CodeMsg.SERVER_ERROR) ;
        String mobile = loginVo.getMobile();
        String formpassword = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if (user==null)
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formpassword, saltDB);
        if(!calcPass.equals(dbPass))
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);

        //生成cookie
        String token= UUIDUtil.uuid();
        addCookie(response,token,user);
//        String token= UUIDUtil.uuid();
//        redisService.set(MiaoshaUserKey.token,token,user);
//        Cookie cookie=new Cookie(COOKI_NAME_TOKEN,token);
//        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
//        cookie.setPath("/");
//        response.addCookie(cookie);
        return token;
    }



    //生成cookie
    private void addCookie(HttpServletResponse response,String token,MiaoshaUser user){
        //System.out.println("生成cookie");

        redisService.set(MiaoshaUserKey.token,token,user);
        Cookie cookie=new Cookie(COOKI_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }





    public MiaoshaUser getByToken(HttpServletResponse response,String token) {
        if (StringUtils.isEmpty(token))
            return null;
        MiaoshaUser user= redisService.get(MiaoshaUserKey.token,token,MiaoshaUser.class);
        //延长有效期
        //生成cookie
        if (user!=null){
            addCookie(response,token,user);
        }
        return user;
    }

    public Long insertByQQ(MiaoshaUser user){
        return miaoshaUserDao.insertByQQ(user);
    }
    public Long selectOfQQById(String qqopenId){
        return miaoshaUserDao.selectOfQQById(qqopenId);
    }

    public Long ontSelectOfinsert(String qqopenId, QQUserInfo userInfo){
        Long a=miaoshaUserDao.selectOfQQById(qqopenId);
        if (a==null){
            MiaoshaUser user= new MiaoshaUser();
            user.setQqopenId(qqopenId);
            user.setNickname(userInfo.getNickname());
            user.setHead(userInfo.getFigureurl());
            return miaoshaUserDao.insertByQQ(user);
        }
        return a;
    }
}
