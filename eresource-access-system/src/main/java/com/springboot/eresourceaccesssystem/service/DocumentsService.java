package com.springboot.eresourceaccesssystem.service;

import com.springboot.eresourceaccesssystem.dao.Documents.DocumentDao;
import com.springboot.eresourceaccesssystem.model.Comments;
import com.springboot.eresourceaccesssystem.model.DocumentElastic;
import com.springboot.eresourceaccesssystem.model.DocumentMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentsService {
    private DocumentDao documentsDao;

    @Autowired
    public DocumentsService(@Qualifier("mongoAndElastic") DocumentDao documentsDao) {
        this.documentsDao = documentsDao;
    }

    public boolean createIndex(String indexName) throws IOException { return documentsDao.createIndex(indexName); }

    public boolean checkIndex(String indexName) throws IOException { return documentsDao.checkIndex(indexName); }

    public boolean deleteIndex(String indexName) throws IOException { return documentsDao.deleteIndex(indexName); }

    public int insertDocuments(DocumentMongo documents) {
        return documentsDao.insertDocuments(documents);
    }

    public List<DocumentMongo> getAllDocuments() {
        return documentsDao.getAllDocuments();
    }

    public List<DocumentElastic> getAllDocumentsElastic() {
        return documentsDao.getAllDocumentsElastic();
    }

    public Optional<DocumentMongo> findDocumentsById(String id) {
        return documentsDao.findDocumentsById(id);
    }

    public List<Optional<DocumentMongo>> findDocuments(Optional<String> id, Optional<String> title, Optional<String> content) throws IOException {
        return documentsDao.findDocuments(id, title, content);
    }

    public int deleteDocumentsById(String id) {
        return documentsDao.deleteDocumentsById(id);
    }

    public int deleteAllDocuments() {
        return documentsDao.deleteAllDocuments();
    }

    public int updateDocumentsById(String id, DocumentMongo documents) {
        return documentsDao.updateDocumentsById(id, documents);
    }

    public int addComment(String id, Comments comment) { return documentsDao.addComment(id, comment); }
}
