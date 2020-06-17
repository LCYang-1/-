package com.lcy.miaosha.dao;

import com.lcy.miaosha.pojo.MiaoshaUser;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MiaoshaUserDao {

    @Select("select * from miaosha_user where id=#{id}")
    public MiaoshaUser getById(@Param("id") long id);

    @Insert("insert into miaosha_user(nickname,qq_openId,head) VALUES(#{user.nickname},#{user.qqopenId},#{user.head})")
    @Options(useGeneratedKeys=true,keyProperty = "id", keyColumn = "id")
    public Long insertByQQ(@Param("user")MiaoshaUser user);

    @Select("select id  from miaosha_user where qq_openId=#{qqopenId}")
    public Long selectOfQQById(String qqopenId);


    @Update("update miaosha_user set password = #{password} where id = #{id}")
    public void update(MiaoshaUser toBeUpdate);

}
