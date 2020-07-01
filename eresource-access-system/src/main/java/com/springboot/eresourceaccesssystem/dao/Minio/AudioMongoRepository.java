package com.springboot.eresourceaccesssystem.dao.Minio;

import com.springboot.eresourceaccesssystem.model.Audio;
import com.springboot.eresourceaccesssystem.model.DocumentMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AudioMongoRepository extends MongoRepository<Audio, String> {
    Optional<Audio> findById(String id);
    List<Audio> findAllOrderByTitle();
}
