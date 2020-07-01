package com.springboot.eresourceaccesssystem.apicontroller;

import java.io.File;
import java.io.IOException;

import com.springboot.eresourceaccesssystem.model.Audio;
import com.springboot.eresourceaccesssystem.service.AudioService;
import io.minio.errors.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/audio")
public class AudioController {
    private final AudioService audioService;

    @Autowired
    public AudioController(AudioService audioService) {
        this.audioService = audioService;
    }

    @PostMapping("/createbucket")
    public int createBucket() throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, ErrorResponseException, XmlParserException, InternalException, InvalidBucketNameException, InsufficientDataException, RegionConflictException {
        return audioService.createBucket();
    }

    @GetMapping("/testbucket")
    public int testBucket() throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        return audioService.testBucket();
    }

    @GetMapping("/allbucket")
    public List<String> getAllBucket() throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, InternalException, XmlParserException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        return audioService.getAllBucket();
    }

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Map<String, String> upload(@RequestPart(value = "file", required = false) MultipartFile files) throws IOException {
        return audioService.upload(files);
    }

//    @GetMapping(path = "/get/{id}", produces = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResponseEntity<Resource> getAudioFile(@PathVariable("id") String id) throws IOException {
//        File file = audioService.getAudioFile(id);
//        Resource fileSystemResource = new FileSystemResource(file);
//        return ResponseEntity.ok()
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(fileSystemResource);
//    }

    @GetMapping("/get/{id}")
    public List<Optional<Audio>> getAudioDetail(@PathVariable("id") String id) {
        return audioService.getAudioDetail(id);
    }

    @GetMapping("/getVideo/{id}")
    public void getAudioFile(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        InputStream file = audioService.getAudioFile(id);

        response.addHeader("Content-disposition", "attachment;filename=" + id);
//        response.setContentType(URLConnection.guessContentTypeFromName("Audio1.mpeg"));
        response.setContentType("mpeg");

        IOUtils.copy(file, response.getOutputStream());
        response.flushBuffer();
    }

    @GetMapping("/getAll")
    public List<Audio> getAllAudio() throws InsufficientDataException, NoSuchAlgorithmException, IOException, InternalException, InvalidKeyException, InvalidBucketNameException, XmlParserException, ErrorResponseException {
        return audioService.getAllAudio();
    }
}
