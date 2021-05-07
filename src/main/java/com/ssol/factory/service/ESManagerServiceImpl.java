package com.ssol.factory.service;

import com.ssol.factory.engine.ESClient;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ESManagerServiceImpl implements ESManagerService {

    @Autowired
    private ESClient esClient;

    @Override
    public JSONObject create(JSONObject request) throws Exception{
        String name = request.get("name").toString();
        int numShard = (int) request.get("numShard");
        int numReplica = (int) request.get("numReplica");
        try {
            esClient.createIndex(name, numShard, numReplica);
            JSONObject success = new JSONObject();
            success.put("message", "ok");
            return success;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public JSONObject insert(JSONObject request) throws Exception{
        String name = request.get("name").toString();
        try {
            esClient.insertData("product");
            JSONObject success = new JSONObject();
            success.put("message", "ok");
            return success;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public JSONObject search(JSONObject request) throws Exception{
        String keyword = request.get("keyword").toString();
        return esClient.search(keyword);
    }
}
