package com.lcy.miaosha.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    /**
     * 获取单个对象
     * @param prefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix prefix,String key,Class<T> clazz){
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey =prefix.getPrefix()+key;
            String str = jedis.get(realKey);
            T t=stringToBean(str,clazz);
            return t;

        }finally {
            returnToPool(jedis);
        }

    }

    /**
     * 设置对象
     * @param prefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix prefix,String key,T value){
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();
            String str=beanToString(value);
            if (str==null||str.length()<=0)
                return false;
            //生成真正的key
            String realKey =prefix.getPrefix()+key;
            int seconds=prefix.expireSeconds();
            if (seconds<=0){
                jedis.set(realKey,str);
            } else {
                jedis.setex(realKey,seconds,str);
            }
            jedis.set(realKey, str);
            //System.out.println("把<"+realKey+","+str+">写进redis");
            return true;

        }finally {
            returnToPool(jedis);
        }

    }

    /**
     * 判断是否存在
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> boolean exists(KeyPrefix prefix,String key){
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey =prefix.getPrefix()+key;
            return jedis.exists(realKey);

        }finally {
            returnToPool(jedis);
        }

    }


    /**
     * 删除
     * */
    public boolean delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            long ret =  jedis.del(key);
            return ret > 0;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 增加值
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix prefix,String key){
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey =prefix.getPrefix()+key;
            return jedis.incr(realKey);

        }finally {
            returnToPool(jedis);
        }

    }

    /**
     * 减少值
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix prefix,String key){
        Jedis jedis =null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey =prefix.getPrefix()+key;
            return jedis.decr(realKey);

        }finally {
            returnToPool(jedis);
        }

    }



    private <T> String beanToString(T value) {
        if (value==null)
            return null;
        Class<?> aClass = value.getClass();
        if (aClass==int.class||aClass==Integer.class){
            return ""+value;
        }else if (aClass==String.class){
            return (String) value;
        }else if (aClass==long.class||aClass==Long.class){
            return ""+value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    private <T> T stringToBean(String str,Class<T> aClass) {
        if (str==null||str.length()<=0||aClass==null)
            return null;


        if (aClass==int.class||aClass==Integer.class){
            return (T)Integer.valueOf(str);
        }else if (aClass==String.class){
            return (T)str;
        }else if (aClass==long.class||aClass==Long.class){
            return (T)Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str),aClass);
        }
    }

    private void returnToPool(Jedis jedis) {
        if (jedis!=null){
            jedis.close();
        }
    }



}
