package com.aiwsport.core.service;


import com.aiwsport.core.entity.OperLog;
import com.aiwsport.core.entity.PageParam;
import com.aiwsport.core.entity.User;
import com.aiwsport.core.mapper.OperLogMapper;
import com.aiwsport.core.mapper.UserMapper;
import com.aiwsport.core.utils.DataTypeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OperLogMapper operLogMapper;

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

    public List<OperLog> getOpLog(Integer uid) {
        List<OperLog> operLogs = operLogMapper.getByUid(uid);
        operLogs.forEach(operLog -> {
            String type = operLog.getType();
            if ("1".equals(type)) {
                operLog.setType("创造权交易");
            } else if ("2".equals(type)) {
                operLog.setType("所有权交易收益");
            } else if ("3".equals(type)) {
                operLog.setType("藏品收益");
            } else if ("4".equals(type)) {
                operLog.setType("收益提现");
            }

            String time = operLog.getCreateTime();
            if (StringUtils.isNotBlank(time)) {
                operLog.setCreateTime(operLog.getCreateTime().substring(0, time.lastIndexOf(".")));
            }
        });
        return operLogs;
    }


    public List<User> getUsersByNickName(String nickName, Integer page, Integer count) {
        PageParam pageParam = new PageParam();
        pageParam.setStart((page - 1) * count);
        pageParam.setLength(count);
        pageParam.setNickName(nickName);
        return userMapper.getUsersByNickName(pageParam);
    }

    public boolean withdrawal(int uid, String tradeNo, int price) {
        OperLog operLog = new OperLog();
        operLog.setUid(uid);
        operLog.setOrderId(0);
        operLog.setIncomeId(0);
        operLog.setType("4");
        operLog.setTradeno(tradeNo);
        operLog.setIncomePrice(price);
        String time = "2019-10-10 00:00:01";
        try {
            time = DataTypeUtils.formatCurDateTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        operLog.setCreateTime(time);
        operLog.setModifyTime(time);
        return operLogMapper.insert(operLog) > 0;
    }

    public int getUsersCount(String nickName) {
        return userMapper.getCount(nickName);
    }

}
