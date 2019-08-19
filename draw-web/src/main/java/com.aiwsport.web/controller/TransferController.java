package com.aiwsport.web.controller;

import com.aiwsport.core.DrawServerException;
import com.aiwsport.core.DrawServerExceptionFactor;
import com.aiwsport.core.constant.ResultMsg;
import com.aiwsport.core.constant.WxConfig;
import com.aiwsport.core.entity.User;
import com.aiwsport.core.service.UserService;
import com.aiwsport.core.utils.HttpUtils;
import com.aiwsport.core.utils.PayUtil;
import com.aiwsport.core.utils.XmlUtil;
import com.aiwsport.web.verify.ParamVerify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 提现相关接口
 */
@RestController
@RequestMapping(value = "/api")
public class TransferController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);

    /**
     * 企业向个人支付转账
     * @param open_id
     * @param amount
     * @param request
     */
    @RequestMapping(value = "/withdrawal.json")
    @Transactional
    public ResultMsg transferPay(@ParamVerify(isNotBlank = true)String open_id,
                                 @ParamVerify(isNumber = true)int amount,
                                 HttpServletRequest request) {
        if(!(amount>100 && amount<500000)){
           // 不在提现范围
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "提现金额需要在1-5000元之间");
        }

        User user = userService.getUser(open_id);
        if (user == null) {
            // 不是我们的用户，不能提现
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "user is exist");
        }

        if (amount > user.getIncome()) {
            // 钱不够
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "余额不足");
        }

        /** ==================================================封装提现所需参数================================================*/
        Map<String, String> restmap = null;
        String tradeNo = PayUtil.getTradeNo();
        try {
            Map<String, String> parm = new HashMap<>();
            parm.put("mch_appid", WxConfig.appid);
            parm.put("mchid", WxConfig.mch_id); //商户号
            parm.put("nonce_str", PayUtil.getNonceStr()); //随机字符串
            parm.put("partner_trade_no", tradeNo); //商户订单号
            parm.put("openid", open_id); //用户openid oCVr20N2YLH9VQztnkZTaCj2aYYY
            parm.put("check_name", "NO_CHECK"); //校验用户姓名选项 OPTION_CHECK
            //parm.put("re_user_name", "安迪"); //check_name设置为FORCE_CHECK或OPTION_CHECK，则必填
            parm.put("amount", BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(100)).toString()); //转账金额
            parm.put("desc", user.getNickName() +" 申请提现金额："+amount+"分 " + user.getId()); //企业付款描述信息
            parm.put("spbill_create_ip", "134.175.110.50"); //Ip地址
            parm.put("sign", PayUtil.getSign(parm, "artchain825c3af0bd36a25c1396c72b"));

            String restxml = HttpUtils.posts(WxConfig.TRANSFERS_PAY, XmlUtil.xmlFormat(parm, false));
            restmap = XmlUtil.xmlParse(restxml);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 异常返回
            return new ResultMsg("withdrawal", "withdrawal is fail");
        }
        /** ========================================================提现结果处理===================================================*/
        if (!CollectionUtils.isEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code"))) {
            logger.info("转账成功");
            //生成提现记录
            userService.withdrawal(user.getId(), tradeNo, -amount);
        } else {
            // 转账失败返回
            return new ResultMsg("withdrawal", "withdrawal is fail");
        }
        return new ResultMsg("withdrawal", true);
    }


    /**
     * 企业向个人转账查询
     * @param tradeno 商户转账订单号
     */
    @PostMapping(value = "/withdrawal_query.json")
    public ResultMsg orderPayQuery(@ParamVerify(isNotBlank = true)String tradeno) {
        Map<String, String> restmap = null;
        try {
            Map<String, String> parm = new HashMap<String, String>();
            parm.put("appid", WxConfig.appid);
            parm.put("mch_id", WxConfig.mch_id);
            parm.put("partner_trade_no", tradeno);
            parm.put("nonce_str", PayUtil.getNonceStr());
            parm.put("sign", PayUtil.getSign(parm, WxConfig.SECRET));

            String restxml = HttpUtils.posts(WxConfig.TRANSFERS_PAY_QUERY, XmlUtil.xmlFormat(parm, true));
            restmap = XmlUtil.xmlParse(restxml);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        if (!CollectionUtils.isEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code"))) {
            // 订单查询成功 处理业务逻辑
            logger.info("订单查询：订单" + restmap.get("partner_trade_no") + "支付成功");
            Map<String, String> transferMap = new HashMap<>();
            transferMap.put("partnerTradeNo", restmap.get("partner_trade_no"));//商户转账订单号
            transferMap.put("openid", restmap.get("openid")); //收款微信号
            transferMap.put("paymentAmount", restmap.get("payment_amount")); //转账金额
            transferMap.put("transferTime", restmap.get("transfer_time")); //转账时间
            transferMap.put("desc", restmap.get("desc")); //转账描述
            return new ResultMsg("pay_query", transferMap);
        }else {
            if (!CollectionUtils.isEmpty(restmap)) {
                logger.info("订单转账查询失败：" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
            }
            throw new DrawServerException(DrawServerExceptionFactor.DEFAULT, "pay_query is fail");
        }
    }



}

