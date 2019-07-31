package com.aiwsport.core.service;


import com.aiwsport.core.entity.IncomeStatistics;
import com.aiwsport.core.entity.OrderStatistics;
import com.aiwsport.core.mapper.IncomeStatisticsMapper;
import com.aiwsport.core.mapper.OrderStatisticsMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    @Autowired
    private IncomeStatisticsMapper incomeStatisticsMapper;

    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;


    private static Logger logger = LogManager.getLogger();

    public List<IncomeStatistics> incomeStatistics(int drawId, int range) {
        return incomeStatisticsMapper.getStatistics(drawId, range);
    }

    public List<OrderStatistics> orderStatistics(int drawId, int range, int type) {
        return orderStatisticsMapper.getStatistics(drawId, range, type);
    }


}
