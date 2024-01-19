package cc.jiusi.blog.service.impl;

import cc.jiusi.blog.common.res.ResultCodeEnum;
import cc.jiusi.blog.common.utils.MinioUtils;
import cc.jiusi.blog.common.utils.UserContext;
import cc.jiusi.blog.entity.po.Source;
import cc.jiusi.blog.exception.GlobalException;
import cc.jiusi.blog.mapper.SourceMapper;
import cc.jiusi.blog.service.IFileService;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.URLUtil;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2023-11-23  17:41
 * @Description: 文件服务
 */
@Service
public class FileServiceImpl implements IFileService {
    @Autowired
    private MinioUtils minioUtils;
    @Value("${minio.bucketName}")
    private String bucketName;
    @Autowired
    private SourceMapper sourceMapper;

    /**
     * @param file
     * @author: 九思.
     * @date: 2023/11/23 17:42
     * @param: MultipartFile file
     * @return: void
     * @description: 上传文件
     */
    @Override
    public void upload(MultipartFile file) {
        if (file == null) {
            throw new GlobalException(ResultCodeEnum.FILE_NOT_FOUND);
        }
        // 获取原文件名
        String originalFilename = file.getOriginalFilename();
        // 生成新文件名（防止重复）
        String fileName = UUID.randomUUID().toString();
        try {
            // 上传到minio中
            minioUtils.putObject(bucketName, file, fileName);
            // 保存信息到数据库中
            Source source = constructSource(originalFilename, fileName);
            sourceMapper.insert(source);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResultCodeEnum.FILE_UPLOAD_FAILED);
        }
    }

    /**
     * @param id
     * @author: 九思.
     * @date: 2023/11/23 17:51
     * @param: Long id
     * @return: void
     * @description: 删除文件
     */
    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new GlobalException(ResultCodeEnum.PARAMS_FAILED);
        }
        // 从数据库查询在minio中的存储名
        Source source = sourceMapper.selectById(id);
        String minioName = source.getRealName();
        try {
            // 从minio删除文件
            minioUtils.removeObject(bucketName, minioName);
            // 删除数据库中的记录
            sourceMapper.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResultCodeEnum.FILE_DELETE_FAILED);
        }
    }

    @Override
    public List<Map<String, String>> getFileList() {
        Iterable<Result<Item>> results = null;
        List<Map<String, String>> res = new ArrayList<>();
        try {
            System.out.println(bucketName);
            results = minioUtils.listObjects(bucketName);
            results.forEach(item -> {
                String name = null;
                String url = null;
                try {
                    name = URLUtil.decode(item.get().objectName());
                    url = minioUtils.getObjectUrl(bucketName, name);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Map<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("url", url);
                res.add(map);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    private Source constructSource(String originalFilename, String fileName) {
        Source source = new Source();
        source.setName(originalFilename);
        // TODO 设置文件类型
        // source.setType(file.getContentType());
        source.setUid(UserContext.getUserId());
        source.setRealName(fileName);
        return source;
    }
}
