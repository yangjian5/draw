package com.aiwsport.core.service;

import com.aiwsport.core.DrawServerException;
import com.aiwsport.core.DrawServerExceptionFactor;
import com.aiwsport.core.entity.*;
import com.aiwsport.core.mapper.*;
import com.aiwsport.core.model.ShowBranner;
import com.aiwsport.core.model.ShowDraws;
import com.aiwsport.core.utils.DataTypeUtils;
import com.aiwsport.core.utils.QRCodeGenerator;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    public Draws createDraw(int uid, String name, String telNo, String drawName,
                              String author, String desc, String urlHd, String urlSimple, int drawWidth, int drawHigh, String qrUrl) throws Exception {
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
        drawMapper.insert(draw);
        int id = draw.getId();
        QRCodeGenerator.generateQRCodeImage("https://art.artchains.cn/data1/qrcode/id="+id, id+"_draw");
        draw.setQrUrl(qrUrl + id +"_draw.png");
        drawMapper.updateByPrimaryKey(draw);
        return draw;
    }

    public List<ShowBranner> getBranner() {
        List<DrawBranner> drawBranners = drawBrannerMapper.selectAll();
        List<ShowBranner> showBranners = new ArrayList<>();
        drawBranners.forEach(drawBranner -> {
            ShowBranner showBranner = new ShowBranner();
            showBranner.setBranner(drawBranner);
            if ("2".equals(drawBranner.getType())) {
                Draws draws = drawMapper.selectByPrimaryKey(drawBranner.getDrawId());
                if (draws != null) {
                    draws.setDrawExt(drawExtMapper.getMaxPriceByDrawId(draws.getId()));
                    showBranner.setDraws(draws);
                }
            }
            showBranners.add(showBranner);
        });

        return showBranners;
    }

    public boolean delDraw(int id, int type) {
        if (type == 1) {
            if (drawExtMapper.deleteDrawExt(id) > -1) {
                return drawMapper.deleteByPrimaryKey(id) > 0;
            }
            return false;
        } else {
            DrawExt drawExt = drawExtMapper.selectByPrimaryKey(id);
            if (drawExt == null) {
                return false;
            }
            Draws draws = drawMapper.selectByPrimaryKey(drawExt.getDrawId());
            draws.setOwnFinishCount(draws.getOwnFinishCount()-1);
            draws.setOwnCount(draws.getOwnCount()-1);
            drawMapper.updateByPrimaryKey(draws);
            return drawExtMapper.deleteByPrimaryKey(id) > 0;
        }
    }

    public int uploadBranner(Integer id, String clickUrl, int drawId, int type, int sort, String brannerUrl, String simpleUrl) {
        if (id == null || id == 0) {
            DrawBranner drawBranner = new DrawBranner();
            drawBranner.setBrannerUrl(brannerUrl);
            drawBranner.setClickUrl(clickUrl);
            drawBranner.setDrawId(drawId);
            drawBranner.setType(type+"");
            drawBranner.setSort(sort);
            drawBranner.setSimpleUrl(simpleUrl);
            drawBrannerMapper.insert(drawBranner);
            return drawBranner.getId();
        } else {
            DrawBranner drawBranner = drawBrannerMapper.selectByPrimaryKey(id);
            if (drawBranner == null) {
                return 0;
            }
            if (StringUtils.isNotBlank(brannerUrl)) {
                drawBranner.setBrannerUrl(brannerUrl);
                drawBranner.setSimpleUrl(simpleUrl);
            }
            drawBranner.setClickUrl(clickUrl);
            drawBranner.setDrawId(drawId);
            drawBranner.setType(type+"");
            drawBranner.setSort(sort);
            drawBrannerMapper.updateByPrimaryKey(drawBranner);
            return drawBranner.getId();
        }
    }

    public boolean updateDraw(int drawId, int createPrice, int ownerPrice, int ownerCount, String isSale, String isUpdate) {
        Draws draw = drawMapper.selectByPrimaryKey(drawId);
        if (draw == null) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "藏品不存在");
        }

        if ("1".equals(isUpdate)) {
            draw.setOwnCount(ownerCount);
            draw.setOwnPrice(ownerPrice);
        }

        draw.setIsSale(isSale);
        draw.setDrawPrice(createPrice);
        if (drawMapper.updateByPrimaryKey(draw) > -1) {
            if ("1".equals(isUpdate)) {
                DrawExt drawExtOld = drawExtMapper.getMaxPriceByDrawIda(drawId);

                drawExtMapper.deleteDrawExt(drawId);
                for (int i=0; i<ownerCount; i++) {
                    DrawExt drawExt = new DrawExt();
                    drawExt.setExtPrice(ownerPrice);
                    drawExt.setExtUid(draw.getProdUid());
                    drawExt.setExtIsSale("1");

                    if (drawExtOld == null || drawExtOld.getExtStatus().equals("0")) {
                        drawExt.setExtStatus("0");
                    } else {
                        drawExt.setExtStatus("1");
                    }

                    drawExt.setDrawId(drawId);
                    drawExtMapper.insert(drawExt);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public Draws getDraw(int id) {
        Draws draws = drawMapper.selectByPrimaryKey(id);
        draws.setOwnFinishCount(drawExtMapper.getIsSaleCount(id));
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
            return buildShowDraws(draws, id + "-" + page, "");
        } else {
            List<DrawExt> drawExts = drawExtMapper.getIndex(id, start, end, sort);
            return buildShowDrawExts(drawExts, id + "-" + page, 0);
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
            draws.forEach(d -> {
                int count = drawExtMapper.getCount(d.getId(), uid);
                if (d.getOwnFinishCount() == 0 && d.getOwnCount() == count) {
                    d.setIsUpdateCount(1);
                } else {
                    d.setIsUpdateCount(0);
                }
            });

            return buildShowDraws(draws, page + "", "my");
        } else {
            List<DrawExt> drawExts = drawExtMapper.getMyList(uid, 0, 2000);

            List<DrawExt> drawExtList = new ArrayList<>();
            drawExts.stream()
                    .collect(Collectors.groupingBy(DrawExt::getDrawId, Collectors.toList()))
                    .entrySet().stream()
                    .map(entry-> {
                        List<DrawExt> drawExts1 = entry.getValue().stream()
//                                .sorted(Comparator.comparingInt(DrawExt::getExtPrice))
                                .limit(1).collect(Collectors.toList());
                        drawExtList.add(drawExts1.get(0));
                        return null;
                    }).collect(Collectors.toList());

            drawExtList.forEach(drawExt -> {
                drawExt.setCount(drawExtMapper.getCount(drawExt.getDrawId(), uid));
            });

            return buildShowDrawExts(drawExtList.subList(start, end+1>drawExtList.size()?drawExtList.size():end+1), page + "", uid);
        }
    }

    public boolean updateDrawExt(int drawExtId, int extPrice, String isSale) {
        DrawExt drawExt = drawExtMapper.selectByPrimaryKey(drawExtId);
        if (drawExt == null) {
            return false;
        }
        drawExt.setExtPrice(extPrice);
        drawExt.setExtIsSale(isSale);
        return drawExtMapper.updateByPrimaryKey(drawExt) > -1;
    }

    public Map<String, String> uploadIncome(String openId, int drawExtId, int incomePrice, int ownerPrize, String isSale, String url) throws Exception {
        User user = userMapper.getByOpenId(openId);
        if (user == null || user.getId() == 0) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "user id is not exist");
        }

        DrawExt drawExt = drawExtMapper.selectByPrimaryKey(drawExtId);
        if (drawExt == null) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "draw ext id is not exist");
        }

        if (!updateDrawExt(drawExtId, ownerPrize, isSale)) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "update draw ext owner price error");
        }

        incomePrice = BigDecimal.valueOf(incomePrice).multiply(BigDecimal.valueOf(0.05)).setScale(0, BigDecimal.ROUND_DOWN).intValue();

        Map<String, String> resMap = orderService.createWXOrder(openId, "127.0.0.1", "income-"+drawExtId,
                incomePrice+"");


