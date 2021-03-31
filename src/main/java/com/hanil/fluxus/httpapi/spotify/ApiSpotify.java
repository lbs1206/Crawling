package com.hanil.fluxus.httpapi.spotify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;


public class ApiSpotify {


    public static String postKakaoApi(String url, String parameter) throws Exception {
        StringBuilder result = new StringBuilder();
        HttpClient httpClient = null;
        HttpPost http = new HttpPost(url);
        httpClient = HttpClientBuilder.create().build();
        http.setHeader("Authorization", "KakaoAK 2559c47fc0d89e2fef49661f335fa8b5");
        StringEntity param = new StringEntity(parameter, "UTF-8");
        param.setContentType("application/json");
        http.setEntity(param);
        http.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            HttpResponse response = httpClient.execute(http);

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            String line = null;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            int statusCode = response.getStatusLine().getStatusCode();
            if (200 != statusCode) {
                // throw new Exception("Failed : Http Status Code = "+statusCode+", Message
                // ="+Utility.jsonToMap(result.toString()).get("error"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            http.releaseConnection();
        }

        return result.toString();
    }

}
