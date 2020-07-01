package com.springboot.eresourceaccesssystem.dao.Minio;

import com.springboot.eresourceaccesssystem.model.Audio;
import com.springboot.eresourceaccesssystem.model.Video;
import io.minio.errors.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public interface MinioDao {
    int createBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidResponseException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException, RegionConflictException;

    int testBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidResponseException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException;

    List<String> getAllBucket() throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidResponseException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException;

    Optional<Audio> findAudioById(String id);

    Optional<Video> findVideoById(String id);

    int deleteAudioById(String id);

    int deleteAllAudio();

    int deleteVideoById(String id);

    int deleteAllVideo();

    Map<String, String> upload(MultipartFile files) throws IOException;

    String getBucketName(MultipartFile files);

    int insertMinio(String id, String fileName, String fileType, String bucketName);

    default int insertMinio(String fileName, String fileType, String bucketName) {
        String id = UUID.randomUUID().toString();
        return insertMinio(id, fileName, fileType, bucketName);
    }

    List<Optional<Audio>> getAudioDetail(String id);

    InputStream getAudioFile(String id) throws IOException;

//    File getAudioFile(String id) throws IOException;

    List<Audio> getAllAudio() throws XmlParserException, InsufficientDataException, NoSuchAlgorithmException, IOException, InternalException, InvalidKeyException, InvalidBucketNameException, ErrorResponseException;

    List<Optional<Video>> getVideoDetail(String id);

    InputStream getVideoFile(String id) throws IOException;

    List<Video> getAllVideo() throws XmlParserException, InsufficientDataException, NoSuchAlgorithmException, IOException, InternalException, InvalidKeyException, InvalidBucketNameException, ErrorResponseException;
}
