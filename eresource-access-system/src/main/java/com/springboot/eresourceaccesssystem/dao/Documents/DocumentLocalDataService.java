package com.springboot.eresourceaccesssystem.dao.Documents;

import com.springboot.eresourceaccesssystem.model.Comments;
import com.springboot.eresourceaccesssystem.model.DocumentElastic;
import com.springboot.eresourceaccesssystem.model.DocumentMongo;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository("fakeDao")
public class DocumentLocalDataService implements DocumentDao {
    private static List<DocumentMongo> DB = new ArrayList();

    @Override
    public boolean createIndex(String indexName) throws IOException {
        return false;
    }

    @Override
    public boolean checkIndex(String indexName) throws IOException {
        return false;
    }

    @Override
    public boolean deleteIndex(String indexName) throws IOException {
        return false;
    }

    @Override
    public int insertDocuments(String id, DocumentMongo documents) {
        DB.add(new DocumentMongo(id, documents.getTitle(), documents.getContent(), documents.getComments(), 0.0));
        return 1;
    }

    @Override
    public int insertDocuments(DocumentMongo documents) {
        DB.add(new DocumentMongo(UUID.randomUUID().toString(), documents.getTitle(), documents.getContent(), documents.getComments(), 0.0));
        return 1;
    }

    public List<DocumentMongo> getAllDocuments() {
        return DB;
    }

    @Override
    public List<DocumentElastic> getAllDocumentsElastic() {
        return null;
    }

    @Override
    public Optional<DocumentMongo> findDocumentsById(String id) {
        return DB.
                stream()
                .filter(documents -> documents.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Optional<DocumentMongo>> findDocuments(Optional<String> id, Optional<String> title, Optional<String> content) throws IOException {
        return null;
    }

    @Override
    public int deleteDocumentsById(String id) {
        Optional<DocumentMongo> documents = findDocumentsById(id);
        if (documents.isEmpty()) {
            return 0;
        }
        DB.remove(documents.get());
        return 1;
    }

    @Override
    public int deleteAllDocuments() {
        DB.removeAll(DB);
        return 1;
    }

    @Override
    public int updateDocumentsById(String id, DocumentMongo documents) {
        return 0;
    }

    @Override
    public int addComment(String id, Comments comment) {
        return 0;
    }
}
