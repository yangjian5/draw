package com.aiwsport.core.mapper;

import com.aiwsport.core.entity.Order;
import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    Order selectByPrimaryKey(Integer id);

    List<Order> selectAll();

    int updateByPrimaryKey(Order record);

    Order getOrderByNo(String orderNo);
}