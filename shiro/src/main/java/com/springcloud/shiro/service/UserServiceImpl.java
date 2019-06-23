package com.springcloud.shiro.service;

import com.springcloud.shiro.dao.UserDao;
import com.springcloud.shiro.entity.User;
import com.springcloud.shiro.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User retrieveUserById(Long id) {
        return userDao.retrieveUserById(id);
    }

//    @Override
//    public Map retrieveUserMapByName(String name) {
//        return userDao.retrieveUserMapByName(name);
//    }

    @Override
    public User retrieveUserByName(String name) {
        return userDao.retrieveUserByName(name);
    }

    @Override
    public UserModel retrieveUserByNameAndPassword(String name, String password) {
        return userDao.retrieveUserByNameAndPassword(name,password);
    }
}
