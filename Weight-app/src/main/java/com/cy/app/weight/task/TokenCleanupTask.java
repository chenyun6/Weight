package com.cy.app.weight.task;

import com.cy.infrastructure.weight.repository.UserTokenRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Token清理定时任务
 * 每天凌晨3点清理7天未使用的Token
 *
 * @author visual-ddd
 * @since 1.0
 */
@Slf4j
@Component
public class TokenCleanupTask {

    @Resource
    private UserTokenRepositoryImpl userTokenRepository;

    /**
     * 每天凌晨3点执行
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanupUnusedTokens() {
        log.info("开始清理7天未使用的Token");
        try {
            userTokenRepository.deleteUnusedTokens(7);
            log.info("Token清理完成");
        } catch (Exception e) {
            log.error("Token清理失败", e);
        }
    }
}
