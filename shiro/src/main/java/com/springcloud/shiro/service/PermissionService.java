package com.springcloud.shiro.service;

import com.springcloud.shiro.entity.Permission;

import java.util.List;

public interface PermissionService {

    List<Permission> retrievePermissionByRoleId(Long roleId);
}
