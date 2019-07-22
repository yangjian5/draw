package com.aiwsport.web.utlis;

import com.aiwsport.core.constant.DrawConstant;
import com.aiwsport.web.model.TokenInfo;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class TokenUtil {
    private static Logger logger = LogManager.getLogger();
    private static Map<String, TokenInfo> tokenInfoMap = new HashMap<>();

    public static String getToken() {
        TokenInfo tokenInfo = tokenInfoMap.get("tokenInfo");
        if (tokenInfo == null) {
            logger.error("tokenInfo map is null !! ");
            return "";
        }

        if (tokenInfo.getTime() > System.currentTimeMillis()) {
            TokenInfo newTokenInfo = reFreshToken();
            if (newTokenInfo != null) {
                tokenInfoMap.put("tokenInfo", newTokenInfo);
            }
        }

        return tokenInfo.getToken();
    }

    public static TokenInfo reFreshToken() {
        String res = ParseUrl.getDataFromUrl("https://api.weixin.qq.com/cgi-bin/token?" +
                "grant_type=client_credential&appid="+ DrawConstant.APP_ID +"&secret="+DrawConstant.SECRET);

        if (StringUtils.isBlank(res)) {
            logger.error("get token is error ");
            return null;
        }

        JSONObject jsonObject = JSONObject.parseObject(res);

        if (jsonObject.containsKey("errcode")) {
            logger.error("cgi-bin token is error " + res);
            return null;
        }

        long expireTime = jsonObject.getLongValue("expires_in");
        TokenInfo tokenInfo = new TokenInfo(jsonObject.getString("access_token"), System.currentTimeMillis() + expireTime/2*1000);
        return tokenInfo;
    }





}
