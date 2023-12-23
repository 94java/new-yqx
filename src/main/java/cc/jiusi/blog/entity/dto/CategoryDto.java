package cc.jiusi.blog.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2023-12-10  16:35
 * @Description: TODO
 */
@Data
public class CategoryDto{
    /**
     * @author: 九思.
     * @date: 2023/12/10 16:37
     * @description: 标签名
     */
    private String name;

    /**
     * @author: 九思.
     * @date: 2023/12/10 16:37
     * @description: 创建者
     */
    private String status;

    /**
     * @author: 九思.
     * @date: 2023/12/10 16:37
     * @description: 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date[] createTime;
}
