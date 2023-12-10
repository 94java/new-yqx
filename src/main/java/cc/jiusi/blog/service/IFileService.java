package cc.jiusi.blog.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2023-11-23  17:40
 * @Description: 文件业务层
 */
public interface IFileService {

    /**
     * @author: 九思.
     * @date: 2023/11/23 17:42
     * @param:  MultipartFile file
     * @return: void
     * @description: 上传文件
     */
    void upload(MultipartFile file);

    /**
     * @author: 九思.
     * @date: 2023/11/23 17:51
     * @param:  Long id
     * @return: void
     * @description: 删除文件
     */
    void delete(Long id);
}
