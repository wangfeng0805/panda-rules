package org.wangfeng.panda.app;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.wangfeng.panda.app.cache.SpringUtil;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"org.wangfeng.panda"})
@MapperScan(basePackages = "org.wangfeng.panda.app.dao.mapper")
@EnableTransactionManagement(proxyTargetClass = true)
@EnableCaching
@EnableSwagger2
@EnableAsync
@Slf4j
public class PandaAppApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(PandaAppApplication.class, args);
        String[] activeProfiles = ctx.getEnvironment().getActiveProfiles();
        for (String profile : activeProfiles) {
            SpringUtil.env = profile;
            log.info("当前环境为:"+profile);
        }
    }

}
