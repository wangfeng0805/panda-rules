package org.wangfeng.panda.support;

import org.wangfeng.panda.app.PandaAppApplication;
import org.wangfeng.panda.app.common.base.AppBaseService;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * User: wangfeng
 * Date: 2020/8/26
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PandaAppApplication.class)
@ActiveProfiles("dev")
@ImportResource(locations = {"classpath:config/spring-jsf-consumer.xml"})
public class BaseUT extends AppBaseService {
}
