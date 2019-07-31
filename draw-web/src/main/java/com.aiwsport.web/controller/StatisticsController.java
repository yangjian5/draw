package com.aiwsport.web.controller;

import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.entity.IncomeStatistics;
import com.aiwsport.core.entity.OrderStatistics;
import com.aiwsport.core.service.StatisticsService;
import com.aiwsport.web.verify.ParamVerify;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 统计
 *
 * @author yangjian
 */
@RestController
@RequestMapping(value = "/api/")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;
    private static Logger logger = LogManager.getLogger();

    @RequestMapping(value = "/statistics.json")
    public ResultMsg statistics(@ParamVerify(isNumber = true)int draw_id, @ParamVerify(isNumber = true)int range) {
        List<IncomeStatistics> createIncomeList = statisticsService.incomeStatistics(draw_id, range);
        List<OrderStatistics> orderStatisticsList = statisticsService.orderStatistics(draw_id, range, 1);
        List<OrderStatistics> orderStatisticsList1 = statisticsService.orderStatistics(draw_id, range, 2);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("create_income_list", createIncomeList);
        jsonObject.put("create_prize_list", orderStatisticsList);
        jsonObject.put("owner_prize_list", orderStatisticsList1);
        return new ResultMsg("statistics", jsonObject);
    }
}
