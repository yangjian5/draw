package com.aiwsport.web.controller;

import com.aiwsport.core.DrawServerException;
import com.aiwsport.core.DrawServerExceptionFactor;
import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.service.OrderService;
import com.aiwsport.core.utils.XmlUtil;
import com.aiwsport.web.utlis.ParseUrl;
import com.aiwsport.web.verify.ParamVerify;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * 订单
 *
 * @author yangjian
 */
@RestController
@RequestMapping(value = "/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private static Logger logger = LogManager.getLogger();

    @RequestMapping(value = "/buy.json")
    public ResultMsg buy(@ParamVerify(isNumber = true)int id,
                           @ParamVerify(isNumber = true)int type,
                           @ParamVerify(isNotBlank = true)String open_id,
                           HttpServletRequest request) {
        Map<String, String> res = null;
        try {
            res = orderService.createOrder(id, type, open_id, ParseUrl.getLocalIp(request));
        } catch (Exception e) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, e.getMessage());
        }

        if (res == null) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "buy is fail");
        }

        return new ResultMsg("buy", res);
    }

    @RequestMapping(value = "/my_order.json")
    public ResultMsg myOrder(@ParamVerify(isNotBlank = true)String open_id, @ParamVerify(isNotBlank = true)String status) {
        return new ResultMsg("buy", orderService.myOrder(open_id, status));
    }

    @RequestMapping(value = "/order_check.json")
    public ResultMsg myOrder(@ParamVerify(isNumber = true)int id,
                             @ParamVerify(isNumber = true)int type) {
        boolean res = orderService.buyCheck(id, type);
        if (!res) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "已经被他人抢先购买");
        }

        return new ResultMsg("order_check", true);
    }

    @RequestMapping("/add_web_order_log.json")
    public ResultMsg addWebOrderLog(@ParamVerify(isNumber = true)int uid,
                                    @ParamVerify(isNotBlank = true)String order_no,
                                    @ParamVerify(isNotBlank = true)String type) throws Exception{
        boolean res = orderService.addWebOderLog(order_no, uid, type);
        if (!res) {
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "add_web_order_log is fail");
        }
        return new ResultMsg("add_web_order_log", true);
    }


    @RequestMapping("/wx_notify.json")
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
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
            orderService.finishPay(resMap.get("out_trade_no"));

            //验证签名是否正确
//            String reSign = PayUtil.getSign(resMap, resMap.get("sign"));
//            String realSign = PayUtil.getSign(resMap, WxConfig.SECRET);
//            if(reSign.equals(realSign)){
                /*此处添加自己的业务逻辑代码start*/
//                orderService.finishPay(resMap.get("orderNo"));
                /*此处添加自己的业务逻辑代码end */

                //通知微信服务器已经支付成功
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
//            }
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
