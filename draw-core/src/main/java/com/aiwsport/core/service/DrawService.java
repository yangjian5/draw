package com.aiwsport.core.service;

import com.aiwsport.core.DrawServerException;
import com.aiwsport.core.DrawServerExceptionFactor;
import com.aiwsport.core.entity.*;
import com.aiwsport.core.mapper.*;
import com.aiwsport.core.model.ShowBranner;
import com.aiwsport.core.model.ShowDraws;
import com.aiwsport.core.utils.DataTypeUtils;
import com.aiwsport.core.utils.PayUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        draw.setIsSale("0");
        draw.setDrawWidth(drawWidth);
        draw.setDrawHigh(drawHigh);
        draw.setOwnFinishCount(0);
        draw.setCreateTime(time);
        draw.setModifyTime(time);
        return drawMapper.insert(draw) > 0;
    }

    public List<ShowBranner> getBranner() {
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

    public boolean uploadBranner(String clickUrl, int drawId, int type, int sort, String brannerUrl) {
        DrawBranner drawBranner = new DrawBranner();
        drawBranner.setBrannerUrl(brannerUrl);
        drawBranner.setClickUrl(clickUrl);
        drawBranner.setDrawId(drawId);
        drawBranner.setType(type+"");
        drawBranner.setSort(sort);
        return drawBrannerMapper.insert(drawBranner) > 0;
    }

    public boolean updateDraw(int drawId, int createPrice, int ownerPrice, int ownerCount, String isSale) {
        Draws draw = drawMapper.selectByPrimaryKey(drawId);
        if (draw == null || draw.getOwnFinishCount() > 0) {
            return false;
        }

        draw.setOwnPrice(ownerPrice);
        draw.setOwnCount(ownerCount);
        draw.setOwnFinishCount(0);
        draw.setIsSale(isSale);
        draw.setDrawPrice(createPrice);
        return drawMapper.updateByPrimaryKey(draw) > 0;
    }

    public Draws getDraw(int id) {
        Draws draws = drawMapper.selectByPrimaryKey(id);
        draws.setDrawExt(drawExtMapper.getMaxPriceByDrawId(id));
        return draws;
    }

    public ShowDraws getDraws(int sort, String maxId, int type) {
        int start = 0;
        int end = start + 14;
        int id;
        int page = 1;
        ShowDraws showDraws = new ShowDraws();
        if (StringUtils.isNotBlank(maxId)) {
            String[] maxs = maxId.split("-");
            id = Integer.parseInt(maxs[0]);
            start = (Integer.parseInt(maxs[1]) - 1) * 15;
            end = start + 14;
            page = Integer.parseInt(maxs[1]) + 1;
        } else {
            if (type == 1) {
                Draws draw = drawMapper.getMaxOne();
                if (draw == null) {
                    return showDraws;
                }
                id = draw.getId();
            } else {
                DrawExt drawExt = drawExtMapper.getMaxOne();
                if (drawExt == null) {
                    return showDraws;
                }
                id = drawExt.getId();
            }
        }

        if (type == 1) {
            List<Draws> draws = drawMapper.getIndex(id, start, end, sort);
            return buildShowDraws(draws, id + "-" + page);
        } else {
            List<DrawExt> drawExts = drawExtMapper.getIndex(id, start, end, sort);
            return buildShowDrawExts(drawExts, id + "-" + page);
        }
    }

    public ShowDraws getMyDraws(int uid, String maxId, int type) {
        int start = 0;
        int end = start + 14;
        int page = 1;
        if (StringUtils.isNotBlank(maxId)) {
            start = (Integer.parseInt(maxId) - 1) * 15;
            end = start + 14;
            page = Integer.parseInt(maxId) + 1;
        }

        if (type == 1) {
            List<Draws> draws = drawMapper.getMyList(uid, start, end);
            return buildShowDraws(draws, page + "");
        } else {
            List<DrawExt> drawExts = drawExtMapper.getMyList(uid, start, end);
            return buildShowDrawExts(drawExts, page + "");
        }
    }

    public boolean updateDrawExt(int drawExtId, int extPrice) {
        DrawExt drawExt = drawExtMapper.selectByPrimaryKey(drawExtId);
        if (drawExt == null) {
            return false;
        }
        drawExt.setExtPrice(extPrice);
        return drawExtMapper.updateByPrimaryKey(drawExt) > 0;
    }

    public String uploadIncome(String openId, int drawExtId, int incomePrice, int ownerPrize, String url) throws Exception {
        User user = userMapper.getByOpenId(openId);
        if (user == null || user.getId() == 0) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "user id is not exist");
        }

        DrawExt drawExt = drawExtMapper.selectByPrimaryKey(drawExtId);
        if (drawExt == null) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "draw ext id is not exist");
        }

        if (updateDrawExt(drawExtId, ownerPrize)) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "update draw ext owner price error");
        }

//        Map<String, Object> resMap = orderService.createWXOrder(openId, "127.0.0.1", "income-"+drawExtId,
//                BigDecimal.valueOf(incomePrice).divide(BigDecimal.valueOf(100)).toString());


        Map<String, Object> resMap = new HashMap<>();
        resMap.put("orderNo", PayUtil.getTradeNo());
        resMap.put("paySign", "dasdadasdadadadqwd2d22d2");

        if (resMap.isEmpty() || resMap.get("orderNo") == null) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "create order is fail");
        }

        String orderNo = (String) resMap.get("orderNo");
        String paySign = (String) resMap.get("paySign");

        Income income = new Income();
        income.setDrawExtid(drawExtId);
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

    private ShowDraws buildShowDrawExts(List<DrawExt> drawExts, String maxId) {
        if (drawExts == null || drawExts.size() == 0) {
            return new ShowDraws();
        }

        List<Draws> drawsList = drawExts.stream()
                .map(drawExt -> {
                    Draws draws = drawMapper.selectByPrimaryKey(drawExt.getDrawId());
                    draws.setDrawExt(drawExt);
                    return draws;
                }).collect(Collectors.toList());

        ShowDraws showDraws = new ShowDraws();
        showDraws.setDraws(drawsList);
        if (drawExts.size() < 15) {
            showDraws.setMaxId("-1");
        } else {
            showDraws.setMaxId(maxId);
        }
        return showDraws;
    }

    private ShowDraws buildShowDraws(List<Draws> draws, String maxId) {
        ShowDraws showDraws = new ShowDraws();
        if (draws == null || draws.size() == 0) {
            showDraws.setMaxId("-1");
            return showDraws;
        }

        draws.forEach(d -> d.setDrawExt(drawExtMapper.getMaxPriceByDrawId(d.getId())));

        showDraws.setDraws(draws);
        if (draws.size() < 15) {
            showDraws.setMaxId("-1");
        } else {
            showDraws.setMaxId(maxId);
        }
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
