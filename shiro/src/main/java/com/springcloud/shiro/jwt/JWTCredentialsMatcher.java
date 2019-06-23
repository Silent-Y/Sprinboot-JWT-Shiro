package com.springcloud.shiro.jwt;

import com.springcloud.shiro.model.UserModel;
import com.springcloud.shiro.util.JWTUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JWTCredentialsMatcher implements CredentialsMatcher {

    protected static final Logger logger = LoggerFactory.getLogger(JWTCredentialsMatcher.class);

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {

        String token = (String)((JwtToken)authenticationToken).getToken();
        UserModel user = (UserModel) authenticationInfo.getPrincipals().getPrimaryPrincipal();
        Boolean result = JWTUtil.verify(token,user);
        logger.info("==================>" + "the result of verify:" + result + "<==================");
        return result;
    }
}
