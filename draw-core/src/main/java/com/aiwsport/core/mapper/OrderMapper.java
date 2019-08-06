package com.aiwsport.core.mapper;

import com.aiwsport.core.entity.Draws;
import com.aiwsport.core.entity.Order;
import com.aiwsport.core.entity.PageParam;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    Order selectByPrimaryKey(Integer id);

    List<Order> selectAll();

    int updateByPrimaryKey(Order record);

    Order getOrderByNo(String orderNo);

    List<Order> getOrderByUid(Integer uid, String status);

    List<Order> getOrderByCode(PageParam pageParam);

    int getCount(String code);
}