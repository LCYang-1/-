package com.lcy.miaosha.send;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SendSmsUtil {

    @Value("${send.mobile.regionId}")
    private String regionId;



    @Value("${send.mobile.ignName}")
    private String SignName;

    @Value("${send.mobile.templateCode}")
    private String TemplateCode;

    @Value("${send.mobile.accessKeyId}")
    private String accessKeyId;

    @Value("${send.mobile.accessSecret}")
    private String accessSecret;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }



    public String getSignName() {
        return SignName;
    }

    public void setSignName(String signName) {
        SignName = signName;
    }

    public String getTemplateCode() {
        return TemplateCode;
    }

    public void setTemplateCode(String templateCode) {
        TemplateCode = templateCode;
    }

    public void SendSms(String u_phone, String message){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",getAccessKeyId(), getAccessSecret());
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", getRegionId());
        request.putQueryParameter("PhoneNumbers", u_phone);
        request.putQueryParameter("SignName", "青工秒杀");
        request.putQueryParameter("TemplateCode", getTemplateCode());
        request.putQueryParameter("TemplateParam", "{\"code\":"+message+"}");
        System.out.println(getSignName());
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
//    public static void main(String[] args) {
//
//
//
//        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",accessKeyId, "<accessSecret>");
//        IAcsClient client = new DefaultAcsClient(profile);
//
//        CommonRequest request = new CommonRequest();
//        request.setSysMethod(MethodType.POST);
//        request.setSysDomain("dysmsapi.aliyuncs.com");
//        request.setSysVersion("2017-05-25");
//        request.setSysAction("SendSms");
//        request.putQueryParameter("RegionId", "cn-hangzhou");
//        request.putQueryParameter("PhoneNumbers", "15836183586");
//        request.putQueryParameter("SignName", "青工秒杀");
//        request.putQueryParameter("TemplateCode", "SMS_191815046");
//        try {
//            CommonResponse response = client.getCommonResponse(request);
//            System.out.println(response.getData());
//        } catch (ServerException e) {
//            e.printStackTrace();
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }
//    }
}
