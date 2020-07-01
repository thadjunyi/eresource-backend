package com.springboot.eresourceaccesssystem.dao.Documents;

import com.springboot.eresourceaccesssystem.model.DocumentMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentMongoRepository extends MongoRepository<DocumentMongo, String> {
    Optional<DocumentMongo> findById(String id);
}
