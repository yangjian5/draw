package com.aiwsport.core.entity;

import java.util.Date;

public class DrawExt {
    private Integer id;

    private Integer extUid;

    private Integer drawId;

    private int extPrice;

    private String opName;

    private String extStatus;

    private String extIsSale;

    private Integer opId;

    private Date createTime;

    private Date modifyTime;

    private Draws draws;

    private Integer count;

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

    public int getExtPrice() {
        return extPrice;
    }

    public void setExtPrice(int extPrice) {
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

    public Draws getDraws() {
        return draws;
    }

    public void setDraws(Draws draws) {
        this.draws = draws;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getExtStatus() {
        return extStatus;
    }

    public void setExtStatus(String extStatus) {
        this.extStatus = extStatus;
    }

    public String getExtIsSale() {
        return extIsSale;
    }

    public void setExtIsSale(String extIsSale) {
        this.extIsSale = extIsSale;
    }
}
