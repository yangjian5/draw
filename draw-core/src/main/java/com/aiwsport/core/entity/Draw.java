package com.aiwsport.core.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Draw {
    private Integer id;

    private Integer prodUid;

    private String prodName;

    private String prodTel;

    private BigDecimal drawPrice;

    private String drawName;

    private String drawStatus;

    private String authName;

    private String urlHd;

    private String urlSimple;

    private String desc;

    private Integer ownCount;

    private Integer ownFinishCount;

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

    public Integer getProdUid() {
        return prodUid;
    }

    public void setProdUid(Integer prodUid) {
        this.prodUid = prodUid;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdTel() {
        return prodTel;
    }

    public void setProdTel(String prodTel) {
        this.prodTel = prodTel;
    }

    public BigDecimal getDrawPrice() {
        return drawPrice;
    }

    public void setDrawPrice(BigDecimal drawPrice) {
        this.drawPrice = drawPrice;
    }

    public String getDrawName() {
        return drawName;
    }

    public void setDrawName(String drawName) {
        this.drawName = drawName;
    }

    public String getDrawStatus() {
        return drawStatus;
    }

    public void setDrawStatus(String drawStatus) {
        this.drawStatus = drawStatus;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getUrlHd() {
        return urlHd;
    }

    public void setUrlHd(String urlHd) {
        this.urlHd = urlHd;
    }

    public String getUrlSimple() {
        return urlSimple;
    }

    public void setUrlSimple(String urlSimple) {
        this.urlSimple = urlSimple;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getOwnCount() {
        return ownCount;
    }

    public void setOwnCount(Integer ownCount) {
        this.ownCount = ownCount;
    }

    public Integer getOwnFinishCount() {
        return ownFinishCount;
    }

    public void setOwnFinishCount(Integer ownFinishCount) {
        this.ownFinishCount = ownFinishCount;
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