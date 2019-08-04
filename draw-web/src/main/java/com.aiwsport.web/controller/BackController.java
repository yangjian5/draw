package com.aiwsport.web.controller;

import com.aiwsport.core.DrawServerException;
import com.aiwsport.core.DrawServerExceptionFactor;
import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.entity.DrawBranner;
import com.aiwsport.core.service.BackService;
import com.aiwsport.web.verify.ParamVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页展示
 *
 * @author yangjian
 */
@RestController
@RequestMapping(value = "/api/")
public class BackController {

    @Autowired
    private BackService backService;


    @RequestMapping(value = "/banner/update.json")
    public ResultMsg bannerUpdate(DrawBranner branner) {
        return new ResultMsg("update", backService.bannerUpdate(branner));
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
    public ResultMsg userSelect(@ParamVerify(isNotNull = true) String nickName,
                                @ParamVerify(isNumber = true) int page,
                                @ParamVerify(isNumber = true) int count) {
        if (page <= 0 || count < 0) {
            throw new DrawServerException(DrawServerExceptionFactor.PARAM_COUNT_FAIL, "param is error");
        }
        return new ResultMsg("userSelect", backService.showUsers(nickName, page, count));
    }


    @RequestMapping(value = "/draw/select.json")
    public ResultMsg drawSelect(@ParamVerify(isNotNull = true) String drawName,
                                @ParamVerify(isNumber = true) int page,
                                @ParamVerify(isNumber = true) int count) {
        if (page <= 0 || count < 0) {
            throw new DrawServerException(DrawServerExceptionFactor.PARAM_COUNT_FAIL, "param is error");
        }
        return new ResultMsg("drawSelect", backService.showDraws(drawName, page, count));
    }


}
