package com.aiwsport.core.service;

import com.aiwsport.core.entity.DrawBranner;
import com.aiwsport.core.entity.Draws;
import com.aiwsport.core.entity.User;
import com.aiwsport.core.mapper.DrawBrannerMapper;
import com.aiwsport.core.model.ShowDraws;
import com.aiwsport.core.model.ShowUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pangxin1
 * @date 2019-07-28 16:11
 */
@Service
public class BackService {
    @Autowired
    private DrawBrannerMapper drawBrannerMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private DrawService drawService;

    public boolean bannerUpdate(DrawBranner drawBranner) {
        return drawBrannerMapper.updateByPrimaryKey(drawBranner) > 0;
    }

    public boolean bannerInsert(DrawBranner drawBranner) {
        return drawBrannerMapper.insert(drawBranner) > 0;
    }

    public boolean bannerDelete(int id) {
        return drawBrannerMapper.deleteByPrimaryKey(id) > 0;
    }

    public ShowUsers showUsers(String nickName, int page, int count) {
        List<User> usersByNickName = userService.getUsersByNickName(nickName, page, count);
        int usersCount = userService.getUsersCount(nickName);
        ShowUsers showUsers = new ShowUsers();
        showUsers.setCount(count);
        showUsers.setPage(page);
        showUsers.setTotalCount(usersCount);
        showUsers.setUsers(usersByNickName);
        return showUsers;
    }

    public ShowDraws showDraws(String draw, int page, int count) {
        List<Draws> drawsByDrawName = drawService.getDrawsByDrawName(draw, page, count);
        int drawCount = drawService.getDrawCount(draw);
        ShowDraws showDraws = new ShowDraws();
        showDraws.setCount(count);
        showDraws.setPage(page);
        showDraws.setTotalCount(drawCount);
        showDraws.setDraws(drawsByDrawName);
        return showDraws;
    }
}
