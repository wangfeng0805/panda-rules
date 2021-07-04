package org.wangfeng.panda.app.configuration.mybatis;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * User: wangfeng
 * Date: 2020/8/25
 */
@Slf4j
@Configuration
public class MyBatisConfiguration {
    @Bean
    public PageHelper pageHelper() {
        log.info("resist PageHelper for mybatis page select");
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }
}