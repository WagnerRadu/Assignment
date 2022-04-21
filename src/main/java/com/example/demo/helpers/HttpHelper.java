package com.example.demo.helpers;

import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpRequest;

public class HttpHelper {
    public static Gson gson = new Gson();

    public static HttpRequest createGetRequest(String url) {
        HttpRequest getRequest = null;
        try {
            URL getUrl = new URL(url);
            getRequest = HttpRequest.newBuilder(getUrl.toURI()).build();
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }
        return getRequest;
    }

    public static HttpRequest createPostRequest(String url, Object body) {
        HttpRequest postRequest = null;
        String json = gson.toJson(body);
        try {
            URL postUrl = new URL(url);
            postRequest = HttpRequest.newBuilder(postUrl.toURI())
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }
        return postRequest;
    }
}
