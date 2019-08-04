package com.aiwsport.core.entity;

public class Income {
    private Integer id;

    private Integer drawExtid;

    private String proofUrl;

    private Integer proofPrice;

    private String info;

    private String orderNo;

    private String status;

    private String opName;

    private Integer opId;

    private String createTime;

    private String modifyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDrawExtid() {
        return drawExtid;
    }

    public void setDrawExtid(Integer drawExtid) {
        this.drawExtid = drawExtid;
    }

    public String getProofUrl() {
        return proofUrl;
    }

    public void setProofUrl(String proofUrl) {
        this.proofUrl = proofUrl;
    }

    public Integer getProofPrice() {
        return proofPrice;
    }

    public void setProofPrice(Integer proofPrice) {
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}