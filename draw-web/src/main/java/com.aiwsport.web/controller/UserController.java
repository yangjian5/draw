package com.aiwsport.web.controller;

import com.aiwsport.core.DrawServerException;
import com.aiwsport.core.DrawServerExceptionFactor;
import com.aiwsport.core.constant.DrawConstant;
import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.entity.User;
import com.aiwsport.core.service.UserService;
import com.aiwsport.web.utlis.ParseUrl;
import com.aiwsport.web.verify.ParamVerify;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
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
    private static Logger logger = LogManager.getLogger();

    @RequestMapping(value = "/login.json")
    public ResultMsg login(@ParamVerify(isNotBlank = true)String code, @ParamVerify(isNotNull = true)String avatar_url,
                           @ParamVerify(isNotNull = true)String nick_name, @ParamVerify(isNotNull = true)String gender) {

        String url1 = "https://api.weixin.qq.com/sns/jscode2session?appid="+ DrawConstant.APP_ID +"&secret="+DrawConstant.SECRET+"&js_code=";
        String url2 = "&grant_type=authorization_code";
        JSONObject userObj = null;
        try {
            String userInfo = ParseUrl.getDataFromUrl((url1+code+url2));
            if (StringUtils.isBlank(userInfo)) {
                throw new DrawServerException(DrawServerExceptionFactor.PUSH_CONN_INTERRUPT, "jscode2session is fail");
            }
            userObj = JSONObject.parseObject(userInfo);
            if (userObj.containsKey("errcode")) {
                throw new DrawServerException(DrawServerExceptionFactor.PUSH_CONN_INTERRUPT, userObj.getString("errmsg"));
            }
            User user = userService.login(userObj.getString("openid"), nick_name, avatar_url, gender);
            userObj.put("income", user.getIncome());
            userObj.put("userid", user.getId());
        } catch (Exception e) {
            logger.error("login is error", e);
            throw new DrawServerException(DrawServerExceptionFactor.PUSH_CONN_INTERRUPT, "login is fail");
        }
        return new ResultMsg("login", userObj);
    }

    @RequestMapping(value = "/get_user.json")
    public ResultMsg login(@ParamVerify(isNotBlank = true)String open_id) {
        User user = userService.getUser(open_id);
        return new ResultMsg("get_user", user);
    }
}
