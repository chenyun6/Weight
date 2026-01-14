package com.cy.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Weighty - 体重管理系统启动类
 *
 * @author visual-ddd
 * @since 1.0
 */
@SpringBootApplication(scanBasePackages = "com.cy")
@MapperScan(basePackages = "com.cy.infrastructure.weight.repository.mapper")
@EnableScheduling
public class WeightApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeightApplication.class, args);
    }
}
