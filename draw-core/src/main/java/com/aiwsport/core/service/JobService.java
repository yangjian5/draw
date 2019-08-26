package com.aiwsport.core.service;

import com.aiwsport.core.entity.OrderCheck;
import com.aiwsport.core.mapper.OrderCheckMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pangxin1
 * @date 2019-08-27 00:39
 */
@Service
@EnableScheduling
public class JobService {

    @Autowired
    private OrderCheckMapper orderCheckMapper;

    //3.添加定时任务
    @Scheduled(cron = "0/5 * * * * ?")
    private void configureTasks() {
        Long time = System.currentTimeMillis();
        List<OrderCheck> orderChecks = orderCheckMapper.selectByJob();
        for (OrderCheck orderCheck : orderChecks) {
            if (orderCheck.getCreateTime() < time) {
                orderCheckMapper.deleteByPrimaryKey(orderCheck.getId());
            } else {
                break;
            }
        }
    }
}
