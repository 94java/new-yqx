package top.hellocode.devdemo;

import io.github.biezhi.ome.OhMyEmail;
import io.github.biezhi.ome.SendMailException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.hellocode.common.PasswordEncoder;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.Properties;

import static io.github.biezhi.ome.OhMyEmail.SMTP_QQ;

@SpringBootTest
class DevDemoApplicationTests {

    @Test
    void testPwdEncoder(@Autowired PasswordEncoder passwordEncoder) {
        // 加密
        String encode = passwordEncoder.encode("123456");
        // 密码匹配测试
        boolean res = passwordEncoder.match("12345678", encode);
        System.out.println(res);
    }

    @Test
    public void testSendText() {
        String code = RandomStringUtils.randomNumeric(6);
        OhMyEmail.config(SMTP_QQ(false), "1242306285@qq.com", "rcyfhxmuoadiibde");
        System.out.println("======="+code+"==========");
        try {
            //SmsUtils.sendMessage(dto.getPhone(), code);
            // 生成一个邮箱快捷登录的html邮件模板
            OhMyEmail.subject("【易启学】登录验证码")
                    .from("易启学")
                    .to("1242306285@qq.com")
                    .html("感谢您使用易启学系统，您的验证码为：<br/><strong style='font-size:40px;'>"+code + "</strong><br/>有效期为5分钟，请勿告知他人~")
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
