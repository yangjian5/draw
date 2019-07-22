package com.aiwsport.web.controller;

import com.aiwsport.core.service.UserService;
import com.aiwsport.web.utlis.ParseUrl;
import com.aiwsport.web.verify.ParamVerify;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户操作
 *
 * @author yangjian
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    private static final String APP_ID = "wx169ddfe67114165d";

    private static final String SECRET = "e26e1b29d8fc04e461d3277c919100aa";

    private static Logger logger = LogManager.getLogger();

    @RequestMapping(value = "/login.json")
    public JSONObject onlogin(@ParamVerify(isNotBlank = true)String code) {
        String url1 = "https://api.weixin.qq.com/sns/jscode2session?appid="+APP_ID+"&secret="+SECRET+"&js_code=";
        String url2 = "&grant_type=authorization_code";
        String userInfo = "";
        JSONObject jsonObject = null;
        try {
            long start = System.currentTimeMillis();
            userInfo = ParseUrl.getDataFromUrl((url1+code+url2));
            System.out.println("------userInfo------" + userInfo);
            long end = System.currentTimeMillis();
            System.out.println("------cost------" + (end-start));
            jsonObject = JSONObject.parseObject(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


}
