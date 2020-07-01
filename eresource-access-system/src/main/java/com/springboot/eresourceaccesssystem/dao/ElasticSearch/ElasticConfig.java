package com.springboot.eresourceaccesssystem.dao.ElasticSearch;

import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

@Configuration
public class ElasticConfig {

    @Value("${spring.data.elasticsearch.cluster-nodes}")
    String elasticUrl;

    public RestHighLevelClient generateElasticClient() {
        try {
            ClientConfiguration clientConfiguration = ClientConfiguration.builder().connectedTo(elasticUrl).build();
            RestHighLevelClient client = RestClients.create(clientConfiguration).rest();
            return client;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
