package com.aiwsport.core.service;

import com.aiwsport.core.entity.*;
import com.aiwsport.core.mapper.*;
import com.aiwsport.core.model.*;
import com.aiwsport.core.utils.DataTypeUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private DrawsMapper drawMapper;

    @Autowired
    private DrawExtMapper drawExtMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private IncomeMapper incomeMapper;

    @Autowired
    private IncomeStatisticsMapper incomeStatisticsMapper;

    @Autowired
    private OperLogMapper operLogMapper;

    @Autowired
    private UserMapper userMapper;


    public boolean bannerUpdate(DrawBranner drawBranner) {
        return drawBrannerMapper.updateByPrimaryKeyWithIf(drawBranner) > 0;
    }

    public boolean bannerInsert(DrawBranner drawBranner) {
        drawBranner.setOpId(1);
        drawBranner.setOpName("admin");
        //目前 brannerUrl 没有
        drawBranner.setBrannerUrl("");
        Draws draws = drawMapper.selectByPrimaryKey(drawBranner.getId());
        if(draws!= null){
            drawBranner.setDrawExtId(draws.getId());
        }
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

    public ShowIncome showIncome(int page, int count, String status) {
        int start = (page -1)*count;
        int end = page*count-1;
        List<Income> incomes = incomeMapper.getPayFinish(start, end, status);
        int payFinishCount = incomeMapper.getPayFinishCount(status);
        ShowIncome showIncome = new ShowIncome();
        showIncome.setCount(count);
        showIncome.setPage(page);
        showIncome.setTotalCount(payFinishCount);
        JSONArray jsonArray = new JSONArray();
        for (Income income : incomes) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("income", income);
            DrawExt drawExt = drawExtMapper.selectByPrimaryKey(income.getDrawExtid());
            Draws draws = drawMapper.selectByPrimaryKey(drawExt.getDrawId());
            jsonObject.put("draws", draws);
            jsonArray.add(jsonObject);
        }
        showIncome.setJsonArray(jsonArray);
        return showIncome;
    }

    public boolean drawCheck(int id, int drawStatus) {
        Draws draws = new Draws();
        draws.setId(id);
        draws.setDrawStatus(drawStatus + "");

        DrawExt drawExt = drawExtMapper.getMaxPriceByDrawIda(id);
        if (drawExt == null) {
            return false;
        }

        drawExtMapper.updateExtStatus(drawStatus+"", id);
        return drawMapper.updateDrawsStatus(draws) > 0;
    }

    public synchronized boolean incomeCheck(int id, String status) throws Exception{
        Income income = incomeMapper.selectByPrimaryKey(id);

        if (income == null) {
            return false;
        }

        if (!"1".equals(income.getStatus())) {
            return false;
        }

        if ("3".equals(status)) {
            income.setStatus(status);
            incomeMapper.updateByPrimaryKey(income);
            return true;
        }

        DrawExt drawExt = drawExtMapper.selectByPrimaryKey(income.getDrawExtid());
        Draws draws = drawMapper.selectByPrimaryKey(drawExt.getDrawId());

        IncomeStatistics incomeStatistics = incomeStatisticsMapper.getIncomeByDrawIdAndDate(drawExt.getDrawId());
        if (incomeStatistics == null) {
            incomeStatistics = new IncomeStatistics();
            incomeStatistics.setDrawId(draws.getId());
            incomeStatistics.setIncomePrice(income.getProofPrice());
            incomeStatistics.setCreateTime(DataTypeUtils.formatCurDateTime());
            incomeStatisticsMapper.insert(incomeStatistics);
        } else {
            int sumIncome = incomeStatistics.getIncomePrice() + income.getProofPrice();
            incomeStatistics.setIncomePrice(sumIncome);
            incomeStatisticsMapper.updateByPrimaryKey(incomeStatistics);
        }

        User user = userMapper.selectByPrimaryKey(draws.getProdUid());
        user.setIncome(user.getIncome() + income.getProofPrice());
        userMapper.updateByPrimaryKey(user);

        income.setStatus(status);
        incomeMapper.updateByPrimaryKey(income);

        OperLog operLog = new OperLog();
        operLog.setUid(user.getId());
        operLog.setOrderId(0);
        operLog.setIncomeId(income.getId());
        operLog.setType("3");
        operLog.setTradeno(income.getOrderNo());
        operLog.setIncomePrice(income.getProofPrice());
        operLogMapper.insert(operLog);
        return true;
    }

    public boolean refund(int id){
        return incomeMapper.refund(id) > 0;
    }

    public ShowBackOrder getOrders(String code, int page, int count) {
        List<Order> orders = orderService.getOrderByCode(code, page, count);
        int orderCount = orderService.getOrderCount(code);

        List<ShowOrder> showOrders = new ArrayList<>();
        for (Order order : orders) {
            ShowOrder showOrder = new ShowOrder();
            showOrder.setOrder(order);
            showOrder.setDraws(drawMapper.selectByPrimaryKey(order.getId()));
            showOrders.add(showOrder);
        }

        ShowBackOrder showBackOrder = new ShowBackOrder();
        showBackOrder.setShowOrders(showOrders);
        showBackOrder.setCount(count);
        showBackOrder.setPage(page);
        showBackOrder.setTotalCount(orderCount);
        return showBackOrder;
    }
}
