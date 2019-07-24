package com.aiwsport.core.model;

import com.aiwsport.core.entity.DrawExt;

import java.util.List;

public class ShowDrawExts {

    private String maxId;

    private List<DrawExt> drawExt;

    public String getMaxId() {
        return maxId;
    }

    public void setMaxId(String maxId) {
        this.maxId = maxId;
    }

    public List<DrawExt> getDrawExt() {
        return drawExt;
    }

    public void setDrawExt(List<DrawExt> drawExt) {
        this.drawExt = drawExt;
    }
}
