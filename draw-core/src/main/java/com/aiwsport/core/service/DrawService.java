package com.aiwsport.core.service;

import com.aiwsport.core.DrawServerException;
import com.aiwsport.core.DrawServerExceptionFactor;
import com.aiwsport.core.entity.*;
import com.aiwsport.core.mapper.*;
import com.aiwsport.core.model.ShowBranner;
import com.aiwsport.core.model.ShowDrawExts;
import com.aiwsport.core.model.ShowDraws;
import com.aiwsport.core.utils.DataTypeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DrawService {

    @Autowired
    private DrawsMapper drawMapper;

    @Autowired
    private DrawExtMapper drawExtMapper;

    @Autowired
    private IncomeMapper incomeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IncomeStatisticsMapper incomeStatisticsMapper;

    @Autowired
    private DrawBrannerMapper drawBrannerMapper;

    @Autowired
    private OrderService orderService;

    private static Logger logger = LogManager.getLogger();

    public boolean createDraw(int uid, String name, String telNo, String drawName,
                              String author, String desc, String urlHd, String urlSimple, int drawWidth, int drawHigh) throws Exception {
        Draws draw = new Draws();
        draw.setDrawStatus("0");
        draw.setProdName(name);
        draw.setProdTel(telNo);
        draw.setProdUid(uid);
        draw.setAuthName(author);
        draw.setDrawName(drawName);
        draw.setDrawDesc(desc);
        draw.setUrlHd(urlHd);
        String time = DataTypeUtils.formatCurDateTime();
        draw.setUrlSimple(urlSimple);
        draw.setDrawPrice(0);
        draw.setOwnCount(0);
        draw.setDrawWidth(drawWidth);
        draw.setDrawHigh(drawHigh);
        draw.setOwnFinishCount(0);
        draw.setCreateTime(time);
        draw.setModifyTime(time);
        return drawMapper.insert(draw) > 0;
    }

    public List<ShowBranner> getBranner(){
        List<DrawBranner> drawBranners = drawBrannerMapper.selectAll();
        List<ShowBranner> showBranners = new ArrayList<>();
        drawBranners.forEach(drawBranner -> {
            ShowBranner showBranner = new ShowBranner();
            showBranner.setBranner(drawBranner);
            if ("2".equals(drawBranner.getType())) {
                Draws draws = drawMapper.selectByPrimaryKey(drawBranner.getId());
                showBranner.setDraws(draws);
            }
        });

        return showBranners;
    }

    public boolean updateDraw(int drawId, int createPrice, int ownerCount){
        Draws draw = drawMapper.selectByPrimaryKey(drawId);
        if (draw == null) {
            return false;
        }

        draw.setOwnCount(ownerCount);
        draw.setOwnFinishCount(0);
        draw.setDrawPrice(createPrice);
        return drawMapper.updateByPrimaryKey(draw) > 0;
    }

    public ShowDrawExts getDraws(int sort, String maxId){
        int start = 0;
        int end = start + 14;
        int id;
        int page = 1;
        if (StringUtils.isNotBlank(maxId)) {
            String[] maxs = maxId.split("-");
            id = Integer.parseInt(maxs[0]);
            start = (Integer.parseInt(maxs[1]) -1 ) * 15;
            end = start + 14;
            page = Integer.parseInt(maxs[1]) + 1;
        } else {
            Draws draw = drawMapper.getMaxOne();
            if (draw == null) {
                return new ShowDrawExts();
            }
            id = draw.getId();
        }

        List<Draws> draws = drawMapper.getIndex(id, start, end, sort);
        ShowDrawExts showDrawExts = buildShowDrawsIndex(draws);
        if (draws.size() < 15) {
            showDrawExts.setMaxId("-1");
        } else {
            showDrawExts.setMaxId(id + "-" + page);
        }
        return showDrawExts;
    }

    public ShowDrawExts getDrawExts(int sort, String maxId){
        int start = 0;
        int end = start + 14;
        int id;
        int page = 1;
        if (StringUtils.isNotBlank(maxId)) {
            String[] maxs = maxId.split("-");
            id = Integer.parseInt(maxs[0]);
            start = (Integer.parseInt(maxs[1]) -1 ) * 15;
            end = start + 14;
            page = Integer.parseInt(maxs[1]) + 1;
        } else {
            DrawExt drawExt = drawExtMapper.getMaxOne();
            if (drawExt == null) {
                return new ShowDrawExts();
            }
            id = drawExt.getId();
        }

        List<DrawExt> drawExts = drawExtMapper.getIndex(id, start, end, sort);
        ShowDrawExts showDrawExts = buildShowDrawExts(drawExts);
        if (drawExts.size() < 15) {
            showDrawExts.setMaxId("-1");
        } else {
            showDrawExts.setMaxId(id + "-" + page);
        }
        return showDrawExts;
    }

    public ShowDraws getMyDraws(int uid, String maxId){
        int start = 0;
        int end = start + 14;
        int page = 1;
        if (StringUtils.isNotBlank(maxId)) {
            start = (Integer.parseInt(maxId) -1 ) * 15;
            end = start + 14;
            page = Integer.parseInt(maxId) + 1;
        }

        List<Draws> draws = drawMapper.getMyList(uid, start, end);
        ShowDraws showDraws = buildShowDraws(draws);
        if (draws.size() < 15) {
            showDraws.setMaxId("-1");
        } else {
            showDraws.setMaxId(page+"");
        }
        return showDraws;
    }

    public ShowDrawExts getMyDrawExts(int uid, String maxId){
        int start = 0;
        int end = start + 14;
        int page = 1;
        if (StringUtils.isNotBlank(maxId)) {
            start = (Integer.parseInt(maxId) -1 ) * 15;
            end = start + 14;
            page = Integer.parseInt(maxId) + 1;
        }

        List<DrawExt> drawExts = drawExtMapper.getMyList(uid, start, end);
        ShowDrawExts showDrawExts = buildShowDrawExts(drawExts);
        if (drawExts.size() < 15) {
            showDrawExts.setMaxId("-1");
        } else {
            showDrawExts.setMaxId(page+"");
        }
        return showDrawExts;
    }

    public boolean updateDrawExt(int drawExtId, int extPrice){
        DrawExt drawExt = drawExtMapper.selectByPrimaryKey(drawExtId);
        if (drawExt == null) {
            return false;
        }
        drawExt.setExtPrice(extPrice);
        return drawExtMapper.updateByPrimaryKey(drawExt) > 0;
    }

    public String uploadIncome(String openId, int drawExtId, int incomePrice, String url) throws Exception {
        User user = userMapper.getByOpenId(openId);
        if (user == null || user.getId() == 0) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "user id is not exist");
        }

        DrawExt drawExt = drawExtMapper.selectByPrimaryKey(drawExtId);
        if (drawExt == null) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "draw ext id is not exist");
        }

        Map<String, Object> resMap = orderService.createWXOrder(openId, "127.0.0.1", "income-"+drawExtId,
                BigDecimal.valueOf(incomePrice).divide(BigDecimal.valueOf(100)).toString());

        if (resMap.isEmpty() || resMap.get("orderNo") == null) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "create order is fail");
        }

        String orderNo = (String) resMap.get("orderNo");
        String paySign = (String) resMap.get("paySign");

        Income income = new Income();
        income.setDrawExtId(drawExtId);
        income.setProofPrice(incomePrice);
        income.setProofUrl(url);
        income.setStatus("0");
        income.setInfo(paySign);
        income.setOrderNo(orderNo);
        String time = DataTypeUtils.formatCurDateTime();
        income.setCreateTime(time);
        income.setModifyTime(time);
        incomeMapper.insert(income);
        return paySign;
    }

    private ShowDrawExts buildShowDrawExts (List<DrawExt> drawExts) {
        drawExts.forEach(drawExt -> {
            Draws draws = drawMapper.selectByPrimaryKey(drawExt.getDrawId());
            drawExt.setDraws(draws);
        });

        ShowDrawExts showDrawExts = new ShowDrawExts();
        showDrawExts.setDrawExt(drawExts);
        return showDrawExts;
    }

    private ShowDrawExts buildShowDrawsIndex (List<Draws> draws) {
        ShowDrawExts showDrawExts = new ShowDrawExts();
        showDrawExts.setDrawExt(new ArrayList<>());
        draws.forEach(draws1 -> {
            DrawExt drawExt = drawExtMapper.getMaxPriceByDrawId(draws1.getId());
            drawExt.setDraws(draws1);
            showDrawExts.getDrawExt().add(drawExt);
        });
        return showDrawExts;
    }

    private ShowDraws buildShowDraws (List<Draws> draws) {
        ShowDraws showDraws = new ShowDraws();
        showDraws.setDraws(draws);
        return showDraws;
    }

    public List<Draws> getDrawsByDrawName(String drawName, int page, int count) {
        PageParam pageParam = new PageParam();
        pageParam.setStart((page - 1) * count);
        pageParam.setLength(count);
        pageParam.setDrawName(drawName);
        return drawMapper.getDrawsByDrawName(pageParam);
    }

    public int getDrawCount(String drawName) {
        return drawMapper.getCount(drawName);
    }



}
