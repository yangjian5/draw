import com.aiwsport.web.utlis.ParseUrl;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by yangjian9 on 2018/11/20.
 */
public class TaccountMy {



    public static void main(String[] args){

        String datas = "6732405366\n" +
                "6731517772\n" +
                "6588740489\n" +
                "6731491520\n" +
                "6731556675\n" +
                "6732448007\n" +
                "6732607250\n" +
                "6731539156\n" +
                "6731579724\n" +
                "6732416292\n" +
                "6732573548\n" +
                "6731523492\n" +
                "6731533817\n" +
                "6731603544\n" +
                "6731676342\n" +
                "6731482075\n" +
                "6731585710\n" +
                "6732394599\n" +
                "6732585392\n" +
                "6567631747\n" +
                "6732132936\n" +
                "6731551602\n" +
                "6731698904\n" +
                "6732561776\n" +
                "6731506959\n" +
                "6731670943\n" +
                "6732511801\n" +
                "6732529489\n" +
                "6732555214\n" +
                "6731502204\n" +
                "6731616066\n" +
                "6732431576\n" +
                "6731659707\n" +
                "6732630496\n" +
                "6731568317\n" +
                "6732624340\n" +
                "6731622523\n" +
                "6733011140\n" +
                "6732096181\n" +
                "6733080310\n" +
                "6732084690\n" +
                "6732099969\n" +
                "6732107556\n" +
                "6732120844\n" +
                "6732612348\n" +
                "6733022482\n" +
                "6732168907\n" +
                "6733032402\n" +
                "6732642890\n" +
                "6692506338";

        String[] uids = datas.split("\n");

        for (String uid : uids) {

            try {
                String res = getTaccount(uid);
                System.out.println(res);
            } catch (Exception e) {
                System.out.println("get taccount is error: " + uid);
            }


        }




    }


    public static String getTaccount(String uid) throws Exception{
        String url = "http://api.weibo.com/taccount/v2/get.json?uid=yangjian&source=3439264077&sensitive=1";

        String res = ParseUrl.getDataFromUrl(url.replace("yangjian", uid+""));
//        System.out.println(res);
        JSONObject json = JSONObject.parseObject(res);

        JSONObject alipay = (JSONObject)json.get("alipay");

        return "uid:" + uid + "   " + "ali_id:" + String.valueOf(alipay.get("ali_id")) + "   "
                + "ali_mobile:" + String.valueOf(alipay.get("ali_mobile"))
                + "   " + "ali_email:" + String.valueOf(alipay.get("ali_email")) + "   "
                + "intime:" + String.valueOf(json.get("intime"));
    }





}
