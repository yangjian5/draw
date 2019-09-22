package com.aiwsport.core.entity;

public class IncomeStatistics {
    private Integer id;

    private Integer drawId;

    private Integer incomePrice;

    private String createTime;

    private String type;

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

    public Integer getIncomePrice() {
        return incomePrice;
    }

    public void setIncomePrice(Integer incomePrice) {
        this.incomePrice = incomePrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}