package com.aiwsport.core.constant;

public class WxConfig {
    //小程序appid
    public static final String appid = "wx212677b8e5e12f06";
    //微信支付的商户id
    public static final String mch_id = "1481009602";
    //微信支付的商户密钥
    public static final String SECRET = "339315a194cfcbda9d9d8aa653e59ea8";
    //支付成功后的服务器回调url
    public static final String notify_url = "https://art.artchains.cn/api/wx_notify.json";
    //签名方式
    public static final String SIGNTYPE = "MD5";
    //交易类型
    public static final String TRADETYPE = "JSAPI";
    //微信统一下单接口地址
    public static final String pay_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    // 企业付款API
    public static final String TRANSFERS_PAY = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
    // 企业付款查询API
    public static final String TRANSFERS_PAY_QUERY = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo";
}
