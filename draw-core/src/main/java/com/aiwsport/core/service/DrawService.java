package com.aiwsport.core.service;

import com.aiwsport.core.entity.Draws;
import com.aiwsport.core.mapper.DrawsMapper;
import com.aiwsport.core.utils.DataTypeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrawService {

    @Autowired
    private DrawsMapper drawMapper;

    private static Logger logger = LogManager.getLogger();

    public boolean createDraw(int uid, String name, String telNo, String drawName, String author, String desc, String urlHd) throws Exception {
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
        draw.setCreateTime(time);
        draw.setModifyTime(time);
        return drawMapper.insert(draw) > 0;
    }

    public boolean updateDraw(int drawId, int createPrice, int ownerCount){
        Draws draw = new Draws();
        draw.setId(drawId);
        draw.setOwnCount(ownerCount);
        draw.setOwnFinishCount(0);
        draw.setDrawPrice(createPrice);
        return drawMapper.updateByPrimaryKey(draw) > 0;
    }

    public List<Draws> getDraws(int sort, String maxId){
        String[] maxs = maxId.split("-");
        return drawMapper.getIndex(Integer.parseInt(maxs[0]), Integer.parseInt(maxs[1]), sort);
    }

}
