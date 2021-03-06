package com.aiwsport.web.utlis;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * httpClient工具类
 *
 * @author yangjian9
 */
public class ParseUrl {
    static HttpClient httpClient;

    static {
        HttpClientParams httpClientParams = new HttpClientParams();
        httpClientParams.setConnectionManagerTimeout(2000);
        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = connectionManager.getParams();
        params.setDefaultMaxConnectionsPerHost(100);
        params.setMaxTotalConnections(100);
        params.setConnectionTimeout(3000);
        params.setSoTimeout(2000);
        httpClient = new HttpClient(httpClientParams, connectionManager);
    }

    private static Logger logger = LogManager.getLogger();

    /**
     * 返回客户端ip
     *
     * @param request
     * @return
     */
    public static String getRemoteAddrIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    /**
     * 获取服务器的ip地址
     *
     * @param request
     * @return
     */
    public static String getLocalIp(HttpServletRequest request) {
        return request.getLocalAddr();
    }

    /**
     * 从指定的API获取JSON字符串数据
     *
     * @param url 待调用的API地址
     * @return 返回的JSON数据
     */
    public static String getDataFromUrl(String url) {
        HttpMethod httpMethod = new GetMethod(url);
        long beginTime = System.currentTimeMillis();
        int status = 0;
        try {
            status = httpClient.executeMethod(httpMethod);
            byte[] body = httpMethod.getResponseBody();
            if (body == null) {
                return "";
            }

            if (status == HttpStatus.SC_OK) {
                return new String(body);
            } else {
                logger.error(new StringBuilder(128).append("getDataFromUrl ").append(url).append("fail.status:").append(status)
                        .append(", return:").append(new String(body)));
            }
        } catch (ConnectTimeoutException e) {
            status = 1;
            logger.error(new StringBuilder(128).append("getDataFromUrl ").append(url).append(" occurs ConnectTimeoutException"), e);
        } catch (IOException e) {
            logger.error(new StringBuilder(128).append("getDataFromUrl ").append(url).append(" occurs IOException"), e);
        } finally {
            httpMethod.releaseConnection();
        }

        long endTime = System.currentTimeMillis();
        if ((endTime - beginTime) > 70) {
            logger.warn(new StringBuilder(128).append("get message from url ").append(url).append(" too slow,it costs ").append((endTime - beginTime)).append(" ms"));
        }
        return "";
    }

    public static String getHeaderValue(String url) {
        HttpMethod httpMethod = new GetMethod(url);
        long beginTime = System.currentTimeMillis();
        int status = 0;
        try {
            status = httpClient.executeMethod(httpMethod);

            Header responseHeader = httpMethod.getResponseHeader("Server-Config-Version");
            if (responseHeader == null) {
                return "";
            }

            String value = responseHeader.getValue();
            if (value == null) {
                return "";
            }

            if (status == HttpStatus.SC_OK) {
                return value;
            } else {
                logger.error(new StringBuilder(128).append("getDataFromUrl ").append(url).append("fail.status:").append(status)
                        .append(", return:").append(value));
            }
        } catch (ConnectTimeoutException e) {
            status = 1;
            logger.error(new StringBuilder(128).append("getDataFromUrl ").append(url).append(" occurs ConnectTimeoutException"), e);
        } catch (IOException e) {
            logger.error(new StringBuilder(128).append("getDataFromUrl ").append(url).append(" occurs IOException"), e);
        } finally {
            httpMethod.releaseConnection();
        }

        long endTime = System.currentTimeMillis();
        if ((endTime - beginTime) > 250) {
            logger.warn(new StringBuilder(128).append("get message from url ").append(url).append(" too slow,it costs ").append((endTime - beginTime)).append(" ms"));
        }
        return "";
    }


}