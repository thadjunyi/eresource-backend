package com.springboot.eresourceaccesssystem.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Comments {
    private String comment;
    private Double rating;

    public Comments(@JsonProperty("comment") String comment,
                    @JsonProperty("rating") Double rating) {
        this.comment = comment;
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public Double getRating() {
        return rating;
    }
}
