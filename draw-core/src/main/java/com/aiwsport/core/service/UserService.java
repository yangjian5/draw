package com.aiwsport.core.service;


import com.aiwsport.core.entity.User;
import com.aiwsport.core.mapper.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    private static Logger logger = LogManager.getLogger();

    public User login(String openId, String nickName, String avatarUrl, String gender, String income) {
        // 获取openid {"session_key":"VEX3GZ5cG31i1+DeLyqHyg==","openid":"ov8p35OEtLxO7nILiHq6rmBCpkv4"}
        User user = userMapper.getByOpenId(openId);
        if (user == null) {
            // 创建用户
            User userNew = new User();
            userNew.setOpenId(openId);
            userNew.setAvatarUrl(avatarUrl);
            userNew.setGender(gender);
            userNew.setNickName(nickName);
            userNew.setIncome(new BigDecimal(0));
            userMapper.insert(userNew);
            return userNew;
        } else {
            if (!avatarUrl.equals(user.getAvatarUrl())) {
                user.setAvatarUrl(avatarUrl);
            }
            if (!nickName.equals(user.getNickName())) {
                user.setNickName(nickName);
            }
            userMapper.updateByPrimaryKey(user);
        }
        return user;
    }


}
