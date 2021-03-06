package com.aiwsport.web.controller;

import com.aiwsport.core.DrawServerException;
import com.aiwsport.core.DrawServerExceptionFactor;
import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.entity.Draws;
import com.aiwsport.core.entity.User;
import com.aiwsport.core.service.DrawService;
import com.aiwsport.core.service.UserService;
import com.aiwsport.web.utlis.FileUtil;
import com.aiwsport.web.verify.ParamVerify;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

/**
 * 首页展示
 *
 * @author yangjian
 */
@RestController
@RequestMapping(value = "/api")
public class DrawController {

    @Autowired
    private DrawService drawService;

    @Autowired
    private UserService userService;

    private static final String IMG_HOST = "https://art.artchains.cn";

    private static final String BASE = "/home/www-data";

    private static final String PATH = "/data1/draw";

    private static final String BRANNER = "/data1/branner";

    private static final String BRANNER_DESC = "/data1/brannerdesc";

    private static final String BRANNER_DESC_SIMPLE = "/data1/brannerdesc_simple";

    private static final String SIMPLE_BRANNER = "/data1/branner_simple";

    private static final String SIMPLE_PATH = "/data1/draw_simple";

    private static final String QR_PATH = "/data1/qrcode/";

    private static final String INCOME_PATH = "/data1/income";

    @RequestMapping(value = "/index.json")
    public ResultMsg index(@ParamVerify(isNumber = true) int type,
                           @ParamVerify(isNumber = true) int sort,
                           String max_id) {
        if ("0".equals(max_id)) {
            max_id = "";
        }
        return new ResultMsg("index", drawService.getDraws(sort, max_id, type));
    }

    @RequestMapping(value = "/draw/get.json")
    public ResultMsg getDesc(@ParamVerify(isNumber = true) int drawId) {

        return new ResultMsg("drawGet", drawService.getDraw(drawId));
    }


    @RequestMapping(value = "/branner.json")
    public ResultMsg branner() {
        return new ResultMsg("branner", drawService.getBranner());
    }

    @RequestMapping(value = "/backend/delete_draw.json")
    public ResultMsg deleteDraw(@ParamVerify(isNumber = true) Integer id,
                                @ParamVerify(isNumber = true) Integer type) {
        return new ResultMsg("delete_draw", drawService.delDraw(id, type));
    }


