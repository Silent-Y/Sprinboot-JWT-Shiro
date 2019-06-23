package com.springcloud.shiro.service;

import com.springcloud.shiro.dao.PermissionDao;
import com.springcloud.shiro.entity.Permission;
import com.springcloud.shiro.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public List<Permission> retrievePermissionByRoleId(Long roleId) {
        return permissionDao.retrievePermissionByRoleId(roleId);
    }
}
