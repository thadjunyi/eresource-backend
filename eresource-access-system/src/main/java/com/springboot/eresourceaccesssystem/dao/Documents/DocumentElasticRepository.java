package com.springboot.eresourceaccesssystem.dao.Documents;

import com.springboot.eresourceaccesssystem.model.DocumentElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface DocumentElasticRepository extends ElasticsearchRepository<DocumentElastic, String> {
    List<DocumentElastic> findByTitle(String title);
    List<DocumentElastic> findByContent(String title);
    List<DocumentElastic> findByRating(String title);
}
