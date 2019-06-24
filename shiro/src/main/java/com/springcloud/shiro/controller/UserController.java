package com.springcloud.shiro.controller;

import com.springcloud.shiro.application.ApplicationYml;
import com.springcloud.shiro.entity.User;
import com.springcloud.shiro.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api("用户服务")
@RestController
@RequestMapping("/user")
public class UserController {

    protected static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ApplicationYml applicationYml;

    @Autowired
    private UserService userService;

    @ApiOperation("根据id获取用户")
    @GetMapping("/retrieve/user/{id}")
    public User retrieveUserById(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                 @PathVariable(required = true,name = "id") Long id){

        logger.info("==================>" + "secretKey:" + applicationYml.getSecretKey() + "<==================");
        logger.info("==================>" + httpServletRequest.getRequestURI() + "<==================");

        return userService.retrieveUserById(id);
    }
}
