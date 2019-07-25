package com.aiwsport.core.service;


import com.aiwsport.core.constant.WxConfig;
import com.aiwsport.core.entity.DrawExt;
import com.aiwsport.core.entity.Draws;
import com.aiwsport.core.entity.Order;
import com.aiwsport.core.entity.User;
import com.aiwsport.core.mapper.DrawExtMapper;
import com.aiwsport.core.mapper.DrawsMapper;
import com.aiwsport.core.mapper.OrderMapper;
import com.aiwsport.core.mapper.UserMapper;
import com.aiwsport.core.utils.HttpUtils;
import com.aiwsport.core.utils.PayUtil;
import com.aiwsport.core.utils.XmlUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private DrawExtMapper drawExtMapper;

    @Autowired
    private DrawsMapper drawsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    private static Logger logger = LogManager.getLogger();


    public Map<String, Object> createOrder(int id, int type, String openId, String ip){
        Map<String, Object> resMap = null;
        try {
            String orderNo = "";
            int drawId;
            int drawExtId = 0;
            int orderPrice;

                    User user = userMapper.getByOpenId(openId);
            if (user == null) {
                return null;
            }

            if (type == 1) {
                Draws draws = drawsMapper.selectByPrimaryKey(id);
                if (draws == null) {
                    return null;
                }

                drawId = draws.getId();
                orderPrice = draws.getDrawPrice();
                resMap = createWXOrder(openId, ip, draws.getDrawName()+"创造权",
                        BigDecimal.valueOf(orderPrice).divide(BigDecimal.valueOf(100)).toString());

                orderNo = (String) resMap.get("orderNo");
                if (StringUtils.isBlank(orderNo)) {
                    return null;
                }
            } else {
                DrawExt drawExt = drawExtMapper.selectByPrimaryKey(id);
                if (drawExt == null) {
                    return null;
                }

                drawExtId = drawExt.getId();
                drawId = drawExt.getDrawId();
                Draws draws = drawsMapper.selectByPrimaryKey(drawId);
                if (draws == null) {
                    return null;
                }
                orderPrice = drawExt.getExtPrice();
                resMap = createWXOrder(openId, ip, draws.getDrawName()+"所有权",
                        BigDecimal.valueOf(orderPrice).divide(BigDecimal.valueOf(100)).toString());

                orderNo = (String) resMap.get("orderNo");
                if (StringUtils.isBlank(orderNo)) {
                    return null;
                }
            }

            Order order = new Order();
            order.setCode(orderNo);
            order.setUid(user.getId());
            order.setDrawId(drawId);
            order.setDrawExtId(drawExtId);
            order.setType(type+"");
            order.setStatus("1");
            order.setOrderPrice(orderPrice);
            JSONObject jsonObj=new JSONObject(resMap);
            order.setInfo(jsonObj.toJSONString());
            orderMapper.insert(order);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resMap;
    }


    private Map<String, Object> createWXOrder(String openId, String ip, String goodName, String money) throws Exception {
        //生成的随机字符串
        String nonce_str = PayUtil.getNonceStr();
        String orderNo = PayUtil.getTradeNo();
        Map<String, String> packageParams = new HashMap<>();
        packageParams.put("appid", WxConfig.appid);
        packageParams.put("mch_id", WxConfig.mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", goodName);//商品名称
        packageParams.put("out_trade_no", orderNo); //商户订单号
        packageParams.put("total_fee", money); //支付金额，这边需要转成字符串类型，否则后面的签名会失败
        packageParams.put("spbill_create_ip", ip);//获取本机的IP地址
        packageParams.put("notify_url", WxConfig.notify_url);
        packageParams.put("trade_type", WxConfig.TRADETYPE);
        packageParams.put("openid", openId);
        //把数组所有元素，按照"参数=参数值"的模式用"＆"字符拼接成字符串
        // MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
        String mysign = PayUtil.getSign(packageParams, WxConfig.SECRET);
        packageParams.put("sign", mysign);

        logger.info("=======================第一次签名："+ mysign +"============ ======");

        //拼接统一下单接口使用的XML数据，要将上一步生成的签名一起拼接进去
        String xml = XmlUtil.xmlFormat(packageParams, false);

        System.out.println("调试模式_统一下单接口请求XML数据："+ xml);

        //调用统一下单接口，并接受返回的结果
        String restxml = HttpUtils.posts(WxConfig.pay_url, xml);

        System.out.println("调试模式_统一下单接口返回XML数据："+restxml);

        //将解析结果存储在HashMap中
        Map<String, String> restmap = XmlUtil.xmlParse(restxml);

        String return_code = restmap.get("return_code"); //返回状态码

        //返回给移动端需要的参数
        Map<String, Object> response = new HashMap<>();
        if ("SUCCESS".equals(return_code)) {
            response.put("appid", WxConfig.appid);
            //业务结果
            String prepay_id = restmap.get("prepay_id"); //返回的预付单信息
            response.put("nonceStr",nonce_str);
            response.put("package", "prepay_id ="+ prepay_id);
            Long timeStamp = System.currentTimeMillis() / 1000;
            response.put("timeStamp", timeStamp +""); //这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
            response.put("orderNo", orderNo); //商户订单号

            String stringSignTemp ="appId ="+ WxConfig.appid +"＆nonceStr ="+ nonce_str +"＆package = prepay_id ="+ prepay_id +"＆signType ="+ WxConfig.SIGNTYPE +"＆timeStamp ="+ timeStamp;
            //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
            String paySign = PayUtil.sign(stringSignTemp, WxConfig.SECRET);
            logger.info("=======================第二次签名："+ paySign +"============ ======");
            response.put("paySign", paySign);
            //更新订单信息
            //业务逻辑代码
        }
        return response;
    }

}
