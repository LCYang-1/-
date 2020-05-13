package com.lcy.miaosha.service;

import com.lcy.miaosha.dao.UserDao;
import com.lcy.miaosha.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getById(int id){
        return userDao.getById(id);
    }
}
