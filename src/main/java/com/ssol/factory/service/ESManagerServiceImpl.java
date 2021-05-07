package com.ssol.factory.service;

import com.ssol.factory.engine.ESClient;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            return esClient.createIndex(name, numShard, numReplica);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public JSONObject insert(JSONObject request) throws Exception{
        String name = request.get("name").toString();
        String data = (request.containsKey("data") || request.get("data") != null) ? request.get("data").toString() : null;
        if(data == null) {
            try {
                return esClient.insertData(name);
            } catch (Exception ex) {
                throw ex;
            }
        } else {
            try {
                return esClient.insertData(name, data);
            } catch (Exception ex) {
                throw ex;
            }
        }
    }

    @Override
    public JSONObject search(JSONObject request) throws Exception{
        String keyword = request.get("keyword").toString();
        return esClient.search(keyword);
    }
}
