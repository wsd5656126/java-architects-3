package com.wusd.web.jsp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JspApp.class)
public class BaseTest {
    private static Logger logger = LoggerFactory.getLogger(BaseTest.class);
    @Test
    public void log() {
        logger.info("wusd love {}", "lij");
        logger.error("wusd love {}", "lij");
        logger.debug("wusd love {}", "lij");
    }
}
