-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `phone` VARCHAR(11) NOT NULL COMMENT '手机号',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 验证码表
CREATE TABLE IF NOT EXISTS `verification_code` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `phone` VARCHAR(11) NOT NULL COMMENT '手机号',
    `code` VARCHAR(6) NOT NULL COMMENT '验证码',
    `expire_time` DATETIME NOT NULL COMMENT '过期时间',
    `send_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    `ip` VARCHAR(50) COMMENT 'IP地址',
    `used` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已使用 0-未使用 1-已使用',
    PRIMARY KEY (`id`),
    KEY `idx_phone_time` (`phone`, `send_time`),
    KEY `idx_ip_time` (`ip`, `send_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='验证码表';

-- 体重记录表
CREATE TABLE IF NOT EXISTS `weight_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `weight_type` TINYINT NOT NULL COMMENT '体重类型 1-胖了 2-瘦了',
    `record_date` DATE NOT NULL COMMENT '记录日期',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_date` (`user_id`, `record_date`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_record_date` (`record_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='体重记录表';

-- 短信发送记录表
CREATE TABLE IF NOT EXISTS `sms_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT COMMENT '用户ID',
    `phone` VARCHAR(11) NOT NULL COMMENT '手机号',
    `sms_type` TINYINT NOT NULL COMMENT '短信类型 1-验证码 2-提醒',
    `send_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    `content` VARCHAR(500) COMMENT '短信内容',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_phone_time` (`phone`, `send_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短信发送记录表';