//        Map<String, Object> resMap = new HashMap<>();
//        resMap.put("orderNo", PayUtil.getTradeNo());
//        resMap.put("paySign", "dasdadasdadadadqwd2d22d2");

        if (resMap.isEmpty() || resMap.get("orderNo") == null) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "create order is fail");
        }

        String orderNo = resMap.get("orderNo");
        Income income = new Income();
        income.setDrawExtid(drawExtId);
        income.setProofPrice(incomePrice);
        income.setProofUrl(url);
        income.setStatus("0");
        income.setInfo(JSON.toJSONString(resMap));
        income.setOrderNo(orderNo);
        String time = DataTypeUtils.formatCurDateTime();
        income.setCreateTime(time);
        income.setModifyTime(time);
        incomeMapper.insert(income);
        return resMap;
    }

    private ShowDraws buildShowDrawExts(List<DrawExt> drawExts, String maxId, int uid) {
        if (drawExts == null || drawExts.size() == 0) {
            return new ShowDraws();
        }

        List<Draws> drawsList = drawExts.stream()
                .map(drawExt -> {
                    Draws draws = drawMapper.selectByPrimaryKey(drawExt.getDrawId());
                    if (uid != 0) {
                        int count = drawExtMapper.getCount(draws.getId(), uid);
                        if (draws.getOwnFinishCount() == 0 && draws.getOwnCount() == count) {
                            draws.setIsUpdateCount(1);
                        } else {
                            draws.setIsUpdateCount(0);
                        }
                    }

                    draws.setDrawExt(drawExt);
                    draws.setOwnFinishCount(drawExtMapper.getIsSaleCount(drawExt.getDrawId()));
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

    private ShowDraws buildShowDraws(List<Draws> draws, String maxId, String type) {
        ShowDraws showDraws = new ShowDraws();
        if (draws == null || draws.size() == 0) {
            showDraws.setMaxId("-1");
            return showDraws;
        }

        draws.forEach(d -> {
            if ("my".equals(type)) {
                d.setDrawExt(drawExtMapper.getMaxPriceByDrawIda(d.getId()));
            } else {
                d.setDrawExt(drawExtMapper.getMaxPriceByDrawId(d.getId()));
                d.setOwnFinishCount(drawExtMapper.getIsSaleCount(d.getId()));
            }
        });

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
