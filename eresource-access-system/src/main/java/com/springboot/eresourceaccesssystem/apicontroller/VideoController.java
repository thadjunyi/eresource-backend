package com.springboot.eresourceaccesssystem.apicontroller;

import com.springboot.eresourceaccesssystem.model.Video;
import com.springboot.eresourceaccesssystem.service.VideoService;
import io.minio.errors.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/video")
public class VideoController {
    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/createbucket")
    public int createBucket() throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, ErrorResponseException, XmlParserException, InternalException, InvalidBucketNameException, InsufficientDataException, RegionConflictException {
        return videoService.createBucket();
    }

    @GetMapping("/testbucket")
    public int testBucket() throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        return videoService.testBucket();
    }

    @GetMapping("/allbucket")
    public List<String> getAllBucket() throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        return videoService.getAllBucket();
    }

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Map<String, String> upload(@RequestPart(value = "file", required = false) MultipartFile files) throws IOException {
        return videoService.upload(files);
    }

    @GetMapping("/get/{id}")
    public List<Optional<Video>> getVideoDetail(@PathVariable("id") String id) {
        return videoService.getVideoDetail(id);
    }

    @GetMapping("/getVideo/{id}")
    public void getVideoFile(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        InputStream file = videoService.getVideoFile(id);

        response.addHeader("Content-disposition", "attachment;filename=" + id);
        response.setContentType("mp4");

        IOUtils.copy(file, response.getOutputStream());
        response.flushBuffer();
    }

    @GetMapping("/getAll")
    public List<Video> getAllVideo() throws InsufficientDataException, NoSuchAlgorithmException, IOException, InternalException, InvalidKeyException, InvalidBucketNameException, XmlParserException, ErrorResponseException {
        return videoService.getAllVideo();
    }
}
