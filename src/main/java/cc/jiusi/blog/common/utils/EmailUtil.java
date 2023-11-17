package cc.jiusi.blog.common.utils;

import io.github.biezhi.ome.OhMyEmail;
import io.github.biezhi.ome.SendMailException;
import lombok.extern.slf4j.Slf4j;

import static io.github.biezhi.ome.OhMyEmail.SMTP_QQ;

/**
 * @blog: <a href="https://www.hellocode.top">HelloCode.</a>
 * @Author: HelloCode.
 * @CreateTime: 2023-11-16  16:06
 * @Description: 邮件工具类
 */
@Slf4j
public class EmailUtil {
    private static final String username = "1242306285@qq.com";
    private static final String password = "rcyfhxmuoadiibde";

    static {
        // 仅需要执行一次
        OhMyEmail.config(SMTP_QQ(false), username, password);
    }
    public static void sendEmailCode(String to,String code) throws SendMailException {
        log.debug("========={}==========",code);
        // 生成一个邮箱快捷登录的html邮件模板
        OhMyEmail.subject("【易启学】登录验证码")
                .from("易启学")
                .to(to)
                .html("感谢您使用易启学系统，您的验证码为：<br/><strong style='font-size:40px;'>"+code + "</strong><br/>有效期为5分钟，请勿告知他人~")
                .send();

    }
}
