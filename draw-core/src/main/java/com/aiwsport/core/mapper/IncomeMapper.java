package com.aiwsport.core.mapper;

import com.aiwsport.core.entity.Income;
import java.util.List;

public interface IncomeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Income record);

    Income selectByPrimaryKey(Integer id);

    List<Income> selectAll();

    int updateByPrimaryKey(Income record);

    Income getIncomeByOrderNo(String orderNo);

    List<Income> getPayFinish(int start, int end);

    int getPayFinishCount();
}