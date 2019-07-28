package com.aiwsport.core.service;


import com.aiwsport.core.entity.PageParam;
import com.aiwsport.core.entity.User;
import com.aiwsport.core.mapper.UserMapper;
import com.aiwsport.core.utils.DataTypeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    private static Logger logger = LogManager.getLogger();

    public User login(String openid, String nickName, String avatarUrl, String gender) throws Exception {
        User user = userMapper.getByOpenId(openid);
        if (user == null) {
            // 创建用户
            User userNew = new User();
            userNew.setOpenId(openid);
            userNew.setAvatarUrl(avatarUrl);
            userNew.setGender(gender);
            userNew.setNickName(nickName);
            userNew.setIncome(0);
            String time = DataTypeUtils.formatCurDateTime();
            userNew.setCreateTime(time);
            userNew.setModifyTime(time);
            userMapper.insert(userNew);
            return userNew;
        } else {
            if (!(avatarUrl.equals(user.getAvatarUrl()) && nickName.equals(user.getNickName()))) {
                user.setAvatarUrl(avatarUrl);
                user.setNickName(nickName);
                userMapper.updateByPrimaryKey(user);
            }
        }
        return user;
    }

    public User getUser(String openid) {
        return userMapper.getByOpenId(openid);
    }

    public List<User> getUsersByNickName(String nickName, Integer page, Integer count) {
        PageParam pageParam = new PageParam();
        pageParam.setStart((page - 1) * count);
        pageParam.setLength(count);
        pageParam.setNickName(nickName);
        return userMapper.getUsersByNickName(pageParam);
    }

    public int getUsersCount(String nickName) {
        return userMapper.getCount(nickName);
    }

}
