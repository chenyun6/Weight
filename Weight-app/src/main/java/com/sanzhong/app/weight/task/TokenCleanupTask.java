package com.sanzhong.app.weight.task;

import com.sanzhong.domain.weight.token.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Token清理定时任务
 * 每天凌晨3点清理7天未使用的Token
 *
 * @author visual-ddd
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenCleanupTask {

    private final UserTokenRepository userTokenRepository;

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
