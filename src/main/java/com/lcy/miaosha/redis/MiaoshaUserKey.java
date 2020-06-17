package com.lcy.miaosha.redis;

public class MiaoshaUserKey extends BasePrefix {

    public static final  int TOKEN_EXPIRE=3600*24*3;

    public static final  int MOBILE_EXPIRE=60;

    public MiaoshaUserKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    public static MiaoshaUserKey token=new MiaoshaUserKey(TOKEN_EXPIRE,"tk");
    public static MiaoshaUserKey mobile=new MiaoshaUserKey(MOBILE_EXPIRE,"mobile");
    public static MiaoshaUserKey getById = new MiaoshaUserKey(0, "id");

}

