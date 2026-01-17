package com.cy.infrastructure.weight.sms;

import cn.hutool.core.util.StrUtil;
import com.aliyun.dypnsapi20170525.Client;
import com.aliyun.dypnsapi20170525.models.CheckSmsVerifyCodeRequest;
import com.aliyun.dypnsapi20170525.models.CheckSmsVerifyCodeResponse;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.aliyun.teaopenapi.models.Config;
import com.cy.domain.weight.sms.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 阿里云短信服务实现
 * 使用阿里云号码认证服务的SendSmsVerifyCode和CheckSmsVerifyCode接口
 * 实现领域层定义的SmsService接口
 *
 * 参考文档：https://api.aliyun.com/api/Dypnsapi/2017-05-25/SendSmsVerifyCode
 *
 * @author visual-ddd
 * @since 1.0
 */
@Slf4j
@Service
public class AliyunSmsService implements SmsService {

    @Value("${aliyun.sms.access-key-id:}")
    private String accessKeyId;

    @Value("${aliyun.sms.access-key-secret:}")
    private String accessKeySecret;

    @Value("${aliyun.sms.scheme-name:}")
    private String schemeName;

    @Value("${aliyun.sms.sign-name:}")
    private String signName;

    @Value("${aliyun.sms.template-code:}")
    private String templateCode;

    @Value("${aliyun.sms.endpoint:dypnsapi.aliyuncs.com}")
    private String endpoint;

    @Value("${sms.enabled:false}")
    private boolean smsEnabled;

    private Client client;

    /**
     * 初始化阿里云号码认证客户端
     * 使用dypnsapi20170525 SDK，支持SendSmsVerifyCode和CheckSmsVerifyCode接口
     */
    @PostConstruct
    public void init() {
        if (!smsEnabled) {
            log.info("短信服务未开启，将使用测试模式（固定验证码）");
            return;
        }

        if (StrUtil.isBlank(accessKeyId) || StrUtil.isBlank(accessKeySecret)) {
            log.warn("阿里云短信配置未完成，将无法发送短信");
            return;
        }

        try {
            Config config = new Config()
                    .setAccessKeyId(accessKeyId)
                    .setAccessKeySecret(accessKeySecret);
            config.setEndpoint(endpoint);
            this.client = new Client(config);
            log.info("阿里云号码认证客户端初始化成功");
        } catch (Exception e) {
            log.error("阿里云号码认证客户端初始化失败", e);
        }
    }

    /**
     * 发送验证码短信
     * 使用阿里云号码认证服务的SendSmsVerifyCode接口
     * 支持动态生成验证码（使用##code##占位符）或传入固定验证码
     *
     * @param phone 手机号
     * @param code  验证码（可选，如果为null则使用动态生成验证码，可通过CheckSmsVerifyCode核验）
     * @return 是否发送成功
     */
    @Override
    public boolean sendVerificationCode(String phone, String code) {
        if (!smsEnabled) {
            log.debug("短信服务未开启，跳过发送。手机号：{}", phone);
            return false;
        }

        if (client == null) {
            log.warn("阿里云号码认证客户端未初始化，跳过发送。手机号：{}，验证码：{}", phone, code);
            return false;
        }

        if (StrUtil.isBlank(schemeName) || StrUtil.isBlank(signName) || StrUtil.isBlank(templateCode)) {
            log.warn("阿里云号码认证配置未完成（scheme-name/sign-name/template-code），跳过发送。手机号：{}", phone);
            return false;
        }

        try {
            SendSmsVerifyCodeRequest request = new SendSmsVerifyCodeRequest();
            request.setSchemeName(schemeName);
            request.setPhoneNumber(phone);
            request.setCountryCode("86"); // 中国区默认86
            request.setSignName(signName);
            request.setTemplateCode(templateCode);
            
            // 如果提供了验证码，使用固定验证码；否则使用动态生成
            request.setTemplateParam(String.format("{\"code\":\"%s\",\"min\":\"%s\"}", code, 1));

            SendSmsVerifyCodeResponse response = client.sendSmsVerifyCode(request);
            
            if (response != null && response.getBody() != null) {
                String responseCode = response.getBody().getCode();
                if ("OK".equals(responseCode)) {
                    String verifyCode = response.getBody().getModel().getVerifyCode();
                    log.info("阿里云验证码短信发送成功。手机号：{}，verifyCode：{}", phone, verifyCode);
                    return true;
                } else {
                    String message = response.getBody().getMessage();
                    log.error("阿里云验证码短信发送失败。手机号：{}，错误码：{}，错误信息：{}", phone, responseCode, message);
                    return false;
                }
            } else {
                log.error("阿里云验证码短信发送失败，响应为空。手机号：{}", phone);
                return false;
            }
        } catch (Exception e) {
            log.error("阿里云验证码短信发送异常。手机号：{}，验证码：{}", phone, code, e);
            return false;
        }
    }

    /**
     * 核验验证码
     * 使用阿里云号码认证服务的CheckSmsVerifyCode接口核验验证码
     * 注意：只有使用动态生成验证码（##code##）发送的验证码才能通过此接口核验
     *
     * @param phone 手机号
     * @param code  验证码
     * @return 是否核验成功
     */
    @Override
    public boolean verifyCode(String phone, String code) {
        if (!smsEnabled) {
            // 测试模式：固定验证码"111111"
            boolean verified = "111111".equals(code);
            if (verified) {
                log.info("测试模式：验证码核验成功。手机号：{}", phone);
            }
            return verified;
        }

        if (client == null) {
            log.warn("阿里云号码认证客户端未初始化，无法核验验证码。手机号：{}", phone);
            return false;
        }

        if (StrUtil.isBlank(schemeName)) {
            log.warn("阿里云号码认证方案未配置，无法核验验证码。手机号：{}", phone);
            return false;
        }

        if (StrUtil.isBlank(code)) {
            log.warn("验证码为空。手机号：{}", phone);
            return false;
        }

        try {
            CheckSmsVerifyCodeRequest request = new CheckSmsVerifyCodeRequest();
            request.setSchemeName(schemeName);
            request.setPhoneNumber(phone);
            request.setCountryCode("86"); // 中国区默认86
            request.setVerifyCode(code);
            request.setCaseAuthPolicy(1L);

            CheckSmsVerifyCodeResponse response = client.checkSmsVerifyCode(request);
            
            if (response != null && response.getBody() != null) {
                String responseCode = response.getBody().getCode();
                String verifyResult = response.getBody().getModel().getVerifyResult();
                
                if ("OK".equals(responseCode) && "PASS".equals(verifyResult)) {
                    log.info("阿里云验证码核验成功。手机号：{}", phone);
                    return true;
                } else {
                    log.warn("阿里云验证码核验失败。手机号：{}，错误码：{}，核验结果：{}", phone, responseCode, verifyResult);
                    return false;
                }
            } else {
                log.error("阿里云验证码核验失败，响应为空。手机号：{}", phone);
                return false;
            }
        } catch (Exception e) {
            log.error("阿里云验证码核验异常。手机号：{}，验证码：{}", phone, code, e);
            return false;
        }
    }
}
