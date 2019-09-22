package com.aiwsport.core.service;


import com.aiwsport.core.entity.IncomeStatistics;
import com.aiwsport.core.entity.OrderStatistics;
import com.aiwsport.core.mapper.IncomeStatisticsMapper;
import com.aiwsport.core.mapper.OrderStatisticsMapper;
import com.aiwsport.core.utils.DataTypeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private IncomeStatisticsMapper incomeStatisticsMapper;

    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;


    private static Logger logger = LogManager.getLogger();

    public List<IncomeStatistics> incomeStatistics(int drawId, int range, String type) {
        List<IncomeStatistics> incomeStatisticsList = incomeStatisticsMapper.getStatistics(drawId, range, type);
        int count = incomeStatisticsList.size();
        switch (range) {
            case 1:
                if (count < 7) {
                    try {
                        return buildStatistics(incomeStatisticsList, drawId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                if (count < 30) {
                    try {
                        return buildStatistics(incomeStatisticsList, drawId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 3:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

                String[] dateStr = sdf.format(new Date()).split("-");
                List<IncomeStatistics> resList = new ArrayList<>();
                if (incomeStatisticsList == null || incomeStatisticsList.size() == 0) {
                    int m = Integer.parseInt(dateStr[1])+1;
                    int y = Integer.parseInt(dateStr[0]);
                    for (int i=0; i<12; i++) {

                        m = m - 1;
                        if (m == 0) {
                            m = 12;
                            y = y -1;
                        }

                        IncomeStatistics incomeStatistics = new IncomeStatistics();
                        incomeStatistics.setCreateTime(y+"-"+m);
                        incomeStatistics.setIncomePrice(0);
                        incomeStatistics.setDrawId(drawId);
                        resList.add(incomeStatistics);
                    }
                    return resList;
                }

                resList = incomeStatisticsList.stream()
                        .peek(incomeStatistics -> {
                            String time = incomeStatistics.getCreateTime();
                            if (StringUtils.isNotBlank(time)) {
                                String yymmdd = time.split(" ")[0];
                                String yymm = yymmdd.substring(0, yymmdd.lastIndexOf("-"));
                                incomeStatistics.setCreateTime(yymm);
                            }
                        })
                        .collect(Collectors.groupingBy(IncomeStatistics::getCreateTime, Collectors.toList()))
                        .entrySet().stream()
                        .map(entry -> {
                            int sumPrice = entry.getValue().stream().map(IncomeStatistics::getIncomePrice).reduce(Integer::sum).orElse(0);
                            IncomeStatistics incomeStatistics = new IncomeStatistics();
                            incomeStatistics.setCreateTime(entry.getKey());
                            incomeStatistics.setIncomePrice(sumPrice);
                            incomeStatistics.setDrawId(drawId);
                            return incomeStatistics;
                        }).sorted((o1, o2) -> {
                            long i = 1;
                            try {
                                i = sdf.parse(o1.getCreateTime()).getTime() - sdf.parse(o2.getCreateTime()).getTime();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return (int)i;
                        }).collect(Collectors.toList());

            if (resList.size() < 12) {
                IncomeStatistics is = resList.get(resList.size()-1);
                String[] dStr = is.getCreateTime().split("-");
                int m1 = Integer.parseInt(dStr[1]);
                int y1 = Integer.parseInt(dStr[0]);
                for (int j=0; j<12-resList.size(); j++) {
                    m1 = m1 - 1;
                    if (m1 == 0) {
                        m1 = 12;
                        y1 = y1 -1;
                    }

                    IncomeStatistics incomeStatistics = new IncomeStatistics();
                    incomeStatistics.setCreateTime(y1+"-"+m1);
                    incomeStatistics.setIncomePrice(0);
                    incomeStatistics.setDrawId(drawId);
                    resList.add(incomeStatistics);
                }
            }
            return resList;
        }
        return incomeStatisticsList;
    }

    public List<OrderStatistics> orderStatistics(int drawId, int range, int type) {
        List<OrderStatistics> orderStatisticsList = orderStatisticsMapper.getStatistics(drawId, range, type);
        int count = orderStatisticsList.size();
        switch (range) {
            case 1:
                if (count < 7) {
                    try {
                        return buildOrderStatistics(orderStatisticsList, drawId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                if (count < 30) {
                    try {
                        return buildOrderStatistics(orderStatisticsList, drawId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 3:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

                String[] dateStr = sdf.format(new Date()).split("-");
                List<OrderStatistics> resList = new ArrayList<>();
                if (orderStatisticsList == null || orderStatisticsList.size() == 0) {
                    int m = Integer.parseInt(dateStr[1])+1;
                    int y = Integer.parseInt(dateStr[0]);
                    for (int i=0; i<12; i++) {

                        m = m - 1;
                        if (m == 0) {
                            m = 12;
                            y = y -1;
                        }

                        OrderStatistics orderStatistics = new OrderStatistics();
                        orderStatistics.setCreateTime(y+"-"+m);
                        orderStatistics.setsPrice(0);
                        orderStatistics.setDrawId(drawId);
                        resList.add(orderStatistics);
                    }
                    return resList;
                }

                resList = orderStatisticsList.stream()
                        .peek(orderStatistics -> {
                            String time = orderStatistics.getCreateTime();
                            if (StringUtils.isNotBlank(time)) {
                                String yymmdd = time.split(" ")[0];
                                String yymm = yymmdd.substring(0, yymmdd.lastIndexOf("-"));
                                orderStatistics.setCreateTime(yymm);
                            }
                        })
                        .collect(Collectors.groupingBy(OrderStatistics::getCreateTime, Collectors.toList()))
                        .entrySet().stream()
                        .map(entry -> {
                            int sumPrice = entry.getValue().stream().map(OrderStatistics::getsPrice).reduce(Integer::sum).orElse(0);
                            OrderStatistics orderStatistics = new OrderStatistics();
                            orderStatistics.setCreateTime(entry.getKey());
                            orderStatistics.setsPrice(sumPrice);
                            orderStatistics.setDrawId(drawId);
                            return orderStatistics;
                        }).sorted((o1, o2) -> {
                            long i = 1;
                            try {
                                i = sdf.parse(o1.getCreateTime()).getTime() - sdf.parse(o2.getCreateTime()).getTime();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return (int)i;
                        }).collect(Collectors.toList());

                if (resList.size() < 12) {
                    OrderStatistics os = resList.get(resList.size()-1);
                    String[] dStr = os.getCreateTime().split("-");
                    int m1 = Integer.parseInt(dStr[1]);
                    int y1 = Integer.parseInt(dStr[0]);
                    for (int j=0; j<12-resList.size(); j++) {
                        m1 = m1 - 1;
                        if (m1 == 0) {
                            m1 = 12;
                            y1 = y1 -1;
                        }

                        OrderStatistics orderStatistics = new OrderStatistics();
                        orderStatistics.setCreateTime(y1+"-"+m1);
                        orderStatistics.setsPrice(0);
                        orderStatistics.setDrawId(drawId);
                        resList.add(orderStatistics);
                    }
                }
                return resList;
        }

        return orderStatisticsMapper.getStatistics(drawId, range, type);
    }

    private List<OrderStatistics> buildOrderStatistics(List<OrderStatistics> orderStatisticsList, int drawId) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long curDateTime = sdf.parse(DataTypeUtils.formatCurDateTime_yyyy_mm_dd()).getTime();
        long startTime = curDateTime - 6 * 24 * 3600 * 1000;

        List<OrderStatistics> statistics = new ArrayList<>();
        for (OrderStatistics stat: orderStatisticsList) {
            String indexTime = stat.getCreateTime().substring(0, stat.getCreateTime().lastIndexOf(" "));
            long thisTime = sdf.parse(indexTime).getTime();
            while (startTime != thisTime && startTime <= curDateTime) {
                OrderStatistics orderStatistics = new OrderStatistics();
                orderStatistics.setCreateTime(sdf.format(new Date(startTime)));
                orderStatistics.setsPrice(0);
                orderStatistics.setDrawId(drawId);
                statistics.add(orderStatistics);
                startTime = startTime + 24 * 3600 * 1000;
            }
            stat.setCreateTime(indexTime);
            statistics.add(stat);
            startTime = startTime + 24 * 3600 * 1000;
        }
        int hasCount = 7 - statistics.size();
        for (int i=0; i<hasCount; i++) {
            OrderStatistics orderStatistics = new OrderStatistics();
            orderStatistics.setCreateTime(sdf.format(new Date(startTime)));
            orderStatistics.setsPrice(0);
            orderStatistics.setDrawId(drawId);
            statistics.add(orderStatistics);
            startTime = startTime + 24 * 3600 * 1000;
        }
        return statistics;
    }

    private List<IncomeStatistics> buildStatistics(List<IncomeStatistics> incomeStatisticsList, int drawId) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long curDateTime = sdf.parse(DataTypeUtils.formatCurDateTime_yyyy_mm_dd()).getTime();
        long startTime = curDateTime - 6 * 24 * 3600 * 1000;

        List<IncomeStatistics> statistics = new ArrayList<>();
        for (IncomeStatistics stat: incomeStatisticsList) {
            String indexTime = stat.getCreateTime().substring(0, stat.getCreateTime().lastIndexOf(" "));
            long thisTime = sdf.parse(indexTime).getTime();
            while (startTime != thisTime && startTime <= curDateTime) {
                IncomeStatistics incomeStatistics = new IncomeStatistics();
                incomeStatistics.setCreateTime(sdf.format(new Date(startTime)));
                incomeStatistics.setIncomePrice(0);
                incomeStatistics.setDrawId(drawId);
                statistics.add(incomeStatistics);
                startTime = startTime + 24 * 3600 * 1000;
            }
            stat.setCreateTime(indexTime);
            statistics.add(stat);
            startTime = startTime + 24 * 3600 * 1000;
        }
        int hasCount = 7 - statistics.size();
        for (int i=0; i<hasCount; i++) {
            IncomeStatistics incomeStatistics = new IncomeStatistics();
            incomeStatistics.setCreateTime(sdf.format(new Date(startTime)));
            incomeStatistics.setIncomePrice(0);
            incomeStatistics.setDrawId(drawId);
            statistics.add(incomeStatistics);
            startTime = startTime + 24 * 3600 * 1000;
        }
        return statistics;
    }
}
