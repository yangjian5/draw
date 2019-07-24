package com.aiwsport.web.controller;

import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.service.UserService;
import com.aiwsport.web.verify.ParamVerify;
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
public class OrderController {

    @Autowired
    private UserService userService;
    private static Logger logger = LogManager.getLogger();

    @RequestMapping(value = "/buy.json")
    public ResultMsg login(@ParamVerify(isNumber = true)int draw_id, @ParamVerify(isNumber = true)int type) {






        return new ResultMsg("login", true);
    }

}
