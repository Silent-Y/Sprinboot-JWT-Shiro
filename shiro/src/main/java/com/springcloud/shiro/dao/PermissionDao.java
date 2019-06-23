package com.springcloud.shiro.dao;

import com.springcloud.shiro.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionDao extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {

    @Query(value = "select permission.id,permission.name from role_permission left join permission on permission.id = role_permission.permission_id where role_permission.role_id = ?1",nativeQuery = true)
    List<Permission> retrievePermissionByRoleId(Long roleId);
}
