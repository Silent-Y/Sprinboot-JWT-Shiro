package com.springcloud.shiro.service;

import com.springcloud.shiro.dao.RoleDao;
import com.springcloud.shiro.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Role> retrieveRoleByUserId(Long userId) {
        return roleDao.retrieveRoleByUserId(userId);
    }
}
