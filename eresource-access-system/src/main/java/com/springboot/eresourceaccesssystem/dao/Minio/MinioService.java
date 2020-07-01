package com.springboot.eresourceaccesssystem.dao.Minio;

import com.springboot.eresourceaccesssystem.model.Audio;
import com.springboot.eresourceaccesssystem.model.Video;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@Repository("minio")
public class MinioService implements MinioDao {
    @Autowired
    MinioClient minioClient;

    @Value("${minio.url}")
    String minioUrl;

    @Value("${minio.audio.bucket.name}")
    String audioBucket;

    @Value("${minio.video.bucket.name}")
    String videoBucket;

    private AudioMongoRepository audioMongoRepository;
    private VideoMongoRepository videoMongoRepository;

    public MinioService(AudioMongoRepository audioMongoRepository, VideoMongoRepository videoMongoRepository) {
        this.audioMongoRepository = audioMongoRepository;
        this.videoMongoRepository = videoMongoRepository;
    }

    @Override
    public int createBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidResponseException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException, RegionConflictException {
        boolean isExist = minioClient.bucketExists(bucketName);
        if (isExist) {
            System.out.println("Bucket " + bucketName + " already exists.");
        }
        else {
            minioClient.makeBucket(bucketName);
            System.out.println("Created bucket " + bucketName + ".");
        }
        return 1;
    }

    @Override
    public int testBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidResponseException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        if (minioClient.bucketExists(bucketName)) {
            return 1;
        }
        return 0;
    }

    @Override
    public List<String> getAllBucket() throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidResponseException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        List<String> bucketNames = new ArrayList<>();
        minioClient.listBuckets().forEach(bucket -> bucketNames.add(bucket.name()));
        return bucketNames;
    }

    @Override
    public Optional<Audio> findAudioById(String id) {
        Optional<Audio> audio = this.audioMongoRepository.findById(id);
        return audio;
    }

    @Override
    public Optional<Video> findVideoById(String id) {
        Optional<Video> video = this.videoMongoRepository.findById(id);
        return video;
    }

    @Override
    public int deleteAudioById(String id) {
        this.audioMongoRepository.deleteById(id);
        return 1;
    }

    @Override
    public int deleteAllAudio() {
        this.audioMongoRepository.deleteAll();
        return 1;    }

    @Override
    public int deleteVideoById(String id) {
        this.videoMongoRepository.deleteById(id);
        return 1;
    }

    @Override
    public int deleteAllVideo() {
        this.videoMongoRepository.deleteAll();
        return 1;    }

    @Override
    public Map<String, String> upload(MultipartFile files)  throws IOException {
        String fileName = files.getOriginalFilename().split("\\.")[0];
        byte[] content = files.getBytes();
        String fileType = "." + files.getContentType().split("/")[1];
        String bucketName = getBucketName(files);

        try {
            Path tempFile = Files.createTempFile(fileName, fileType);

            try (InputStream in = new ByteArrayInputStream(content)) {
                Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            minioClient.putObject(bucketName,
                    fileName + fileType,
                    tempFile.toString(),
                    new PutObjectOptions(Files.size(tempFile), -1));

            Files.delete(tempFile);

            int result = insertMinio(fileName, fileType, bucketName);
            //System.out.println(result);

        } catch (MinioException e) {
            throw new IllegalStateException("The file cannot be upload on the internal storage. Please retry later", e);
        } catch (IOException e) {
            throw new IllegalStateException("The file cannot be read", e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        Map<String, String> result = new HashMap<>();
        result.put("key", files.getOriginalFilename());

        return result;
    }

    @Override
    public String getBucketName(MultipartFile files) {
        String fileType = files.getContentType().split("/")[1];
        //System.out.println(fileType);
        switch (fileType) {
            case "mpeg":
                return audioBucket;
            case "mp4":
                return videoBucket;
        }
        return null;
    }

    @Override
    public int insertMinio(String id, String fileName, String fileType, String bucketName) {
        switch(bucketName) {
            case "audiobucket":
                audioMongoRepository.insert(new Audio(id, fileName, fileType, null));
                return 1;
            case "videobucket":
                videoMongoRepository.insert(new Video(UUID.randomUUID().toString(), fileName, fileType, null));
                return 1;
        }
        return 0;
    }

    //Not advisable: Slow in getting response as it copy the byte from the file.
//    @Override
//    public File getAudioFile(String id) throws IOException {
//        Optional<Audio> audio = findAudioById(id);
//        try {
//            if (audio != null) {
//                String fileName = audio.get().getTitle();
//                String fileType = audio.get().getFileType();
//                InputStream inputFile = minioClient.getObject(audioBucket, fileName + fileType);
//
//                fileType = fileType.replace(".", "");
//                final File tempFile = File.createTempFile(fileName, fileType);
//                tempFile.deleteOnExit();
//
//                try (FileOutputStream outputFile = new FileOutputStream(tempFile)) {
//                    IOUtils.copy(inputFile, outputFile);
//                }
//                return tempFile;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("Error in getAudioFile(). Could not find audio.");
//        final File emptyFile = File.createTempFile("Empty", "mp3");
//        emptyFile.deleteOnExit();
//        return emptyFile;
//    }

    @Override
    public List<Optional<Audio>> getAudioDetail(String id) {
        List<Optional<Audio>> list = new ArrayList<>();
        Optional<Audio> audio = findAudioById(id);
        list.add(audio);
        return list;
    }

    @Override
    public InputStream getAudioFile(String id) throws IOException {
        Optional<Audio> audio = findAudioById(id);
        try {
            if (audio != null) {
                String fileName = audio.get().getTitle();
                String fileType = audio.get().getFileType();
                InputStream inputFile = minioClient.getObject(audioBucket, fileName + fileType);

                return inputFile;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Error in getAudioFile(). Could not find audio.");
        return InputStream.nullInputStream();
    }

    @Override
    public List<Audio> getAllAudio() throws XmlParserException, InsufficientDataException, NoSuchAlgorithmException, IOException, InternalException, InvalidKeyException, InvalidBucketNameException, ErrorResponseException {
        Sort sort = Sort.by("title").ascending();
        List<Audio> audio = audioMongoRepository.findAll(sort);
        return audio;
    }

    @Override
    public List<Optional<Video>> getVideoDetail(String id) {
        List<Optional<Video>> list = new ArrayList<>();
        Optional<Video> video = findVideoById(id);
        list.add(video);
        return list;
    }

    @Override
    public InputStream getVideoFile(String id) throws IOException {
        Optional<Video> video = findVideoById(id);
        try {
            if (video != null) {
                String fileName = video.get().getTitle();
                String fileType = video.get().getFileType();
                InputStream inputFile = minioClient.getObject(videoBucket, fileName + fileType);

                return inputFile;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Error in getVideoFile(). Could not find video.");
        return InputStream.nullInputStream();
    }

    @Override
    public List<Video> getAllVideo() throws XmlParserException, InsufficientDataException, NoSuchAlgorithmException, IOException, InternalException, InvalidKeyException, InvalidBucketNameException, ErrorResponseException {
//        Iterable<Result<Item>> itemList = minioClient.listObjects(videoBucket);
//        List<String> result = new ArrayList<>();
//
//        for (Result<Item> items : itemList) {
//            Item item = items.get();
//            result.add(item.objectName());
//        }
//        return result;
        Sort sort = Sort.by("title").ascending();
        List<Video> video = videoMongoRepository.findAll(sort);
        return video;
    }
}
