package com.aiwsport.core.mapper;

import com.aiwsport.core.entity.WebOrderLog;

import java.util.List;

public interface WebOrderLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WebOrderLog record);

    WebOrderLog selectByPrimaryKey(Integer id);

    List<WebOrderLog> selectAll();

    int updateByPrimaryKey(WebOrderLog record);
}