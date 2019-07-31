package com.aiwsport.core.mapper;

import com.aiwsport.core.entity.OrderStatistics;
import java.util.List;

public interface OrderStatisticsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderStatistics record);

    OrderStatistics selectByPrimaryKey(Integer id);

    List<OrderStatistics> selectAll();

    int updateByPrimaryKey(OrderStatistics record);

    List<OrderStatistics> getStatistics(Integer drawId, Integer range, Integer type);


}