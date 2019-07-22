package com.aiwsport.core.mapper;

import com.aiwsport.core.entity.Draws;

import java.util.List;

public interface DrawsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Draws record);

    Draws selectByPrimaryKey(Integer id);

    List<Draws> selectAll();

    int updateByPrimaryKey(Draws record);

    List<Draws> getIndex(int price, int id, int sort);

}