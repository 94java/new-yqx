package cc.jiusi.blog.devdemo;

import cc.jiusi.blog.common.res.ResultCodeEnum;
import cc.jiusi.blog.common.utils.MinioUtils;
import cc.jiusi.blog.common.utils.PasswordEncoder;
import cc.jiusi.blog.exception.GlobalException;
import cn.hutool.Hutool;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import io.github.biezhi.ome.OhMyEmail;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

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

    /**
     * @author: 九思.
     * @date: 2023/11/19 16:04
     * @param:
     * @return: void
     * @description: 测试 Minio
     */
    @Test
    public void testMinio(@Autowired MinioUtils minioUtils) throws IOException, ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\lihao\\Pictures\\Camera Roll\\qq.jpg");
        boolean bs = minioUtils.putObject("94blog", "头像.jpg", fileInputStream, "image/jpg");
        System.out.println(bs);
        // 获取对象访问路径
        System.out.println(minioUtils.getObjectUrl("94blog","qq.jpg"));
    }

    /**
     * @author: 九思.
     * @date: 2024/1/19 20:22
     * @param:  MinioUtils minioUtils
     * @return: void
     * @description: 获取 Minio 文件列表
     */
    @Test
    public void testGetMinioList(@Autowired MinioUtils minioUtils) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InvalidResponseException, InternalException {
        Iterable<Result<Item>> results = minioUtils.listObjects("94blog");
        results.forEach(item -> {
            String name = null;
            String etag = null;
            String url = null;
            try {
                name = item.get().objectName();
                etag = item.get().etag();
                name = URLUtil.decode(name);
                url = minioUtils.getObjectUrl("94blog",name);
            } catch (ErrorResponseException | InsufficientDataException | InternalException |
                     InvalidBucketNameException | InvalidKeyException | InvalidResponseException | IOException |
                     NoSuchAlgorithmException | ServerException | XmlParserException e) {
                throw new RuntimeException(e);
            }
            System.out.println(name + "===" + etag + "===" + url);
        });
    }
}
