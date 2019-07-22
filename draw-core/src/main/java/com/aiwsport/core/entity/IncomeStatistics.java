package com.aiwsport.core.entity;

import java.sql.Date;

public class IncomeStatistics {
    private Integer id;

    private Integer drawId;

    private Long incomePrice;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDrawId() {
        return drawId;
    }

    public void setDrawId(Integer drawId) {
        this.drawId = drawId;
    }

    public Long getIncomePrice() {
        return incomePrice;
    }

    public void setIncomePrice(Long incomePrice) {
        this.incomePrice = incomePrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}