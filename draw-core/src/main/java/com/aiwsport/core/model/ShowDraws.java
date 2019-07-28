package com.aiwsport.core.model;

import com.aiwsport.core.entity.Draws;

import java.util.List;

public class ShowDraws {

    private String maxId;
    private List<Draws> draws;
    private int page;
    private int count;
    private int totalCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMaxId() {
        return maxId;
    }

    public void setMaxId(String maxId) {
        this.maxId = maxId;
    }

    public List<Draws> getDraws() {
        return draws;
    }

    public void setDraws(List<Draws> draws) {
        this.draws = draws;
    }
}
