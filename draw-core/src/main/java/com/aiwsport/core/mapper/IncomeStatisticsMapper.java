package com.aiwsport.core.mapper;

import com.aiwsport.core.entity.IncomeStatistics;

import java.util.List;

public interface IncomeStatisticsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IncomeStatistics record);

    IncomeStatistics selectByPrimaryKey(Integer id);

    List<IncomeStatistics> selectAll();

    int updateByPrimaryKey(IncomeStatistics record);

    IncomeStatistics getIncomeByDrawIdAndDate(Integer drawId, String type);

    List<IncomeStatistics> getStatistics(Integer drawId, Integer range, String type);
}