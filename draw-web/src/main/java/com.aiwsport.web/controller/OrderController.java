package com.aiwsport.web.controller;

import com.aiwsport.core.DrawServerException;
import com.aiwsport.core.DrawServerExceptionFactor;
import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.constant.WxConfig;
import com.aiwsport.core.service.OrderService;
import com.aiwsport.core.utils.PayUtil;
import com.aiwsport.core.utils.XmlUtil;
import com.aiwsport.web.utlis.ParseUrl;
import com.aiwsport.web.verify.ParamVerify;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * 用户操作
 *
 * @author yangjian
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    private static Logger logger = LogManager.getLogger();

    @RequestMapping(value = "/buy.json")
    public ResultMsg login(@ParamVerify(isNumber = true)int id,
                           @ParamVerify(isNumber = true)int type,
                           @ParamVerify(isNotBlank = true)String open_id,
                           @ParamVerify(isNotBlank = true)String tel,
                           @ParamVerify(isNotBlank = true)String name,
                           HttpServletRequest request) {

        Map<String, Object> res = orderService.createOrder(id, type, open_id, tel, name, ParseUrl.getLocalIp(request));
        if (res == null) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "buy is fail");
        }

        return new ResultMsg("buy", res);
    }


    @RequestMapping("/wx_notify")
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine())!=null){
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        System.out.println("接收到的报文：" + notityXml);

        Map<String, String> resMap = XmlUtil.xmlParse(notityXml);

        String returnCode = resMap.get("return_code");
        if("SUCCESS".equals(returnCode)){
            //验证签名是否正确
            String reSign = PayUtil.getSign(resMap, resMap.get("sign"));
            String realSign = PayUtil.getSign(resMap, WxConfig.SECRET);
            if(reSign.equals(realSign)){
                /*此处添加自己的业务逻辑代码start*/
                orderService.finishPay(resMap.get("orderNo"));
                /*此处添加自己的业务逻辑代码end */

                //通知微信服务器已经支付成功
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            }
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        System.out.println(resXml);
        System.out.println("微信支付回调数据结束");

        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

}
