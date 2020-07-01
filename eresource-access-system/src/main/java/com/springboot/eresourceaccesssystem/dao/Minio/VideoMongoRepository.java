package com.springboot.eresourceaccesssystem.dao.Minio;

import com.springboot.eresourceaccesssystem.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoMongoRepository extends MongoRepository<Video, String> {
    Optional<Video> findById(String id);
}