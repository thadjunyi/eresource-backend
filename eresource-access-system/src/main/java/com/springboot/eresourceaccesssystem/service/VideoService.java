package com.springboot.eresourceaccesssystem.service;

import com.springboot.eresourceaccesssystem.dao.Minio.MinioDao;
import com.springboot.eresourceaccesssystem.model.Video;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class VideoService {
    private MinioDao minioDao;

    @Value("${minio.video.bucket.name}")
    String videoBucket;

    @Autowired
    public VideoService(@Qualifier("minio") MinioDao minioDao) {
        this.minioDao = minioDao;
    }

    public int createBucket() throws IOException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidResponseException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        return minioDao.createBucket(videoBucket);
    }

    public int testBucket() throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        return minioDao.testBucket(videoBucket);
    }

    public List<String> getAllBucket() throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        return minioDao.getAllBucket();
    }

    public Map<String, String> upload(MultipartFile files) throws IOException {
        return minioDao.upload(files);
    }

    public List<Optional<Video>> getVideoDetail(String id) {
        return minioDao.getVideoDetail(id);
    }

    public InputStream getVideoFile(String id) throws IOException {
        return minioDao.getVideoFile(id);
    }

    public List<Video> getAllVideo() throws InsufficientDataException, NoSuchAlgorithmException, IOException, InternalException, ErrorResponseException, InvalidBucketNameException, XmlParserException, InvalidKeyException {
        return minioDao.getAllVideo();
    }
}
