package com.springcloud.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.springcloud.shiro.jwt.JwtToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Service;
import java.io.PrintWriter;
import java.util.Date;

/**
 * 拦截器
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {

    protected static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    /**
     * http方法列表配置基本身份篩選器，獲取request請求，拒絕攔截登錄請求，執行登錄認證方法
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        String requestURI = httpServletRequest.getRequestURI();

        if (requestURI.equals("/user/login/verify") || requestURI.equals("/user/register")){
            return true;
        }else {
            try {
                executeLogin(request,response);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Authorization携带的参数为token
     * JwtToken实现AuthoricationToken接口封装token参数
     * getSubject方法获取subject对象，login发送身份验证
     *默认shiro是基于session验证，在基于token的验证中，login在filter中调用，不能在controller中调用
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest servletRequest,ServletResponse servletResponse) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String token = httpServletRequest.getHeader("Authorization");
            JwtToken jwtToken = new JwtToken(token);

            /**
             * 提交realm进行登录
             */
            Subject subject = getSubject(servletRequest,servletResponse);
            subject.login(jwtToken);
            logger.info("==================>" + "JWT authorization success" + "<==================");
            return true;
        }catch (Exception e){

            /**
             * 原生shiro验证失败会进入全局异常，与JWT接合在过滤器里验证，成功与否都返回过滤器中，成功则进入controller，失败进入springboot自定义异常界面
             */
            JSONObject  responseJSONObject = new JSONObject();
            responseJSONObject.put("result","401");
            responseJSONObject.put("resultCode","Toke is invalid,please get token again!");
            responseJSONObject.put("resultDate",null);
            responseJSONObject.put("resultTime",new Date().toString());
            PrintWriter printWriter = null;
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            logger.info("==================>" + "The result data:" +responseJSONObject.toString() + "<==================");
            printWriter = httpServletResponse.getWriter();
            printWriter.append(responseJSONObject.toString());
        }
        return false;
    }

    /**
     * 对跨域提供支持
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;
        httpServletResponse.setHeader("Access-control-Allow-Origin",httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-control-Allow-Methods","GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-control-Allow-Headers",httpServletRequest.getHeader("Access-Control-Request-Headers"));

        /**
         * 跨域时首先发送一个option请求，此处option直接返回正常状态
         */
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())){
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(servletRequest, servletResponse);
    }
}
