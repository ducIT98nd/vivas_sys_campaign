package com.vivas.campaignxsync.utils;

import com.google.gson.Gson;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public String postHttp(String url, Object bodyRequest, int timeout) throws Exception {
        Gson gson = new Gson();
            RequestConfig config = RequestConfig
                    .custom()
                    .setConnectTimeout(timeout)
                    .build();
            CloseableHttpClient httpClient = HttpClientBuilder
                    .create()
                    .setDefaultRequestConfig(config)
                    .build();
            HttpPost httpPost = new HttpPost(url);
            String jsonBody = gson.toJson(bodyRequest);
            StringEntity entityReq = new StringEntity(jsonBody);
            httpPost.setEntity(entityReq);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
    }
}
