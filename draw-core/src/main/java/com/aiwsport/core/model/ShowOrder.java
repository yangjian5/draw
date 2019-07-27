package com.aiwsport.core.model;

import com.aiwsport.core.entity.Draws;
import com.aiwsport.core.entity.Order;

public class ShowOrder {

    private Order order;

    private Draws draws;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Draws getDraws() {
        return draws;
    }

    public void setDraws(Draws draws) {
        this.draws = draws;
    }
}
