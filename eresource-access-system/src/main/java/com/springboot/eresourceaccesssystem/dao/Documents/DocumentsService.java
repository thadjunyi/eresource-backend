package com.springboot.eresourceaccesssystem.dao.Documents;

import com.springboot.eresourceaccesssystem.model.Comments;
import com.springboot.eresourceaccesssystem.model.DocumentElastic;
import com.springboot.eresourceaccesssystem.model.DocumentMongo;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Repository("mongoAndElastic")
public class DocumentsService implements DocumentDao {
    private DocumentMongoRepository documentsMongoRepository;
    private DocumentElasticRepository documentElasticRepository;
    private RestHighLevelClient client;

    @Value("${elastic.hostname}")
    String elasticHostName;

    @Value("${elastic.port}")
    int elasticPort;

    public DocumentsService(DocumentMongoRepository documentsMongoRepository, DocumentElasticRepository documentElasticRepository, @Value("${elastic.hostname}") String elasticHostName, @Value("${elastic.port}") int elasticPort, @Value("${elastic.scheme}") String elasticScheme) {
        this.documentsMongoRepository = documentsMongoRepository;
        this.documentElasticRepository = documentElasticRepository;
        this.client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(elasticHostName, elasticPort, elasticScheme)
                ));
    }

    @Override
    public boolean createIndex(String indexName) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 1)
        );
        try {
            client.indices().create(request, RequestOptions.DEFAULT);
            return true;
        } catch (ElasticsearchStatusException e) {
            if (e.getMessage().contains("resource_already_exists_exception")) {
                throw new RuntimeException("index already exists");
            }
            if (!e.getMessage().contains("resource_already_exists_exception")) {
                throw e;
            }
        }
        return false;
    }

    @Override
    public boolean checkIndex(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        return exists;
    }

    @Override
    public boolean deleteIndex(String indexName) throws IOException {
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            AcknowledgedResponse deleteIndexResponse = client.indices().delete(request, RequestOptions.DEFAULT);

            boolean acknowledged = deleteIndexResponse.isAcknowledged();
            return acknowledged;

        } catch (ElasticsearchException exception) {
            if (exception.status() == RestStatus.NOT_FOUND) {
                exception.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public int insertDocuments(String id, DocumentMongo documents) {
        DocumentMongo documentMongo = new DocumentMongo(id, documents.getTitle(), documents.getContent(), null, 0.0);
        this.documentsMongoRepository.insert(documentMongo);
        this.documentElasticRepository.save(new DocumentElastic(id, documentMongo.getTitle(), documentMongo.getContent(), documentMongo.getRating()));
        return 1;
    }

    @Override
    public List<DocumentMongo> getAllDocuments() {
        List<DocumentMongo> documentMongoDB = this.documentsMongoRepository.findAll();
        for (DocumentMongo document : documentMongoDB) {
            if (document.getContent().length() > 300 ) {
                document.setContent(document.getContent().substring(0, 300) + "...");
            }
        }
        return documentMongoDB;
    }

    @Override
    public List<DocumentElastic> getAllDocumentsElastic() {
        List<DocumentElastic> documentElastic = new ArrayList<>();
        for (DocumentElastic documents : this.documentElasticRepository.findAll()) {
            documentElastic.add(documents);
        }
        return documentElastic;
    }

    @Override
    public Optional<DocumentMongo> findDocumentsById(String id) {
        Optional<DocumentMongo> documentMongo = this.documentsMongoRepository.findById(id);
        return documentMongo;
    }

    @Override
    public List<Optional<DocumentMongo>> findDocuments(Optional<String> id, Optional<String> title, Optional<String> content) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("documentelastic");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        if (id != null && !id.isEmpty()) {
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("id", id.get());
            searchSourceBuilder.query(matchQueryBuilder);
        }
        if (title != null && !title.isEmpty()) {
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("title", title.get());
            searchSourceBuilder.query(matchQueryBuilder);
        }
        if (content != null && !content.isEmpty()) {
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("content", content.get());
            searchSourceBuilder.query(matchQueryBuilder);
        }
//        searchSourceBuilder.sort(new FieldSortBuilder("_id").order(SortOrder.ASC));
        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        List<Optional<DocumentMongo>> documentMongos = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            String resultId = hit.getId();
            Optional<DocumentMongo> documentMongo = findDocumentsById(resultId);
            String limitedContent = documentMongo.get().getContent();
            if (limitedContent.length() > 300) {
                limitedContent = limitedContent.substring(0, 300) + "...";
                documentMongo.get().setContent(limitedContent);
            }
            documentMongos.add(documentMongo);
        }
        return documentMongos;
    }

    @Override
    public int deleteDocumentsById(String id) {
        this.documentsMongoRepository.deleteById(id);
        this.documentElasticRepository.deleteById(id);
        return 1;
    }

    @Override
    public int deleteAllDocuments() {
        this.documentsMongoRepository.deleteAll();
        this.documentElasticRepository.deleteAll();
        return 1;
    }

    @Override
    public int updateDocumentsById(String id, DocumentMongo documents) {
        Optional<DocumentElastic> documentElasticFound = this.documentElasticRepository.findById(id);
        if (!documentElasticFound.isEmpty()) {
            Optional<DocumentMongo> documentMongoFound = this.documentsMongoRepository.findById(id);
            documentMongoFound.map(oldDocumentMongo -> {
                String title = documents.getTitle();
                String content = documents.getContent();
                if (title == null || title.isEmpty()) {
                    title = oldDocumentMongo.getTitle();
                }
                if (content == null || content.isEmpty()) {
                    content = oldDocumentMongo.getContent();
                }
                DocumentMongo updatedDocumentMongo = new DocumentMongo(
                        oldDocumentMongo.getId(),
                        title,
                        content,
                        oldDocumentMongo.getComments(),
                        oldDocumentMongo.getRating()
                );
                this.documentsMongoRepository.save(updatedDocumentMongo);
                this.documentElasticRepository.save(
                        new DocumentElastic(
                                updatedDocumentMongo.getId(),
                                updatedDocumentMongo.getTitle(),
                                updatedDocumentMongo.getContent(),
                                updatedDocumentMongo.getRating()
                        ));
                return 1;
            });
        }
        return 0;
    }

    @Override
    public int addComment(String id, Comments comment) {
        Optional<DocumentElastic> documentElasticFound = this.documentElasticRepository.findById(id);
        if (!documentElasticFound.isEmpty()) {
            Optional<DocumentMongo> documentMongoFound = this.documentsMongoRepository.findById(id);
            documentMongoFound.map(oldDocumentMongo -> {
                List<Comments> comments = oldDocumentMongo.getComments();
                comments.add(comment);
                DocumentMongo updatedDocumentMongo = new DocumentMongo(
                        oldDocumentMongo.getId(),
                        oldDocumentMongo.getTitle(),
                        oldDocumentMongo.getContent(),
                        comments,
                        oldDocumentMongo.getRating());
                this.documentsMongoRepository.save(updatedDocumentMongo);
                this.documentElasticRepository.save(new DocumentElastic(
                        updatedDocumentMongo.getId(),
                        updatedDocumentMongo.getTitle(),
                        updatedDocumentMongo.getContent(),
                        updatedDocumentMongo.getRating())
                );
                return 1;
            });
        }
        return 0;
    }
}
