package com.springboot.eresourceaccesssystem.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.List;

@Document(indexName="documentelastic")
@Data
public class DocumentElastic {
    @Id
    private String id;
    private String title;
    private String content;
    private Double rating;

    public DocumentElastic(@JsonProperty("id") String id,
                           @JsonProperty("title") String title,
                           @JsonProperty("content") String content,
                           @JsonProperty("rating") Double rating) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Double getRating() {
        return rating;
    }
}