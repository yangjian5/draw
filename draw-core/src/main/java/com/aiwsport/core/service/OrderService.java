package com.aiwsport.core.service;


import com.aiwsport.core.constant.WxConfig;
import com.aiwsport.core.entity.*;
import com.aiwsport.core.mapper.*;
import com.aiwsport.core.model.ShowOrder;
import com.aiwsport.core.utils.DataTypeUtils;
import com.aiwsport.core.utils.HttpUtils;
import com.aiwsport.core.utils.PayUtil;
import com.aiwsport.core.utils.XmlUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private IncomeMapper incomeMapper;

    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    @Autowired
    private IncomeStatisticsMapper incomeStatisticsMapper;

    private static Logger logger = LogManager.getLogger();

    public List<ShowOrder> myOrder(String openId, String status) {
        User user = userMapper.getByOpenId(openId);
        if (user == null) {
            return new ArrayList<>();
        }

        List<Order> orders = orderMapper.getOrderByUid(user.getId(), status);

        List<ShowOrder> showOrders = new ArrayList<>();
        orders.forEach(order -> {
            ShowOrder showOrder = new ShowOrder();
            showOrder.setOrder(order);
            Draws draws = drawsMapper.selectByPrimaryKey(order.getDrawId());
            showOrder.setDraws(draws);
            showOrders.add(showOrder);
        });
        return showOrders;
    }

    public Map<String, Object> createOrder(int id, int type, String openId, String tel, String name, String ip){
        Map<String, Object> resMap = null;
        try {
            String orderNo;
            int drawId;
            int drawExtId = 0;
            int orderPrice;
            String goodName;

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
                goodName = draws.getDrawName()+"创造权";
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
                goodName = draws.getDrawName()+"所有权";
            }

//            resMap = createWXOrder(openId, ip, goodName,
//                    BigDecimal.valueOf(orderPrice).divide(BigDecimal.valueOf(100)).toString());

            resMap = new HashMap<>();
            resMap.put("orderNo", "20190728145345156429682500685478");
            resMap.put("paySign", "dasdadasdadadadqwd2d22d2");
            Object orderNoObj = resMap.get("orderNo");
            Object paySignObj = resMap.get("paySign");
            if (orderNoObj == null || paySignObj == null) {
                return null;
            }

            Order order = new Order();
            order.setCode(orderNoObj.toString());
            order.setUid(user.getId());
            order.setDrawId(drawId);
            order.setDrawExtId(drawExtId);
            order.setType(type+"");
            order.setStatus("1");
            order.setoTel(tel);
            order.setInfo(paySignObj.toString());
            order.setoName(name);
            order.setOrderPrice(orderPrice);
            orderMapper.insert(order);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return resMap;
    }

    public void finishPay(String orderNo) throws Exception {
        // 查询订单信息
        Order order = orderMapper.getOrderByNo(orderNo);
        if (order == null) {
            Income income = incomeMapper.getIncomeByOrderNo(orderNo);
            if (income == null) {
                return;
            }

            DrawExt drawExt = drawExtMapper.selectByPrimaryKey(income.getDrawExtId());
            IncomeStatistics incomeStatistics = incomeStatisticsMapper.getIncomeByDrawIdAndDate(drawExt.getDrawId());
            if (incomeStatistics == null) {
                incomeStatistics = new IncomeStatistics();
                incomeStatistics.setDrawId(order.getDrawId());
                incomeStatistics.setIncomePrice(income.getProofPrice());
                incomeStatistics.setCreateTime(DataTypeUtils.formatCurDateTime());
                incomeStatisticsMapper.insert(incomeStatistics);
            } else {
                int sumIncome = incomeStatistics.getIncomePrice() + income.getProofPrice();
                incomeStatistics.setIncomePrice(sumIncome);
                incomeStatisticsMapper.updateByPrimaryKey(incomeStatistics);
            }

            Draws draws = drawsMapper.selectByPrimaryKey(drawExt.getDrawId());
            User user = userMapper.selectByPrimaryKey(draws.getProdUid());
            user.setIncome(user.getIncome() + income.getProofPrice());
            userMapper.updateByPrimaryKey(user);
            return;
        }

        User user = userMapper.selectByPrimaryKey(order.getUid());
        // 价格变化统计
        OrderStatistics orderStatistics = new OrderStatistics();

        if ("1".equals(order.getType())) {
            orderStatistics.setDrawId(order.getDrawId());

            // 商品所属人修改
            Draws draws = drawsMapper.selectByPrimaryKey(order.getDrawId());
            draws.setProdName(order.getoName());
            draws.setProdTel(order.getoTel());
            draws.setProdUid(user.getId());
            drawsMapper.updateByPrimaryKey(draws);
        } else {
            orderStatistics.setDrawId(order.getDrawExtId());

            DrawExt drawExt = drawExtMapper.selectByPrimaryKey(order.getDrawExtId());
            drawExt.setExtUid(order.getUid());
            drawExtMapper.updateByPrimaryKey(drawExt);

            // 收益计算统计
            BigDecimal income = BigDecimal.valueOf(order.getOrderPrice()).multiply(BigDecimal.valueOf(0.05));
            IncomeStatistics incomeStatistics = incomeStatisticsMapper.getIncomeByDrawIdAndDate(order.getDrawId());
            if (incomeStatistics == null) {
                incomeStatistics = new IncomeStatistics();
                incomeStatistics.setDrawId(order.getDrawId());
                incomeStatistics.setIncomePrice(income.intValue());
                incomeStatistics.setCreateTime(DataTypeUtils.formatCurDateTime());
                incomeStatisticsMapper.insert(incomeStatistics);
            } else {
                BigDecimal sumIncome = BigDecimal.valueOf(incomeStatistics.getIncomePrice()).add(income);
                incomeStatistics.setIncomePrice(sumIncome.intValue());
                incomeStatisticsMapper.updateByPrimaryKey(incomeStatistics);
            }
        }

        orderStatistics.setsPrice(order.getOrderPrice());
        orderStatistics.setType(order.getType());
        orderStatistics.setCreateTime(DataTypeUtils.formatCurDateTime());
        orderStatisticsMapper.insert(orderStatistics);

        // 修改订单状态
        order.setStatus("2");
        orderMapper.updateByPrimaryKey(order);
    }


    public Map<String, Object> createWXOrder(String openId, String ip, String goodName, String money) throws Exception {
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
            long timeStamp = System.currentTimeMillis() / 1000;
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
