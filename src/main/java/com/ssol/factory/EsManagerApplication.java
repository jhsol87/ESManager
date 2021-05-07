package com.ssol.factory;

import com.ssol.factory.engine.ESClient;
import org.json.simple.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EsManagerApplication {

	private ESClient esClient;

	public EsManagerApplication(ESClient client) {
		this.esClient = client;
	}

	public static void main(String[] args) {
		SpringApplication.run(EsManagerApplication.class, args);
	}

}
