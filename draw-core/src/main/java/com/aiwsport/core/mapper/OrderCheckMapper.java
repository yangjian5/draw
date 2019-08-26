package com.aiwsport.core.mapper;

import com.aiwsport.core.entity.OrderCheck;

import java.util.List;

public interface OrderCheckMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderCheck record);

    OrderCheck selectByPrimaryKey(Integer id);

    List<OrderCheck> selectAll();

    List<OrderCheck> selectByJob();

    int updateByPrimaryKey(OrderCheck record);
}