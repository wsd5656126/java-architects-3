package com.wusd.web.jsp;

import com.wusd.web.jsp.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    MemberService memberService;
    @Test
    public void send() {
        System.out.println("=============================================程序执行===============================");
        memberService.send();
        System.out.println("========================================程序执行完成了====================================");
    }
}
