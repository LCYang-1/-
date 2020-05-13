package com.lcy.miaosha.dao;

import com.lcy.miaosha.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Mapper
public interface UserDao {

    @Select("select * from user where id=#{id}")
    public User getById(@Param("id") int id);


}
