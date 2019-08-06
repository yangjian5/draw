package com.aiwsport.core.model;

import com.aiwsport.core.entity.Draws;
import com.aiwsport.core.entity.Order;

import java.util.List;

public class ShowBackOrder {


    private List<ShowOrder> showOrders;
    private int page;
    private int count;
    private int totalCount;

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

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ShowOrder> getShowOrders() {
        return showOrders;
    }

    public void setShowOrders(List<ShowOrder> showOrders) {
        this.showOrders = showOrders;
    }
}
