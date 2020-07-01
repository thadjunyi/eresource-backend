package com.springboot.eresourceaccesssystem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document(collection = "Video")
public class Video {
    @Id
    private final String id;
    @NotBlank
    @Indexed(direction = IndexDirection.ASCENDING)
    private final String title;
    @NotBlank
    private final String fileType;
    @NotBlank
    private final String link;

    public Video(String id, @NotBlank String title, @NotBlank String fileType, String link) {
        this.id = id;
        this.title = title;
        this.fileType = fileType;
        this.link = "http://localhost:8080/api/video/getVideo/" + id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() { return title; }

    public String getFileType() { return fileType; }

    public String getLink() { return link; }
}
