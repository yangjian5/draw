package com.aiwsport.core.mapper;

import com.aiwsport.core.entity.Draw;
import java.util.List;

public interface DrawMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Draw record);

    Draw selectByPrimaryKey(Integer id);

    List<Draw> selectAll();

    int updateByPrimaryKey(Draw record);
}