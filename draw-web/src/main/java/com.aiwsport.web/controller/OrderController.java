package com.aiwsport.web.controller;

import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.service.UserService;
import com.aiwsport.web.utlis.HttpUtils;
import com.aiwsport.web.utlis.PayUtil;
import com.aiwsport.web.utlis.WxPayConfig;
import com.aiwsport.web.utlis.XmlUtil;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 用户操作
 *
 * @author yangjian
 */
@RestController
public class OrderController {

    @Autowired
    private UserService userService;
    private static Logger logger = LogManager.getLogger();

    @RequestMapping(value = "/buy.json")
    public ResultMsg login(@ParamVerify(isNumber = true)int id,
                           @ParamVerify(isNumber = true)int type,
                           @ParamVerify(isNotBlank = true)String open_id,
                           HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            //生成的随机字符串
            String nonce_str = PayUtil.getNonceStr();
            //商品名称
            String body = " ";
            //获取本机的IP地址
            String spbill_create_ip = PayUtil.getRemoteAddrIp(request);

            String orderNo = PayUtil.getTradeNo();
            String money = "1"; //支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败

            Map<String, String> packageParams = new HashMap<>();
            packageParams.put("appid", WxPayConfig.appid);
            packageParams.put("mch_id", WxPayConfig.mch_id);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("body", body);
            packageParams.put("out_trade_no", orderNo); //商户订单号
            packageParams.put("total_fee", money); //支付金额，这边需要转成字符串类型，否则后面的签名会失败
            packageParams.put("spbill_create_ip", spbill_create_ip);
            packageParams.put("notify_url", WxPayConfig.notify_url);
            packageParams.put("trade_type", WxPayConfig.TRADETYPE);
            packageParams.put("openid", open_id);
            //把数组所有元素，按照"参数=参数值"的模式用"＆"字符拼接成字符串
            // MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
            String mysign = PayUtil.getSign(packageParams, WxPayConfig.key);
            packageParams.put("sign", mysign);

            logger.info("=======================第一次签名："+ mysign +"============ ======");

            //拼接统一下单接口使用的XML数据，要将上一步生成的签名一起拼接进去
            String xml = XmlUtil.xmlFormat(packageParams, false);

            System.out.println("调试模式_统一下单接口请求XML数据："+ xml);

            //调用统一下单接口，并接受返回的结果
            String restxml = HttpUtils.posts(WxPayConfig.pay_url, xml);

            System.out.println("调试模式_统一下单接口返回XML数据："+restxml);

            //将解析结果存储在HashMap中
            Map<String, String> restmap = XmlUtil.xmlParse(restxml);

            String return_code = restmap.get("return_code"); //返回状态码

            //返回给移动端需要的参数
            if (return_code =="SUCCESS"|| return_code.equals(return_code)) {
                response.put("appid", WxPayConfig.appid);
                //业务结果
                String prepay_id = restmap.get("prepay_id"); //返回的预付单信息
                response.put("nonceStr",nonce_str);
                response.put("package", "prepay_id ="+ prepay_id);
                Long timeStamp = System.currentTimeMillis() / 1000;
                response.put("timeStamp", timeStamp +""); //这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误

                String stringSignTemp ="appId ="+ WxPayConfig.appid +"＆nonceStr ="+ nonce_str +"＆package = prepay_id ="+ prepay_id +"＆signType ="+ WxPayConfig.SIGNTYPE +"＆timeStamp ="+ timeStamp;
                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String paySign = PayUtil.sign(stringSignTemp, WxPayConfig.key);
                logger.info("=======================第二次签名："+ paySign +"============ ======");
                response.put("paySign", paySign);
                //更新订单信息
                //业务逻辑代码
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return new ResultMsg("buy", response);
    }


    @RequestMapping("/wx_notify")
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line = null;
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
            String realSign = PayUtil.getSign(resMap, WxPayConfig.key);
            if(reSign.equals(realSign)){
                /**此处添加自己的业务逻辑代码start**/


                /**此处添加自己的业务逻辑代码end**/

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
