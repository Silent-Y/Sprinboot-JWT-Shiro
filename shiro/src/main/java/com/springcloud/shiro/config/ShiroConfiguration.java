package com.springcloud.shiro.config;

import com.springcloud.shiro.authtentication.UserRealm;
import com.springcloud.shiro.filter.JwtFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    protected static final Logger logger = LoggerFactory.getLogger(ShiroConfiguration.class);

    /**
     * 攔截器配置
     * @param securityManager
     * @return
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        /**
         * 非攔截url
         */
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
//        filterChainDefinitionMap.put("/login/**","anon");
        filterChainDefinitionMap.put("/**","anon");

        /**
         * 攔截url
         */
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwtFilter",new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

//        filterChainDefinitionMap.put("/**","jwtFilter");
        filterChainDefinitionMap.put("/123","jwtFilter");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 禁用session，不保存用戶登錄狀態，保證每次請求都重新認證，但代碼中Subject.getSession()還是可以取值
     * @return
     */
    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator(){
        DefaultWebSessionStorageEvaluator defaultWebSessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        defaultWebSessionStorageEvaluator.setSessionStorageEnabled(false);
        return defaultWebSessionStorageEvaluator;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(UserRealm userRealm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(userRealm);

        /**
         * 關閉shiro自帶的session
         */
        DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        defaultSubjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(defaultSubjectDAO);
        return defaultWebSecurityManager;
    }

    /**
     * 創建自定義的UserRealm @Beam
     * @return
     */
    @Bean("shiroRealm")
    public UserRealm shiroRealm(){
        UserRealm shiroRealm = new UserRealm();
        return shiroRealm;
    }

    /**
     * 自動創建代理，不配置鑑權時可能會出錯
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 開啓aop註解支持，使用代理方式，所以開啓代碼支持
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }


}
