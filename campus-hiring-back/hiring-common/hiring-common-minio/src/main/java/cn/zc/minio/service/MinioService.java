package cn.zc.minio.service;

import cn.zc.minio.domain.OSSResult;
import cn.zc.security.exception.ServiceException;
import cn.zc.common.core.enums.ResultCode;
import io.minio.*;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * MinIO 文件服务类
 * 封装文件上传、下载、删除等操作
 * 
 * @author campus-hiring-system
 */
@Slf4j
@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.endpoint}")
    private String endpoint;

    /**
     * 上传文件
     * 
     * @param file 文件对象
     * @param bucketName 存储桶名称
     * @return 文件上传结果
     */
    public OSSResult uploadFile(MultipartFile file, String bucketName) {
        return uploadFile(file, bucketName, null);
    }

    /**
     * 上传文件，可自定义对象名称
     *
     * @param file 文件对象
     * @param bucketName 存储桶名称
     * @param objectName 对象名称（可选）
     * @return 文件上传结果
     */
    public OSSResult uploadFile(MultipartFile file, String bucketName, String objectName) {
        InputStream inputStream = null;
        try {
            String fileName = StringUtils.hasText(objectName) ? objectName : file.getOriginalFilename();
            if (!StringUtils.hasText(fileName)) {
                fileName = "file_" + System.currentTimeMillis();
            }

            inputStream = file.getInputStream();
            return putFileToMinIO(inputStream, fileName, bucketName, file.getContentType());
        } catch (Exception e) {
            log.error("MinIO upload file error", e);
            throw new ServiceException(ResultCode.FAILED_FILE_UPLOAD);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.error("Close input stream error", e);
                }
            }
        }
    }

    /**
     * 上传文件到 MinIO
     * 
     * @param fileStream 文件流
     * @param fileName 文件名
     * @param bucketName 存储桶名称
     * @param contentType 文件类型
     * @return 文件上传结果
     */
    private OSSResult putFileToMinIO(InputStream fileStream, String fileName, String bucketName, String contentType) {
        try {
            // 检查桶是否存在，如果不存在则创建
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                // 设置桶策略为公共读
                String policyJsonString = "{\"version\":\"2012-10-17\",\"Statement\":[{\"Sid\":\"PublicRead\",\"Effect\":\"Allow\",\"Principal\":{\"AWS\":\"*\"},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}";
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                        .bucket(bucketName)
                        .config(policyJsonString)
                        .build());
            }

            // 上传文件到 MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(fileStream, fileStream.available(), -1)
                            .contentType(contentType != null ? contentType : "application/octet-stream")
                            .build()
            );
            
            // 构建文件URL
            String url = endpoint + "/" + bucketName + "/" + fileName;
            OSSResult ossResult = new OSSResult();
            ossResult.setName(url);
            ossResult.setSuccess(true);
            return ossResult;
        } catch (MinioException e) {
            log.error("MinIO error: {}", e.getMessage(), e);
            throw new ServiceException(ResultCode.FAILED_FILE_UPLOAD, "MinIO error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred during file upload", e);
            throw new ServiceException(ResultCode.FAILED_FILE_UPLOAD);
        }
    }

    /**
     * 下载文件
     * 
     * @param bucketName 存储桶名称
     * @param fileName 文件名
     * @return 文件流
     */
    public InputStream downloadFile(String bucketName, String fileName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            log.error("MinIO download file error: bucket={}, fileName={}", bucketName, fileName, e);
            throw new ServiceException(ResultCode.FAILED_FILE_UPLOAD, "文件下载失败");
        }
    }

    /**
     * 删除文件
     * 
     * @param bucketName 存储桶名称
     * @param fileName 文件名
     * @return true=删除成功，false=删除失败
     */
    public boolean deleteFile(String bucketName, String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
            return true;
        } catch (Exception e) {
            log.error("MinIO delete file error: bucket={}, fileName={}", bucketName, fileName, e);
            return false;
        }
    }

    /**
     * 获取文件URL
     * 
     * @param bucketName 存储桶名称
     * @param fileName 文件名
     * @return 文件URL
     */
    public String getFileUrl(String bucketName, String fileName) {
        return endpoint + "/" + bucketName + "/" + fileName;
    }
}

