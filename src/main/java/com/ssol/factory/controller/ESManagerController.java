package com.ssol.factory.controller;

import com.ssol.factory.service.ESManagerService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ESManagerController {

    @Autowired
    private ESManagerService service;

    @PostMapping(path = "/create")
    public JSONObject create(@RequestBody JSONObject request) {
        try {
            return service.create(request);
        } catch (Exception ex) {
            JSONObject error = new JSONObject();
            error.put("exception", ex.getMessage());
            return error;
        }
    }

    @PostMapping(path = "/insert")
    public JSONObject insert(@RequestBody JSONObject request) {
        try {
            return service.insert(request);
        } catch (Exception ex) {
            JSONObject error = new JSONObject();
            error.put("exception", ex.getMessage());
            return error;
        }
    }

    @PostMapping(path = "/search")
    public JSONObject search(@RequestBody JSONObject request) {
        try {
            return service.search(request);
        } catch (Exception ex) {
            JSONObject error = new JSONObject();
            error.put("exception", ex.getMessage());
            return error;
        }
    }
}
