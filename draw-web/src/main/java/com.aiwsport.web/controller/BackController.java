package com.aiwsport.web.controller;

import com.aiwsport.core.DrawServerException;
import com.aiwsport.core.DrawServerExceptionFactor;
import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.entity.Admin;
import com.aiwsport.core.entity.DrawBranner;
import com.aiwsport.core.service.BackService;
import com.aiwsport.core.utils.AesUtil;
import com.aiwsport.web.verify.ParamVerify;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 首页展示
 *
 * @author yangjian
 */
@RestController
@RequestMapping(value = "/api/backend")
public class BackController {

    @Autowired
    private BackService backService;


    @RequestMapping(value = "/banner/update.json")
    public ResultMsg bannerUpdate(DrawBranner branner) {
        backService.bannerUpdate(branner);
        return new ResultMsg("update", true);
    }


    @RequestMapping(value = "/banner/insert.json")
    public ResultMsg bannerInsert(DrawBranner branner) {
        return new ResultMsg("insert", backService.bannerInsert(branner));
    }

    @RequestMapping(value = "/banner/delete.json")
    public ResultMsg bannerDelete(@ParamVerify(isNumber = true) int id) {
        return new ResultMsg("insert", backService.bannerDelete(id));
    }

    @RequestMapping(value = "/user/select.json")
    public ResultMsg userSelect(@RequestParam(name = "nickName", required = false, defaultValue = "") String nickName,
                                @ParamVerify(isNumber = true) int page,
                                @ParamVerify(isNumber = true) int count) {
        if (page <= 0 || count < 0) {
            throw new DrawServerException(DrawServerExceptionFactor.PARAM_COUNT_FAIL, "param is error");
        }
        return new ResultMsg("userSelect", backService.showUsers(nickName, page, count));
    }


    @RequestMapping(value = "/draw/select.json")
    public ResultMsg drawSelect(@RequestParam(name = "drawName", required = false, defaultValue = "") String drawName,
                                @ParamVerify(isNumber = true) int page,
                                @ParamVerify(isNumber = true) int count) {
        if (page <= 0 || count < 0) {
            throw new DrawServerException(DrawServerExceptionFactor.PARAM_COUNT_FAIL, "param is error");
        }

        return new ResultMsg("drawSelect", backService.showDraws(drawName, page, count));
    }

    @RequestMapping(value = "/income/select.json")
    public ResultMsg incomeSelect(@ParamVerify(isNumber = true) int page,
                                  @ParamVerify(isNumber = true) int count,
                                  String status) {
        if (page <= 0 || count < 0) {
            throw new DrawServerException(DrawServerExceptionFactor.PARAM_COUNT_FAIL, "param is error");
        }
        return new ResultMsg("drawSelect", backService.showIncome(page, count, status));
    }

    @RequestMapping(value = "/income/check.json")
    public ResultMsg incomeCheck(@ParamVerify(isNumber = true) int id,
                               @ParamVerify(isNumber = true) String status) {
        try {
            if (!backService.incomeCheck(id, status)) {
                throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "check income is fail");
            }
        } catch (Exception e) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "check income is error");
        }

        return new ResultMsg("incomeCheck", true);
    }

    @RequestMapping(value = "/income/refund.json")
    public ResultMsg incomeCheck(@ParamVerify(isNumber = true) int id) {
        if (!backService.refund(id)) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "refund income is fail");
        }
        return new ResultMsg("incomeCheck", true);
    }

    @RequestMapping(value = "/draw/check.json")
    public ResultMsg drawCheck(@ParamVerify(isNumber = true) int id,
                               @ParamVerify(isNumber = true) int drawStatus) {
        if (!backService.drawCheck(id, drawStatus)) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "没有添加所有权");
        }

        return new ResultMsg("drawCheck", true);
    }

    @RequestMapping(value = "/backend_login.json")
    public ResultMsg drawLogin(HttpServletResponse response,
                               @ParamVerify(isNotBlank = true) String taccount,
                               @ParamVerify(isNotBlank = true) String password) {
        Admin admin = backService.getAdmin(taccount, password);
        if (admin == null) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "账号或密码有误");
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taccount", taccount);
        jsonObject.put("password", password);
        jsonObject.put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60 * 6);
        String subStr = AesUtil.encrypt(jsonObject.toJSONString());

        Cookie cookie = new Cookie("sub", subStr);
        cookie.setDomain("artchains.cn");
        response.addCookie(cookie);
        response.setHeader("Access-Control-Allow-Origin","artchains.cn");
        response.setHeader("origin", "xh.artchains.cn");

        return new ResultMsg("backend_login", subStr);
    }

    @RequestMapping(value = "/order/select.json")
    public ResultMsg orderSelect(@RequestParam(name = "code", required = false, defaultValue = "") String code,
                                 @ParamVerify(isNumber = true) int page,
                                 @ParamVerify(isNumber = true) int count) {
        if (page <= 0 || count < 0) {
            throw new DrawServerException(DrawServerExceptionFactor.PARAM_COUNT_FAIL, "param is error");
        }

        return new ResultMsg("orderSelect", backService.getOrders(code, page, count));
    }


}
