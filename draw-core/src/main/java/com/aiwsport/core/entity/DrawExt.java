package com.aiwsport.core.entity;

import java.math.BigDecimal;
import java.util.Date;

public class DrawExt {
    private Integer id;

    private Integer extUid;

    private Integer drawId;

    private BigDecimal extPrice;

    private String opName;

    private Integer opId;

    private Date createTime;

    private Date modifyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExtUid() {
        return extUid;
    }

    public void setExtUid(Integer extUid) {
        this.extUid = extUid;
    }

    public Integer getDrawId() {
        return drawId;
    }

    public void setDrawId(Integer drawId) {
        this.drawId = drawId;
    }

    public BigDecimal getExtPrice() {
        return extPrice;
    }

    public void setExtPrice(BigDecimal extPrice) {
        this.extPrice = extPrice;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public Integer getOpId() {
        return opId;
    }

    public void setOpId(Integer opId) {
        this.opId = opId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}