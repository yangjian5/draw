package com.aiwsport.web.controller;

import com.aiwsport.core.DrawServerException;
import com.aiwsport.core.DrawServerExceptionFactor;
import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.entity.User;
import com.aiwsport.core.service.DrawService;
import com.aiwsport.core.service.UserService;
import com.aiwsport.web.utlis.FileUtil;
import com.aiwsport.web.verify.ParamVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 首页展示
 *
 * @author yangjian
 */
@RestController
public class DrawController {

    @Autowired
    private DrawService drawService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/index.json")
    public ResultMsg index(@ParamVerify(isNumber = true)int type,
                           @ParamVerify(isNumber = true)int sort,
                           String max_id) {
        if (type == 1) {
            return new ResultMsg("index", drawService.getDraws(sort, max_id));
        } else {
            return new ResultMsg("index", drawService.getDrawExts(sort, max_id));
        }
    }

    @RequestMapping(value = "/my_draw.json")
    public ResultMsg myDraw(@ParamVerify(isNumber = true)int type,
                            @ParamVerify(isNotBlank = true)String open_id,
                            String max_id) {
        User user = userService.getUser(open_id);
        if (user == null) {
            throw new DrawServerException(DrawServerExceptionFactor.PARAM_VERIFY_FAIL, "user is not exist");
        }

        if (type == 1) {
            return new ResultMsg("index", drawService.getMyDraws(user.getId(), max_id));
        } else {
            return new ResultMsg("index", drawService.getMyDrawExts(user.getId(), max_id));
        }
    }


    @RequestMapping(value = "/upload_image.json")
    public ResultMsg uploadImage(@ParamVerify(isNotBlank = true)String open_id,
                                 @ParamVerify(isNotBlank = true)String name,
                                 @ParamVerify(isNumber = true, isNotBlank=true)String tel_no,
                                 @ParamVerify(isNotBlank = true)String draw_name,
                                 @ParamVerify(isNotBlank = true)String author,
                                 @ParamVerify(isNotBlank = true)String desc,
                                 @ParamVerify(isNumber = true)int draw_width,
                                 @ParamVerify(isNumber = true)int draw_high,
                                 @RequestParam("draw_file") MultipartFile file) {
        try{
            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            String imgName = System.currentTimeMillis()+"_"+fileName;
            System.out.println("imgName "+imgName);
            if(!FileUtil.writeFile("/data1/draw", imgName, inputStream)){
                throw new DrawServerException(DrawServerExceptionFactor.FILE_ERROR);
            }

            User user = userService.getUser(open_id);
            if (user == null) {
                throw new DrawServerException(DrawServerExceptionFactor.PARAM_VERIFY_FAIL, "open_id is not exist");
            }

            boolean res = drawService.createDraw(user.getId(), name, tel_no, draw_name, author, desc, imgName, draw_width, draw_high);
            if (!res) {
                throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "create draw is error");
            }
        } catch (Exception e) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, e.getMessage());
        }

        return new ResultMsg("upload_image", true);
    }

    @RequestMapping(value = "/update_draw.json")
    public ResultMsg updateDraw(@ParamVerify(isNumber = true)int draw_id,
                                @ParamVerify(isNumber = true)int create_price,
                                @ParamVerify(isNotBlank = true)int owner_count){
        boolean res = drawService.updateDraw(draw_id, create_price, owner_count);
        if (!res) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "update draw is error");
        }

        return new ResultMsg("update_draw", true);
    }

    @RequestMapping(value = "/update_owner_draw.json")
    public ResultMsg updateOwnerDraw(@ParamVerify(isNumber = true)int draw_ext_id,
                                     @ParamVerify(isNumber = true)int ext_price,
                                     @ParamVerify(isNumber = true)int income_price,
                                     @RequestParam("income_file") MultipartFile file){
        boolean res = false;
        if (income_price == 0 || file == null) { // 上传收益
            try {
                String fileName = file.getOriginalFilename();
                InputStream inputStream = file.getInputStream();
                String imgName = System.currentTimeMillis()+"_"+fileName;
                if(!FileUtil.writeFile("/data1/income", imgName, inputStream)){
                    throw new DrawServerException(DrawServerExceptionFactor.FILE_ERROR);
                }


            } catch (Exception e) {
                throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, e.getMessage());
            }
        } else { // 修改所有权价格
            res = drawService.updateDrawExt(draw_ext_id, ext_price);
        }
        return new ResultMsg("update_owner_draw", res);
    }


}
