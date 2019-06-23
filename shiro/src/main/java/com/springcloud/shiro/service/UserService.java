package com.springcloud.shiro.service;

import com.springcloud.shiro.entity.User;
import com.springcloud.shiro.model.UserModel;

import java.util.Map;

public interface UserService {

    User retrieveUserById(Long id);

//    Map retrieveUserMapByName(String name);

    User retrieveUserByName(String name);

    UserModel retrieveUserByNameAndPassword(String name, String password);
}
