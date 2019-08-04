package com.aiwsport.core.mapper;

import com.aiwsport.core.entity.OperLog;
import java.util.List;

public interface OperLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OperLog record);

    OperLog selectByPrimaryKey(Integer id);

    List<OperLog> selectAll();

    int updateByPrimaryKey(OperLog record);

    List<OperLog> getByUid(Integer uid);
}