package com.aiwsport.core.mapper;

import com.aiwsport.core.entity.DrawExt;
import java.util.List;

public interface DrawExtMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DrawExt record);

    DrawExt selectByPrimaryKey(Integer id);

    List<DrawExt> selectAll();

    int updateByPrimaryKey(DrawExt record);
}