package com.aiwsport.core.mapper;

import com.aiwsport.core.entity.DrawBranner;
import java.util.List;

public interface DrawBrannerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DrawBranner record);

    DrawBranner selectByPrimaryKey(Integer id);

    List<DrawBranner> selectAll();

    int updateByPrimaryKey(DrawBranner record);

    int updateByPrimaryKeyWithIf(DrawBranner record);

}