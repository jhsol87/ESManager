package com.ssol.factory.service;

import org.json.simple.JSONObject;

public interface ESManagerService {

    JSONObject create(JSONObject request) throws Exception;
    JSONObject insert(JSONObject request) throws Exception;
    JSONObject search(JSONObject request) throws Exception;
}
