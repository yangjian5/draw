package com.aiwsport.core.entity;

public class OrderStatistics {
    private Integer id;

    private Integer drawId;

    private Integer sPrice;

    private Integer createTime;

    private Integer type;

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

    public Integer getsPrice() {
        return sPrice;
    }

    public void setsPrice(Integer sPrice) {
        this.sPrice = sPrice;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}