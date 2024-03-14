package csu.yulin.utils;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class AliyunSmsUtil {


    private final String smsTemplateCode = "SMS_154950909";

    private final String smsSignName = "阿里云短信测试";

    /**
     * 使用 AK&ASK 初始化账号 Client
     *
     * @param accessKeyId     AccessKey ID
     * @param accessKeySecret AccessKey Secret
     * @param endpoint        访问的域名
     * @return Client
     * @throws Exception 短信推送异常
     */
    public static Client createClient(String accessKeyId, String accessKeySecret, String endpoint)
            throws Exception {
        Config config =
                new Config()
                        .setAccessKeyId(accessKeyId)
                        .setAccessKeySecret(accessKeySecret);
        config.endpoint = endpoint;
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    /**
     * 发送短信验证码
     *
     * @param phone 电话号码
     * @param code  验证码
     * @throws Exception 短信推送异常
     */
    public void sendCode(
            String phone,
            String code)
            throws Exception {
        Client client = createClient("LTAI5t6AC4ntMfkS2V6LciSV", "o94mAmWPCsKYu71sNFjiAXwvpvFEP1", "dysmsapi.aliyuncs.com");
        SendSmsRequest sendSmsRequest =
                new SendSmsRequest()
                        .setSignName(smsSignName)
                        .setTemplateCode(smsTemplateCode)
                        .setPhoneNumbers(phone)
                        .setTemplateParam("{\"code\":\"" + code + "\"}");
        try {
            log.info("发送短信入参: " + JSON.toJSONString(sendSmsRequest));
            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
            log.info("发送短信结果: " + JSON.toJSONString(sendSmsRequest));
            if (Objects.nonNull(sendSmsResponse)) {
                sendSmsResponse.getBody();
            }
        } catch (Exception e) {
            log.error("短信推送异常结果: " + e.getMessage());
        }
    }
}
