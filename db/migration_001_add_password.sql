-- 迁移脚本：为 user 表添加密码字段
-- 执行：mysql -u root -p weight < db/migration_001_add_password.sql

ALTER TABLE `user`
    ADD COLUMN `password` VARCHAR(128) DEFAULT NULL COMMENT '登录密码（BCrypt加密）' AFTER `phone`;
