package com.aiwsport.core.model;

import com.aiwsport.core.entity.Draws;

import java.util.List;

public class ShowDraws {

    private String maxId;
    private List<Draws> draws;

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
