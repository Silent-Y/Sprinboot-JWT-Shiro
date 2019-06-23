package com.springcloud.shiro.authtentication;

import com.springcloud.shiro.application.ApplicationYml;
import com.springcloud.shiro.entity.Permission;
import com.springcloud.shiro.entity.Role;
import com.springcloud.shiro.entity.User;
import com.springcloud.shiro.jwt.JWTCredentialsMatcher;
import com.springcloud.shiro.jwt.JwtToken;
import com.springcloud.shiro.model.UserModel;
import com.springcloud.shiro.service.PermissionService;
import com.springcloud.shiro.service.RoleService;
import com.springcloud.shiro.service.UserService;
import com.springcloud.shiro.util.JWTUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Set;

public class UserRealm extends AuthorizingRealm {

    protected static final Logger logger = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private ApplicationYml applicationYml;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 使用自定义的Matcher接口
     */
    public UserRealm(){
        this.setCredentialsMatcher(new JWTCredentialsMatcher());
    }

    /**
     * 重写supports方法,否则shiro报错
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 认证登录
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        /**
         * 获取用户传递的token
         */
        String JwtToken =  ((JwtToken)token).getToken();
        /**
         * Claims对象是一个json格式的对象，可以保存任何值
         * token解密，转换成Claims对象
         */
        Claims claims = JWTUtil.parseJWT(JwtToken, applicationYml.getSecretKey());

        String name = claims.getSubject();
        String password = claims.get("password").toString();
        UserModel principal = userService.retrieveUserByNameAndPassword(name,password);
        return new SimpleAuthenticationInfo(principal, JwtToken,(String) this.getClass().getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SimpleAuthorizationInfo simpleAuthorizationInfo = null;

        /**
         * 返回应用程序内的主要对象，以唯一标识拥有帐户
         */
        Object principal = principals.getPrimaryPrincipal();

        /**
         * 获取身份对象，查询该用户的权限信息
         */
        UserModel user = (UserModel) principal;
        List<Role> roles = roleService.retrieveRoleByUserId(user.getId());
        List<List<Permission>> permissionss = null;
        Set<String> permissionSet = null;
        try {
            if (!roles.isEmpty()){
                for (Role role:roles){
                    List<Permission> permissions = permissionService.retrievePermissionByRoleId(role.getId());
                    permissionss.add(permissions);
                    permissionSet.add(role.getName());
                    if (!permissions.isEmpty()){
                        for (Permission permission:permissions){
                            permissionSet.add(permission.getName());
                        }
                    }
                }
            }
            simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            simpleAuthorizationInfo.setStringPermissions(permissionSet);
        }catch (Exception e){
            throw new AuthenticationException("authorization failed!");
        }
        return simpleAuthorizationInfo;
    }
}
