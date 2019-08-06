package com.aiwsport.core.entity;

import java.util.Date;

public class DrawBranner {
    private Integer id;

    private String brannerUrl;

    private String clickUrl;

    private String type;

    private Integer drawId;

    private Integer drawExtId;

    private Integer sort;

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

    public String getBrannerUrl() {
        return brannerUrl;
    }

    public void setBrannerUrl(String brannerUrl) {
        this.brannerUrl = brannerUrl;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDrawId() {
        return drawId;
    }

    public void setDrawId(Integer drawId) {
        this.drawId = drawId;
    }

    public Integer getDrawExtId() {
        return drawExtId;
    }

    public void setDrawExtId(Integer drawExtId) {
        this.drawExtId = drawExtId;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}