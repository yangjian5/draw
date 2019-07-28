package com.aiwsport.core.model;

import com.aiwsport.core.entity.DrawBranner;
import com.aiwsport.core.entity.Draws;

public class ShowBranner {

    private DrawBranner branner;

    private Draws draws;

    public DrawBranner getBranner() {
        return branner;
    }

    public void setBranner(DrawBranner branner) {
        this.branner = branner;
    }

    public Draws getDraws() {
        return draws;
    }

    public void setDraws(Draws draws) {
        this.draws = draws;
    }
}
