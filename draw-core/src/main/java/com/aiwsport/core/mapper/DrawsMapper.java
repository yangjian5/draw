package com.aiwsport.core.mapper;

import com.aiwsport.core.entity.Draws;
import com.aiwsport.core.entity.PageParam;

import java.util.List;

public interface DrawsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Draws record);

    Draws selectByPrimaryKey(Integer id);

    List<Draws> selectAll();

    int updateByPrimaryKey(Draws record);

    List<Draws> getIndex(int id, int start, int end, int sort);

    Draws getMaxOne();

    List<Draws> getMyList(int uid, int start, int end);

    List<Draws> getDrawsByDrawName(PageParam pageParam);

    int getCount(String drawName);

    int updateOwnFinishCount(int drawId);
}