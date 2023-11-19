package cc.jiusi.blog.common.utils;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * MinIO 客户端工具类
 */
@Component
@Slf4j
public class MinioUtils {

    @Autowired
    private MinioClient minioClient;

    private static final int DEFAULT_EXPIRY_TIME = 7 * 24 * 3600;

    /**
     * 检查存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    public boolean bucketExists(String bucketName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        boolean flag = false;
        flag = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (flag) {
            return true;
        }
        return false;
    }

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     */
    public boolean makeBucket(String bucketName) throws IOException, InvalidKeyException, InvalidResponseException, RegionConflictException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        boolean flag = bucketExists(bucketName);
        if (!flag) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 列出所有存储桶名称
     *
     * @return List<String>
     */
    public List<String> listBucketNames() throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        List<Bucket> bucketList = listBuckets();
        List<String> bucketListName = new ArrayList<>();
        for (Bucket bucket : bucketList) {
            bucketListName.add(bucket.name());
        }
        return bucketListName;
    }

    /**
     * 列出所有存储桶
     *
     * @return List<Bucket>
     */
    public List<Bucket> listBuckets() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        return minioClient.listBuckets();
    }

    /**
     * 删除存储桶
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    public boolean removeBucket(String bucketName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                // 有对象文件，则删除失败
                if (item.size() > 0) {
                    return false;
                }
            }
            // 删除存储桶，注意，只有存储桶为空时才能删除成功。
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            flag = bucketExists(bucketName);
            if (!flag) {
                return true;
            }

        }
        return false;
    }

    /**
     * 列出存储桶中的所有对象名称
     *
     * @param bucketName 存储桶名称
     * @return List<String>
     */
    public List<String> listObjectNames(String bucketName) throws XmlParserException, IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {
        List<String> listObjectNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                listObjectNames.add(item.objectName());
            }
        }
        return listObjectNames;
    }

    /**
     * 列出存储桶中的所有对象
     *
     * @param bucketName 存储桶名称
     * @return Iterable<Result<Item>>
     */
    public Iterable<Result<Item>> listObjects(String bucketName) throws XmlParserException, IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            return minioClient.listObjects( ListObjectsArgs.builder().bucket(bucketName).build());
        }
        return null;
    }

    /**
     * 通过文件上传到对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param fileName   File name
     * @return boolean
     */
    public boolean uploadObject(String bucketName, String objectName, String fileName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName).object(objectName).filename(fileName).build());
            ObjectStat statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.length() > 0) {
                return true;
            }
        }
        return false;

    }

    /**
     * 文件上传
     *
     * @param bucketName 存储捅名称
     * @param multipartFile 文件
     * @param filename   文件名
     */
    public void putObject(String bucketName, MultipartFile multipartFile, String filename) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        PutObjectOptions putObjectOptions = new PutObjectOptions(multipartFile.getSize(), PutObjectOptions.MIN_MULTIPART_SIZE);
        putObjectOptions.setContentType(multipartFile.getContentType());
        minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(filename).stream(
                                multipartFile.getInputStream(), multipartFile.getSize(), -1).contentType(multipartFile.getContentType())
                        .build());
    }

    /**
     * 通过InputStream上传对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param inputStream     要上传的流
     * @param contentType 上传的文件类型 例如 video/mp4  image/jpg
     * @return boolean
     */
    public boolean putObject(String bucketName, String objectName, InputStream inputStream,String contentType) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                            //不清楚文件的大小时，可以传-1，10485760。如果知道大小也可以传入size，partsize。
                            inputStream,  -1, 10485760)
                    .contentType(contentType)
                    .build());
            ObjectStat statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.length() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 以流的形式获取一个文件对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return InputStream
     */
    public InputStream getObject(String bucketName, String objectName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            ObjectStat statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.length() > 0) {
                InputStream stream = minioClient.getObject( GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
                return stream;
            }
        }
        return null;
    }

    /**
     * 以流的形式获取一个文件对象（断点下载）
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param offset     起始字节的位置
     * @param length     要读取的长度 (可选，如果无值则代表读到文件结尾)
     * @return InputStream
     */
    public InputStream getObject(String bucketName, String objectName, long offset, Long length) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            ObjectStat statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.length() > 0) {
                InputStream stream = minioClient.getObject(  GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .offset(1024L)
                        .length(4096L)
                        .build());
                return stream;
            }
        }
        return null;
    }

    /**
     * 下载并将文件保存到本地
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param fileName   File name
     * @return boolean
     */
    public boolean downloadObject(String bucketName, String objectName, String fileName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            ObjectStat statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.length() > 0) {
                minioClient.downloadObject(DownloadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .filename(fileName)
                        .build());
                return true;
            }
        }
        return false;
    }

    /**
     * 删除一个对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     */
    public boolean removeObject(String bucketName, String objectName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return true;
        }
        return false;
    }

    /**
     * 删除指定桶的多个文件对象,返回删除错误的对象列表，全部删除成功，返回空列表
     *
     * @param bucketName  存储桶名称
     * @param objectNames 含有要删除的多个object名称的迭代器对象
     * @return
     * eg:
     * List<DeleteObject> objects = new LinkedList<>();
     * objects.add(new DeleteObject("my-objectname1"));
     * objects.add(new DeleteObject("my-objectname2"));
     * objects.add(new DeleteObject("my-objectname3"));
     */
    public List<String> removeObjects(String bucketName, List<DeleteObject> objectNames) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        List<String> deleteErrorNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(objectNames).build());
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                deleteErrorNames.add(error.objectName());
            }
        }
        return deleteErrorNames;
    }

    /**
     * 生成一个给HTTP GET请求用的presigned URL。
     * 浏览器/移动端的客户端可以用这个URL进行下载，即使其所在的存储桶是私有的。这个presigned URL可以设置一个失效时间，默认值是7天。
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param expires    失效时间（以秒为单位），默认是7天，不得大于七天
     * @return
     */
    public String getPresignedObjectUrl(String bucketName, String objectName, Integer expires) throws InvalidExpiresRangeException, IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            if (expires < 1 || expires > DEFAULT_EXPIRY_TIME) {
                throw new InvalidExpiresRangeException(expires,
                        "expires must be in range of 1 to " + DEFAULT_EXPIRY_TIME);
            }
            try {
                url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(expires)//动态参数
                        //                       .expiry(24 * 60 * 60)//用秒来计算一天时间有效期
//                        .expiry(1, TimeUnit.DAYS)//按天传参
//                        .expiry(1, TimeUnit.HOURS)//按小时传参数
                        .build());
            } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidBucketNameException | InvalidExpiresRangeException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
                e.printStackTrace();
            }
        }
        return url;
    }

    /**
     * 生成一个给HTTP PUT请求用的presigned URL。
     * 浏览器/移动端的客户端可以用这个URL进行上传，即使其所在的存储桶是私有的。这个presigned URL可以设置一个失效时间，默认值是7天。
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param expires    失效时间（以秒为单位），默认是7天，不得大于七天
     * @return String
     */
    public String presignedPutObject(String bucketName, String objectName, Integer expires) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            if (expires < 1 || expires > DEFAULT_EXPIRY_TIME) {
                try {
                    throw new InvalidExpiresRangeException(expires,
                            "expires must be in range of 1 to " + DEFAULT_EXPIRY_TIME);
                } catch (InvalidExpiresRangeException e) {
                    e.printStackTrace();
                }
            }
            try {
                url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(expires)//动态参数
                        //                       .expiry(24 * 60 * 60)//用秒来计算一天时间有效期
//                        .expiry(1, TimeUnit.DAYS)//按天传参
//                        .expiry(1, TimeUnit.HOURS)//按小时传参数
                        .build());
            } catch (ErrorResponseException | InsufficientDataException e) {
                e.printStackTrace();
            } catch (InternalException e) {
                log.error("InternalException",e);
            } catch (InvalidBucketNameException e) {
                log.error("InvalidBucketNameException",e);
            } catch (InvalidExpiresRangeException e) {
                log.error("InvalidExpiresRangeException",e);
            } catch (InvalidKeyException e) {
                log.error("InvalidKeyException",e);
            } catch (InvalidResponseException e) {
                log.error("InvalidResponseException",e);
            } catch (IOException e) {
                log.error("IOException",e);
            } catch (NoSuchAlgorithmException e) {
                log.error("NoSuchAlgorithmException",e);
            } catch (ServerException e) {
                log.error("ServerException",e);
            } catch (XmlParserException e) {
                log.error("XmlParserException",e);
            }
        }
        return url;
    }

    /**
     * 获取对象的元数据
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return
     */
    public ObjectStat statObject(String bucketName, String objectName) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            ObjectStat statObject = null;
            try {
                statObject = minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
            } catch (ErrorResponseException e) {
                log.error("ErrorResponseException",e);
            } catch (InsufficientDataException e) {
                log.error("ErrorResponseException",e);
                e.printStackTrace();
            } catch (InternalException e) {
                log.error("InternalException",e);
            } catch (InvalidBucketNameException e) {
                log.error("InvalidBucketNameException",e);
            } catch (InvalidKeyException e) {
                log.error("InvalidKeyException",e);
            } catch (InvalidResponseException e) {
                log.error("InvalidResponseException",e);
            } catch (IOException e) {
                log.error("IOException",e);
            } catch (NoSuchAlgorithmException e) {
                log.error("NoSuchAlgorithmException",e);
            } catch (ServerException e) {
                log.error("ServerException",e);
            } catch (XmlParserException e) {
                log.error("XmlParserException",e);
            }
            return statObject;
        }
        return null;
    }

    /**
     * 文件访问路径
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return String
     */
    public String getObjectUrl(String bucketName, String objectName) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            try {
                url = minioClient.getObjectUrl(bucketName, objectName);
            } catch (ErrorResponseException e) {
                log.error("XmlParserException",e);
            } catch (InsufficientDataException e) {
                log.error("InsufficientDataException",e);
            } catch (InternalException e) {
                log.error("InternalException",e);
            } catch (InvalidBucketNameException e) {
                log.error("InvalidBucketNameException",e);
            } catch (InvalidKeyException e) {
                log.error("InvalidKeyException",e);
            } catch (InvalidResponseException e) {
                log.error("InvalidResponseException",e);
            } catch (IOException e) {
                log.error("IOException",e);
            } catch (NoSuchAlgorithmException e) {
                log.error("NoSuchAlgorithmException",e);
            } catch (ServerException e) {
                log.error("ServerException",e);
            } catch (XmlParserException e) {
                log.error("XmlParserException",e);
            }
        }
        return url;
    }



    public void downloadFile(String bucketName, String fileName, String originalName, HttpServletResponse response) {
        try {

            InputStream file = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
            String filename = new String(fileName.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            if (StringUtils.isNotEmpty(originalName)) {
                fileName = originalName;
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = file.read(buffer)) > 0) {
                servletOutputStream.write(buffer, 0, len);
            }
            servletOutputStream.flush();
            file.close();
            servletOutputStream.close();
        } catch (ErrorResponseException e) {
            log.error("ErrorResponseException",e);
        } catch (Exception e) {
            log.error("Exception",e);
        }
    }
}
 
