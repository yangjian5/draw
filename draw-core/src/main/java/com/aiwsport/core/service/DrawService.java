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

@Service
public class DrawService {

    @Autowired
    private DrawsMapper drawMapper;

    @Autowired
    private DrawExtMapper drawExtMapper;

    @Autowired
    private IncomeMapper incomeMapper;

    @Autowired
    private IncomeStatisticsMapper incomeStatisticsMapper;

    @Autowired
    private DrawBrannerMapper drawBrannerMapper;

    private static Logger logger = LogManager.getLogger();

    public boolean createDraw(int uid, String name, String telNo, String drawName, String simpleName,
                              String author, String desc, String urlHd, int drawWidth, int drawHigh) throws Exception {
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
        draw.setUrlSimple("");
        draw.setDrawPrice(0);
        draw.setOwnCount(0);
        draw.setUrlSimple(simpleName);
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

    public ShowDraws getDraws(int sort, String maxId){
        int start = 0;
        int end = start + 1;
        int id;
        int page = 1;
        if (StringUtils.isNotBlank(maxId)) {
            String[] maxs = maxId.split("-");
            id = Integer.parseInt(maxs[0]);
            start = (Integer.parseInt(maxs[1]) -1 ) * 15;
            end = start + 1;
            page = Integer.parseInt(maxs[1]) + 1;
        } else {
            Draws draw = drawMapper.getMaxOne();
            if (draw == null) {
                return new ShowDraws();
            }
            id = draw.getId();
        }

        List<Draws> draws = drawMapper.getIndex(id, start, end, sort);
        ShowDraws showDraws = buildShowDraws(draws);
        showDraws.setMaxId(id + "-" + page);
        return showDraws;
    }

    public ShowDrawExts getDrawExts(int sort, String maxId){
        int start = 0;
        int end = start + 1;
        int id;
        int page = 1;
        if (StringUtils.isNotBlank(maxId)) {
            String[] maxs = maxId.split("-");
            id = Integer.parseInt(maxs[0]);
            start = (Integer.parseInt(maxs[1]) -1 ) * 15;
            end = start + 1;
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
        showDrawExts.setMaxId(id + "-" + page);
        return showDrawExts;
    }

    public ShowDraws getMyDraws(int uid, String maxId){
        int start = 0;
        int end = start + 1;
        int page = 1;
        if (StringUtils.isNotBlank(maxId)) {
            start = (Integer.parseInt(maxId) -1 ) * 15;
            end = start + 2;
            page = Integer.parseInt(maxId) + 1;
        }

        List<Draws> draws = drawMapper.getMyList(uid, start, end);
        ShowDraws showDraws = buildShowDraws(draws);
        showDraws.setMaxId(page+"");
        return showDraws;
    }

    public ShowDrawExts getMyDrawExts(int uid, String maxId){
        int start = 0;
        int end = start + 1;
        int page = 1;
        if (StringUtils.isNotBlank(maxId)) {
            start = (Integer.parseInt(maxId) -1 ) * 15;
            end = start + 2;
            page = Integer.parseInt(maxId) + 1;
        }

        List<DrawExt> drawExts = drawExtMapper.getMyList(uid, start, end);
        ShowDrawExts showDrawExts = buildShowDrawExts(drawExts);
        showDrawExts.setMaxId(page+"");
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

    public boolean uploadIncome(int drawExtId, int incomePrice, String url) throws Exception {
        DrawExt drawExt = drawExtMapper.selectByPrimaryKey(drawExtId);
        if (drawExt == null) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "draw ext id is not exist");
        }

        Income income = new Income();
        income.setDrawExtId(drawExtId);
        income.setProofPrice(incomePrice);
        income.setProofUrl(url);
        income.setStatus("0");
        String time = DataTypeUtils.formatCurDateTime();
        income.setCreateTime(time);
        income.setModifyTime(time);
        incomeMapper.insert(income);

        IncomeStatistics incomeStatistics = incomeStatisticsMapper.getTodayIncome(drawExt.getDrawId());
        if (incomeStatistics == null) {

        } else {
            BigDecimal income1 = BigDecimal.valueOf(incomePrice).multiply(BigDecimal.valueOf(0.05));
            BigDecimal sumIncome = BigDecimal.valueOf(incomeStatistics.getIncomePrice()).add(income1);
            incomeStatistics.setIncomePrice(sumIncome.intValue());
            incomeStatisticsMapper.updateByPrimaryKey(incomeStatistics);
        }

        // TODO 生成支付订单， 返回订单信息


        return true;
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
