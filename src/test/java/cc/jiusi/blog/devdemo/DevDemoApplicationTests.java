package cc.jiusi.blog.devdemo;

import cc.jiusi.blog.common.res.ResultCodeEnum;
import cc.jiusi.blog.common.utils.PasswordEncoder;
import cc.jiusi.blog.exception.GlobalException;
import cn.hutool.extra.pinyin.PinyinUtil;
import io.github.biezhi.ome.OhMyEmail;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        System.out.println("=======" + code + "==========");
        try {
            //SmsUtils.sendMessage(dto.getPhone(), code);
            // 生成一个邮箱快捷登录的html邮件模板
            OhMyEmail.subject("【易启学】登录验证码")
                    .from("易启学")
                    .to("1242306285@qq.com")
                    .html("感谢您使用易启学系统，您的验证码为：<br/><strong style='font-size:40px;'>" + code + "</strong><br/>有效期为5分钟，请勿告知他人~")
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testConvert() {
        // 汉字转拼音
        String str = "九四dk42%#_$";
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            // 过滤非数字字母下划线的字符

            if (PinyinUtil.isChinese(c)) {
                // 是中文，取首字母,下划线分割
                sb.append(PinyinUtil.getFirstLetter(c));
            }
            // 是数字字母或者下划线
            else if (
                    c == '_' ||
                    (c >= '0' && c <= '9') ||
                    (c >= 'a' && c <= 'z') ||
                    (c >= 'A' && c <= 'Z')
            ) {
                sb.append(c);
            }
        }

        System.out.println(sb.toString());
    }
}
