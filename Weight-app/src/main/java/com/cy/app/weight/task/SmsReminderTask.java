package com.cy.app.weight.task;

import com.cy.domain.weight.user.User;
import com.cy.domain.weight.user.UserRepository;
import com.cy.domain.weight.weightrecord.WeightRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * 短信提醒定时任务
 * 如果用户两天没有点击，则发送短信提醒
 *
 * @author visual-ddd
 * @since 1.0
 */
@Slf4j
@Component
public class SmsReminderTask {

    @Resource
    private UserRepository userRepository;

    @Resource
    private WeightRecordRepository weightRecordRepository;

    /**
     * 每天凌晨2点执行
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void sendReminderSms() {
        log.info("开始执行短信提醒任务");
        LocalDate twoDaysAgo = LocalDate.now().minusDays(2);

        try {
            // 1. 查询所有用户
            List<User> users = userRepository.findAll();
            
            // 2. 对每个用户，查询最后记录日期
            for (User user : users) {
                LocalDate lastRecordDate = weightRecordRepository.findLastRecordDate(user.getId());
                
                // 3. 如果最后记录日期是两天前或更早（或没有记录），发送提醒短信
                if (lastRecordDate == null || lastRecordDate.isBefore(twoDaysAgo) || lastRecordDate.isEqual(twoDaysAgo)) {
                    // TODO 发送提醒短信到用户手机号
                    log.info("用户 {} 超过两天未记录，发送提醒短信到 {}", user.getId(), user.getPhone());
                    // 这里应该调用短信服务发送提醒短信
                    // 并记录到 sms_log 表
                }
            }
        } catch (Exception e) {
            log.error("短信提醒任务执行失败", e);
        }

        log.info("短信提醒任务执行完成");
    }
}
