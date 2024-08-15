package com.violet.user.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.violet.common.exception.ParameterException;
import com.violet.common.exception.ServiceException;
import com.violet.user.config.SmsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class SmsCodeUtils {

    private final SmsConfig smsConfig;

    public void sendSms(String code, String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().length() != 11) throw new ParameterException("异常的手机号码");
        DefaultProfile profile = DefaultProfile.getProfile(smsConfig.getRegionId(), smsConfig.getAccessKeyId(), smsConfig.getSecret());
        IAcsClient client = new DefaultAcsClient(profile);
        //阿里云原有的配置信息我们不需要改动
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        //自定义短信信息
        request.putQueryParameter("PhoneNumbers", phoneNumber); //发送给哪个手机号
        request.putQueryParameter("SignName", smsConfig.getSignName());  //自己配置的短信签名
        request.putQueryParameter("TemplateCode", smsConfig.getTemplateCode()); //自己配置的模板 模版CODE
        //构建一个短信验证码
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map));   //转换成json字符串
        try {
            CommonResponse response = client.getCommonResponse(request); //发送至客户端
        } catch (ClientException e) {throw new ServiceException("服务异常、请等会使用");}
    }

    public String getCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            code.append(random.nextInt(10));  // 生成 0 到 9 的随机数
        }
        return code.toString();
    }
}
