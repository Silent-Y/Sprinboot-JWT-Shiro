package com.springcloud.shiro.service;

import com.springcloud.shiro.entity.Role;

import java.util.List;

public interface RoleService {

    List<Role> retrieveRoleByUserId(Long userId);
}
