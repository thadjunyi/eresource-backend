package com.springboot.eresourceaccesssystem.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Document(collection = "Documents")
public class DocumentMongo {
    @Id
    private final String id;
    @NotBlank
    @Indexed(direction = IndexDirection.ASCENDING)
    private final String title;
    @NotBlank
    private String content;
    private final List<Comments> comments;
    private final Double rating;

    public DocumentMongo(@JsonProperty("id") String id,
                         @JsonProperty("title") String title,
                         @JsonProperty("content") String content,
                         @JsonProperty("comments") List<Comments> comments,
                         @JsonProperty("rating") Double rating) {
        this.id = id;
        this.title = title;
        this.content = content;

        Double sum = 0.0;
        if (comments != null && !comments.isEmpty()) {
            for (int i=0; i<comments.size(); i++) {
                sum += comments.get(i).getRating();
            }
            sum /= comments.size();
            this.comments = comments;
            this.rating = sum;
        }
        else {
            this.comments = new ArrayList<>();
            this.rating = rating;
        }

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

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public Double getRating() {
        return rating;
    }
}