    @RequestMapping(value = "/backend/upload_branner.json")
    public ResultMsg uploadImage(@ParamVerify(isNumber = true) @RequestParam(name = "id", required = false) Integer id,
                                 String click_url,
                                 @RequestParam(name = "branner_url", required = false, defaultValue = "") String brannerUrl,
                                 @RequestParam(name = "simple_url", required = false, defaultValue = "") String simpleUrl,
                                 @RequestParam(name = "draw_id", required = false, defaultValue = "0") Integer draw_id,
                                 @ParamVerify(isNumber = true) int type,
                                 @ParamVerify(isNumber = true) int sort,
                                 @RequestParam(name = "branner_file", required = false) MultipartFile file,
                                 @RequestParam(name = "desc_file", required = false) MultipartFile descFile) {
        int brannerId = 0;
        try {
            if (file != null) {
                String fileName = file.getOriginalFilename();
                fileName = getEname(fileName);
                InputStream inputStream = file.getInputStream();
                String name = System.currentTimeMillis() + "_" + fileName;
                if (!FileUtil.writeFile(BASE + BRANNER, BASE + SIMPLE_BRANNER, name, inputStream)) {
                    throw new DrawServerException(DrawServerExceptionFactor.FILE_ERROR);
                }

                brannerUrl = IMG_HOST + BRANNER + "/" + name;
                simpleUrl = IMG_HOST + SIMPLE_BRANNER + "/" + name;
                brannerId = drawService.uploadBranner(id, click_url, draw_id, type, sort, brannerUrl, simpleUrl);
                if (brannerId == 0) {
                    throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "upload_branner insert is fail ");
                }
            }

            if (descFile != null) {
                String descFileName = descFile.getOriginalFilename();
                descFileName = getEname(descFileName);
                InputStream descInputStream = descFile.getInputStream();
                String descName = System.currentTimeMillis() + "_" + descFileName;
                if (!FileUtil.writeFile(BASE + BRANNER_DESC, BASE + BRANNER_DESC_SIMPLE, descName, descInputStream)) {
                    throw new DrawServerException(DrawServerExceptionFactor.FILE_ERROR);
                }
                brannerUrl = IMG_HOST + BRANNER_DESC + "/" + descName;
                simpleUrl = IMG_HOST + BRANNER_DESC_SIMPLE + "/" + descName;
                brannerId = drawService.uploadBranner(brannerId, simpleUrl, draw_id, type, sort, null, null);
                if (brannerId == 0) {
                    throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "upload_branner insert is fail ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "upload_branner other is fail ");
        }

        return new ResultMsg("upload_branner", true);
    }


    @RequestMapping(value = "/my_draw.json")
    public ResultMsg myDraw(@ParamVerify(isNumber = true) int type,
                            @ParamVerify(isNotBlank = true) String open_id,
                            String max_id) {
        User user = userService.getUser(open_id);
        if (user == null) {
            throw new DrawServerException(DrawServerExceptionFactor.PARAM_VERIFY_FAIL, "user is not exist");
        }

        return new ResultMsg("my_draw", drawService.getMyDraws(user.getId(), max_id, type));
    }

    @RequestMapping(value = "/upload_image.json")
    public ResultMsg uploadImage(@ParamVerify(isNotBlank = true) String open_id,
                                 @ParamVerify(isNotBlank = true) String name,
                                 @ParamVerify(isNumber = true, isNotBlank = true) String tel_no,
                                 @ParamVerify(isNotBlank = true) String draw_name,
                                 @ParamVerify(isNotBlank = true) String author,
                                 @ParamVerify(isNotBlank = true) String desc,
                                 @ParamVerify(isNumber = true) int draw_width,
                                 @ParamVerify(isNumber = true) int draw_high,
                                 @ParamVerify(isNumber = true) int create_price,
                                 @ParamVerify(isNumber = true) int owner_prize,
                                 @ParamVerify(isNumber = true) int owner_count,
                                 @ParamVerify(isNotBlank = true) String is_sale,
                                 @RequestParam("draw_file") MultipartFile file) {

        try {
            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            String imgName = System.currentTimeMillis() + "_" + fileName;
            System.out.println("imgName " + imgName);
            if (!FileUtil.writeFile(BASE + PATH, BASE + SIMPLE_PATH, imgName, inputStream)) {
                throw new DrawServerException(DrawServerExceptionFactor.FILE_ERROR);
            }

            User user = userService.getUser(open_id);
            if (user == null) {
                throw new DrawServerException(DrawServerExceptionFactor.PARAM_VERIFY_FAIL, "open_id is not exist");
            }

            Draws draws = drawService.createDraw(user.getId(), name, tel_no, draw_name, author, desc, IMG_HOST + SIMPLE_PATH + "/" + imgName,
                    IMG_HOST + PATH + "/" + imgName, draw_width, draw_high, IMG_HOST + QR_PATH);

            boolean res = drawService.updateDraw(draws.getId(), create_price, owner_prize, owner_count, is_sale, "1");
            if (!res) {
                throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "create draw is error");
            }
        } catch (Exception e) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, e.getMessage());
        }

        return new ResultMsg("upload_image", true);
    }

    @RequestMapping(value = "/update_draw.json")
    public ResultMsg updateDraw(@ParamVerify(isNumber = true) int draw_id,
                                @RequestParam(name = "create_price", required = false, defaultValue = "0") Integer create_price,
                                @RequestParam(name = "owner_prize", required = false, defaultValue = "0") Integer owner_prize,
                                @RequestParam(name = "owner_count", required = false, defaultValue = "0") Integer owner_count,
                                @ParamVerify(isNotBlank = true) String is_sale,
                                @ParamVerify(isNotBlank = true)String open_id,
                                @ParamVerify(isNotBlank = true)String is_update_count) {
        if ("1".equals(is_sale)) {
            if (create_price == 0 || owner_prize == 0 || owner_count == 0) {
                throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "update draw param is error");
            }
        }

        boolean res = drawService.updateDraw(draw_id, create_price, owner_prize, owner_count, is_sale, is_update_count);
        if (!res) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "update draw is error");
        }

        return new ResultMsg("update_draw", true);
    }

    @RequestMapping(value = "/update_owner_draw.json")
    public ResultMsg updateOwnerDraw(@ParamVerify(isNotBlank = true) String open_id,
                                     @ParamVerify(isNumber = true) int draw_ext_id,
                                     @ParamVerify(isNumber = true) @RequestParam(name = "owner_prize", required = false, defaultValue = "0") int owner_prize,
                                     @ParamVerify(isNumber = true) @RequestParam(name = "income_prize", required = false, defaultValue = "0") int income_prize,
                                     @ParamVerify(isNotBlank = true) String is_sale,
                                     @RequestParam(name = "income_file", required = false) MultipartFile file) {
        if (income_prize > 0) { // 上传收益
            try {
                String imgName = "";
                if (file != null) {
                    String fileName = file.getOriginalFilename();
                    InputStream inputStream = file.getInputStream();
                    imgName = System.currentTimeMillis() + "_" + fileName;
                    if (!FileUtil.writeFile(BASE + INCOME_PATH, "", imgName, inputStream)) {
                        throw new DrawServerException(DrawServerExceptionFactor.FILE_ERROR);
                    }
                }
                Map<String, String> resMap = drawService.uploadIncome(open_id, draw_ext_id, income_prize, owner_prize, is_sale, IMG_HOST + INCOME_PATH + "/" + imgName);
                return new ResultMsg("update_owner_draw-uploadIncome", resMap);
            } catch (Exception e) {
                throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, e.getMessage());
            }
        } else { // 修改所有权价格
            if (!drawService.updateDrawExt(draw_ext_id, owner_prize, is_sale)) {
                throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "update_owner_draw is error");
            }
        }
        return new ResultMsg("update_owner_draw", true);
    }

    private String getEname(String name) {
        String res = "";
        try {
            HanyuPinyinOutputFormat pyFormat = new HanyuPinyinOutputFormat();
            pyFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            pyFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            pyFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
            res = PinyinHelper.toHanyuPinyinString(name, pyFormat, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
