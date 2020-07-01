package com.springboot.eresourceaccesssystem.service;

import com.springboot.eresourceaccesssystem.dao.Minio.MinioDao;
import com.springboot.eresourceaccesssystem.model.Audio;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AudioService {
    private MinioDao minioDao;

    @Value("${minio.audio.bucket.name}")
    String audioBucket;

    @Autowired
    public AudioService(@Qualifier("minio") MinioDao minioDao) {
        this.minioDao = minioDao;
    }

    public int createBucket() throws IOException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidResponseException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        return minioDao.createBucket(audioBucket);
    }

    public int testBucket() throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        return minioDao.testBucket(audioBucket);
    }

    public List<String> getAllBucket() throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        return minioDao.getAllBucket();
    }

    public Map<String, String> upload(MultipartFile files) throws IOException {
        return minioDao.upload(files);
    }

    public List<Optional<Audio>> getAudioDetail(String id) {
        return minioDao.getAudioDetail(id);
    }

    public InputStream getAudioFile(String id) throws IOException {
        return minioDao.getAudioFile(id);
    }

//    public File getAudioFile(String id) throws IOException {
//        return minioDao.getAudioFile(id);
//    }

    public List<Audio> getAllAudio() throws InsufficientDataException, NoSuchAlgorithmException, IOException, InternalException, ErrorResponseException, InvalidBucketNameException, XmlParserException, InvalidKeyException {
        return minioDao.getAllAudio();
    }
}
