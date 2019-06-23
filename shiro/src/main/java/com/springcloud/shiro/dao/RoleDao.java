package com.springcloud.shiro.dao;

import com.springcloud.shiro.entity.Role;
import com.springcloud.shiro.entity.User;
import com.springcloud.shiro.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RoleDao extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    @Query(value = "select role.id,role.name from user_role left join role on role.id = user_role.role_id where user_role.user_id = ?1",nativeQuery = true)
    List<Role> retrieveRoleByUserId(Long userId);
}
