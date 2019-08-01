package com.aiwsport.core.utils;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class PayUtil {

    /**
     * 生成订单号
     *
     * @return
     */
    public static String getTradeNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            result += random.nextInt(10);
        }
        return newDate + System.currentTimeMillis() + result;
    }

    public static String getSign(Map<String, String> params, String paternerKey) throws UnsupportedEncodingException {
        return DigestUtils.md5Hex(createSign(params, true) + "&key=" + paternerKey).toUpperCase();
    }

    public static String sign(String str, String paternerKey) throws UnsupportedEncodingException {
        return DigestUtils.md5Hex(URLEncoder.encode(str, "UTF-8") + "&key=" + paternerKey).toUpperCase();
    }

    /**
     * 构造签名
     *
     * @param params
     * @param encode
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String createSign(Map<String, String> params, boolean encode) throws UnsupportedEncodingException {
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuffer temp = new StringBuffer();
        boolean first = true;
        for (Object key : keys) {
            if (key == null || StringUtils.isBlank(params.get(key))) // 参数为空不参与签名
                continue;
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueStr = "";
            if (null != value) {
                valueStr = value.toString();
            }
            if (encode) {
                temp.append(URLEncoder.encode(valueStr, "UTF-8"));
            } else {
                temp.append(valueStr);
            }
        }
        return temp.toString();
    }

    /**
     * 创建支付随机字符串
     * @return
     */
    public static String getNonceStr(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


}

