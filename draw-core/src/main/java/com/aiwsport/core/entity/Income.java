package com.aiwsport.core.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Income {
    private Integer id;

    private Integer drawExtId;

    private String proofUrl;

    private BigDecimal proofPrice;

    private String status;

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

    public Integer getDrawExtId() {
        return drawExtId;
    }

    public void setDrawExtId(Integer drawExtId) {
        this.drawExtId = drawExtId;
    }

    public String getProofUrl() {
        return proofUrl;
    }

    public void setProofUrl(String proofUrl) {
        this.proofUrl = proofUrl;
    }

    public BigDecimal getProofPrice() {
        return proofPrice;
    }

    public void setProofPrice(BigDecimal proofPrice) {
        this.proofPrice = proofPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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