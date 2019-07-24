package com.aiwsport.core.service;

import com.aiwsport.core.entity.DrawExt;
import com.aiwsport.core.entity.Draws;
import com.aiwsport.core.mapper.DrawExtMapper;
import com.aiwsport.core.mapper.DrawsMapper;
import com.aiwsport.core.model.ShowDrawExts;
import com.aiwsport.core.model.ShowDraws;
import com.aiwsport.core.utils.DataTypeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrawService {

    @Autowired
    private DrawsMapper drawMapper;

    @Autowired
    private DrawExtMapper drawExtMapper;


    private static Logger logger = LogManager.getLogger();

    public boolean createDraw(int uid, String name, String telNo, String drawName,
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
        draw.setOwnFinishCount(0);
        draw.setCreateTime(time);
        draw.setModifyTime(time);

        return drawMapper.insert(draw) > 0;
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
        showDrawExts.setMaxId(id + "-" + page);
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
        showDraws.setMaxId(page+"");
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


}
