package com.ssol.factory.engine;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class ESClient {

    private String[] sample = new String[] { "나이키", "나이키 골프", "운동화 나이키", "브랜드 나이키", "반다나" };

    private RestHighLevelClient client;

    public ESClient() {
        this.client = init("localhost");
    }

    private RestHighLevelClient init(String host) {
        RestClientBuilder builder = RestClient.builder(
            new HttpHost(host, 9200, "http")
        ).setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder.setSocketTimeout(10000));
        return new RestHighLevelClient(builder);
    }

    public void close() {
        try {
            this.client.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public JSONObject createIndex(String indexName, int numShards, int numReplicas) throws Exception {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        if(!client.indices().exists(getIndexRequest, RequestOptions.DEFAULT)) {
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            request.settings(Settings.builder()
                    .put("index.number_of_shards", numShards)
                    .put("index.number_of_replicas", numReplicas)
            );

            Map<String, Object> name = new HashMap<>();
            name.put("type", "text");
            name.put("index", true);
            name.put("store", true);
            name.put("analyzer", "whitespace");
            Map<String, Object> properties = new HashMap<>();
            properties.put("name", name);
            Map<String, Object> mapping = new HashMap<>();
            mapping.put("properties", properties);
            request.mapping(mapping);

            request.alias(new Alias("productAlias"));

            CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
            if(!response.isAcknowledged()) throw new Exception("cannot create index");
            else {
                JSONObject success = new JSONObject();
                success.put("message", "complete to create index");
                return success;
            }
        } else throw new Exception("index already exists");
    }

    public JSONObject insertData(String indexName) throws Exception {
        BulkRequest request = new BulkRequest();
        for(String data : sample) {
            JSONObject obj = new JSONObject();
            obj.put("name", data);
            request.add(new IndexRequest(indexName).source(obj.toJSONString(), XContentType.JSON));
        }
        BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
        if(response.hasFailures()) throw new Exception("bulk data failed");
        else {
            JSONObject success = new JSONObject();
            success.put("message", "complete to insert 5 sample data");
            return success;
        }
    }

    public JSONObject insertData(String indexName, String data) throws Exception {
        IndexRequest request = new IndexRequest(indexName);
        JSONObject obj = new JSONObject();
        obj.put("name", data);
        request.source(obj.toJSONString(), XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        if(response.getShardInfo().getFailed() > 0) throw new Exception("insert data failed");
        else {
            JSONObject success = new JSONObject();
            success.put("message", "complete to insert 1 sample data");
            return success;
        }
    }

    public JSONObject search(String keyword) throws Exception {
        SearchRequest request = new SearchRequest("product");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.queryStringQuery("name:*" + keyword + "*"));
        request.source(builder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        if(hits.length == 0) throw new Exception("no result");
        JSONObject result = new JSONObject();
        result.put("took", response.getTook().getMillis());
        List<String> list = new ArrayList<>();
        for(SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            for(String key : map.keySet()) list.add(map.get(key).toString());
        }

        Comparator<String> c = (s1, s2) -> Integer.compare(s1.length(), s2.length());
        Collections.sort(list, c);

        result.put("result", list);
        return result;
    }

}
