package com.shangfu.pay.epay.domain;

import com.google.gson.Gson;
import com.shangfu.pay.epay.domain.controller.EasypayController;
import com.shangfu.pay.epay.domain.dao.DownSpInfoRespository;
import com.shangfu.pay.epay.domain.dao.EasypayInfoRespository;
import com.shangfu.pay.epay.domain.entity.DownSpInfo;
import com.shangfu.pay.epay.domain.entity.EasypayInfo;
import com.shangfu.pay.epay.domain.util.RSAUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EpayApplicationTests {


    @Autowired
    EasypayInfoRespository easypayInfoRespository;

    @Autowired
    DownSpInfoRespository downSpInfoRespository;

    @Autowired
    EasypayController easypayController;

//    @Autowired
//    SubmitController submitController;

//    @Autowired
//    QueryController queryController;


    @Test
    public void contextLoads() throws Exception{

        Optional<DownSpInfo> downSpInfo = downSpInfoRespository.findById("1001");

        //获取公钥
        String down_pub_key = downSpInfo.get().getDown_pub_key();
        RSAPublicKey rsaPublicKey = RSAUtils.loadPublicKey(down_pub_key);

        //获取私钥
        String down_pri_key = downSpInfo.get().getDown_pri_key();
        RSAPrivateKey rsaPrivateKey = RSAUtils.loadPrivateKey(down_pri_key);

        EasypayInfo easypayInfo = new EasypayInfo();
        easypayInfo.setDown_sp_id("1001");
        easypayInfo.setDown_mch_id("101");

        easypayInfo.setOut_trade_no(System.currentTimeMillis() + "");
        easypayInfo.setBody("黄焖鸡");
        easypayInfo.setTotal_fee("666");
        easypayInfo.setCard_type("CREDIT");
        easypayInfo.setCard_name( "谢霆锋1");
        easypayInfo.setCard_no("41844896001134258651");
        easypayInfo.setId_type("ID_CARD");
        easypayInfo.setId_no("530102192005080136");
        easypayInfo.setBank_code("03010000");
        easypayInfo.setBank_name("交通银行");
        easypayInfo.setBank_mobile("12345678912");
        easypayInfo.setCvv2("123");
        easypayInfo.setCard_valid_date("0318");
//        easypayInfo.setNotify_url("http://192.168.88.239:8103/easypay/notice");
        easypayInfo.setNotify_url("http://192.168.88.239:9002/shangfu/easypay/notice");
        easypayInfo.setNonce_str("123456789");

        //公钥加密
        easypayInfo.setCard_name(RSAUtils.publicKeyEncrypt(easypayInfo.getCard_name(), rsaPublicKey));
        easypayInfo.setCard_no(RSAUtils.publicKeyEncrypt(easypayInfo.getCard_no(), rsaPublicKey));
        easypayInfo.setId_no(RSAUtils.publicKeyEncrypt(easypayInfo.getId_no(), rsaPublicKey));
        easypayInfo.setBank_mobile(RSAUtils.publicKeyEncrypt(easypayInfo.getBank_mobile(), rsaPublicKey));
        easypayInfo.setCvv2(RSAUtils.publicKeyEncrypt(easypayInfo.getCvv2(), rsaPublicKey));
        easypayInfo.setCard_valid_date(RSAUtils.publicKeyEncrypt(easypayInfo.getCard_valid_date(), rsaPublicKey));


        Gson gson = new Gson();
        String s = gson.toJson(easypayInfo);

        //私钥签名
        easypayInfo.setSign(RSAUtils.sign(s,rsaPrivateKey));
        /*String sign = easypayInfo.getSign();
        System.out.println("签名信息"+sign);*/

        String easypayInfoToJson = gson.toJson(easypayInfo);

        easypayInfoRespository.save(easypayInfo);
        System.out.println("加密后的数据为"+easypayInfoToJson);

        //String collpay = easypayController.Easypay(easypayInfoToJson);
        //System.out.println(collpay);

    }


    @Test
    public void testSubmit()throws Exception{

        Gson gson = new Gson();

        EasypayInfo easypayInfo = new EasypayInfo();
        easypayInfo.setSp_id("1000");
        easypayInfo.setMch_id("100001000000000001");
        easypayInfo.setOut_trade_no("1553763852278");
        easypayInfo.setPassword("123456");
        easypayInfo.setNonce_str("123456789");


        String submitInfoToJson = gson.toJson(easypayInfo);
        System.out.println(submitInfoToJson);

        //submitController.Submit(submitInfoToJson);
    }


    @Test
    public void testQuery(){
        EasypayInfo collpayInfo = new EasypayInfo();
        collpayInfo.setOut_trade_no("1553677712904");
        Gson gson = new Gson();
        String s = gson.toJson(collpayInfo);
        System.out.println(s);
        //String query = queryController.Query(s);
        //System.out.println(query);
    }

}
