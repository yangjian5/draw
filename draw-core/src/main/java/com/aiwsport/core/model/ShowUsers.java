package com.aiwsport.core.model;

import com.aiwsport.core.entity.User;

import java.util.List;

/**
 * @author pangxin1
 * @date 2019-07-28 17:05
 */
public class ShowUsers {

    private int page;
    private int count;
    private int totalCount;
    private List<User> users;

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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
