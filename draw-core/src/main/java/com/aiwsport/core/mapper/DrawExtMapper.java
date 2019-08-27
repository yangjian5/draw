package com.aiwsport.core.mapper;

import com.aiwsport.core.entity.DrawExt;

import java.util.List;

public interface DrawExtMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DrawExt record);

    DrawExt selectByPrimaryKey(Integer id);

    List<DrawExt> selectAll();

    int updateByPrimaryKey(DrawExt record);

    List<DrawExt> getIndex(int id, int start, int end, int sort);

    DrawExt getMaxOne();

    List<DrawExt> getMyList(int uid, int start, int end);

    DrawExt getMaxPriceByDrawId(int drawId);

    DrawExt getMaxPriceByDrawIda(int drawId);

    int deleteDrawExt(int drawId);

    int getCount(int drawId, int uid);

    int getIsSaleCount(int drawId);

    int updateExtStatus(String drawStatus, int drawId);
}