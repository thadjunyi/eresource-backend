package com.springboot.eresourceaccesssystem.dao.Documents;

import com.springboot.eresourceaccesssystem.model.Comments;
import com.springboot.eresourceaccesssystem.model.DocumentElastic;
import com.springboot.eresourceaccesssystem.model.DocumentMongo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface DocumentDao {
    boolean createIndex(String indexName) throws IOException;

    boolean checkIndex(String indexName) throws IOException;

    boolean deleteIndex(String indexName) throws IOException;

    int insertDocuments(String id, DocumentMongo documents);

    default int insertDocuments(DocumentMongo documents) {
        String id = UUID.randomUUID().toString();
        return insertDocuments(id, documents);
    }

    List<DocumentMongo> getAllDocuments();

    List<DocumentElastic> getAllDocumentsElastic();

    Optional<DocumentMongo> findDocumentsById(String id);

    List<Optional<DocumentMongo>> findDocuments(Optional<String> id, Optional<String> title, Optional<String> content) throws IOException;

    int deleteDocumentsById(String id);

    int deleteAllDocuments();

    int updateDocumentsById(String id, DocumentMongo documents);

    int addComment(String id, Comments comment);
}
