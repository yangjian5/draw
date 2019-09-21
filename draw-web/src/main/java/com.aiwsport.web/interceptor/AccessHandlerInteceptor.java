package com.aiwsport.web.interceptor;

import com.aiwsport.core.DrawServerException;
import com.aiwsport.core.DrawServerExceptionFactor;

import com.aiwsport.core.entity.Admin;
import com.aiwsport.core.service.BackService;
import com.aiwsport.core.utils.AesUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class AccessHandlerInteceptor implements HandlerInterceptor {

    private static Logger logger = LogManager.getLogger();

    @Autowired
    private BackService backService;

    /**
     * controller 执行之前调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("/api/backend/backend_login.json".equals(request.getRequestURI())) {
            return true;
        }

        if (backService == null) {//解决service为null无法注入问题
            BeanFactory factory = WebApplicationContextUtils
                    .getRequiredWebApplicationContext(request.getServletContext());
            backService = (BackService) factory
                    .getBean("backService");
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0){
            throw new DrawServerException(DrawServerExceptionFactor.BACKEND_LOGIN_ERROR, "非法登录");
        }
        for (Cookie cookie:cookies){
            if (cookie.getName().equals("sub")){
                String subStr = cookie.getValue();
                try {
                    JSONObject jsonObject = (JSONObject) JSONObject.parse(AesUtil.decrypt(subStr));
                    String account = jsonObject.getString("taccount");
                    String password = jsonObject.getString("password");
                    long expireTime = jsonObject.getLong("expire_time");
                    if (expireTime < System.currentTimeMillis()) {
                        throw new DrawServerException(DrawServerExceptionFactor.BACKEND_LOGIN_ERROR, "登录过期");
                    }
                    Admin admin = backService.getAdmin(account, password);
                    if (admin == null) {
                        throw new DrawServerException(DrawServerExceptionFactor.BACKEND_LOGIN_ERROR, "非法登录");
                    }
                    return true;
                } catch (Exception e) {
                    logger.warn("cookie is not vail sub is " + subStr);
                    throw new DrawServerException(DrawServerExceptionFactor.BACKEND_LOGIN_ERROR, "非法登录");
                }
            }
        }
//        authSign(request, "y21gsdi35zas0921ksjxu3la5noiwns5ak821#2*ds+");
        throw new DrawServerException(DrawServerExceptionFactor.BACKEND_LOGIN_ERROR, "登录失败");
    }

    /**
     * controller 执行之后，且页面渲染之前调用
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    /**
     * 页面渲染之后调用，一般用于资源清理操作
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {


    }

    //进行签名限制
    //签名规则 md5(base64(params) + appsecret)
    protected void authSign(HttpServletRequest request, String secret) {
        String paramString = getQueryString(request);
        String param_sign = request.getParameter("sign");
//        commonAuthSign(paramString,param_sign,secret,request.getRequestURI());
    }

    protected void commonAuthSign(String paramString,String param_sign,String secret, String uri){
        String sign = DigestUtils.md5Hex(URLEncoder.encode(paramString.replaceAll(" ", "")) + secret);
        if (!sign.equals(param_sign)) {
            logger.warn("paramString: " + paramString +
                    ",param sign: " + param_sign + ",sign: " + sign + ", uri: " + uri);
            throw new DrawServerException(DrawServerExceptionFactor.SIGN_IS_ERROR);
        }
    }

    public static String getQueryString(HttpServletRequest request) {
        SortedMap<String, String[]> params = getSortedParams(request);
        return getQueryStringByMap(params);
    }

    private static SortedMap<String, String[]> getSortedParams(HttpServletRequest request) {
        SortedMap<String, String[]> map = new TreeMap<String, String[]>();
        Map<String, String[]> paramMap = request.getParameterMap();
        if (paramMap == null) {
            return map;
        }
        for (Object e : paramMap.entrySet()) {
            Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) e;
            String name = entry.getKey();
            map.put(name, entry.getValue());
        }
        return map;
    }

    public static String getQueryStringByMap(SortedMap sortedMap) {
        boolean first = true;
        StringBuilder strbuf = new StringBuilder();
        for (Object e : sortedMap.entrySet()) {
            Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) e;
            String name = entry.getKey();
            if ("sign".equals(name)) {
                continue;
            }
            String[] sValues = entry.getValue();
            String sValue = "";
            for (int i = 0; i < sValues.length; i++) {
                sValue = sValues[i];
                if (first == true) {
                    //第一个参数
                    first = false;
                    strbuf.append(name).append("=").append(sValue);
                } else if (first == false) {
                    strbuf.append("&").append(name).append("=").append(sValue);
                }
            }
        }
        return strbuf.toString();
    }

}
