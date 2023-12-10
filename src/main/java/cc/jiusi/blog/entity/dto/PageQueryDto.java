package cc.jiusi.blog.entity.dto;

import lombok.Data;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2023-12-10  16:36
 * @Description: TODO
 */
@Data
public class PageQueryDto {
    protected Long pageNum;
    protected Long pageSize;
}
