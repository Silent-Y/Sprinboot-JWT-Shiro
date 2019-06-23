package com.springcloud.shiro.dao;

import com.springcloud.shiro.entity.User;
import com.springcloud.shiro.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface UserDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query(value = "select user.id,user.name,user.password from user where user.id = ?1",nativeQuery = true)
    User retrieveUserById(Long id);

//    @Query(value = "select user.id as userId,user.name as userName,role.id as roleId,role.name as roleName," +
//            " permission.id as permissionId,permission.name as permissionName" +
//            " from user inner join user_role on user_role.user_id = user.id" +
//            " inner join role on role.id = user_role.role_id" +
//            " inner join role_permission on role_permission.role_id = role.id" +
//            " inner join permission on role_permission.permission_id = permission.id" +
//            " where user.name = ?1")
//    Map retrieveUserMapByName(String name);

    @Query(value = "select user.id,user.name,user.password from user where user.name = ?1",nativeQuery = true)
    User retrieveUserByName(String name);

    @Query(value = "select user.id,user.name,user.password from user where user.name = ?1 and user.passowrd = ?2",nativeQuery = true)
    UserModel retrieveUserByNameAndPassword(String name, String password);
}
