package com.bigdata101.bigdata101.service;

import com.bigdata101.bigdata101.model.Article;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Silas on 4/17/2018.
 */

public class ApiService {

    private static final String baseUrl = "localhost:3000/api/";
    private static final String articleDescriptionEndpoint = "http://localhost:3000/api/articleTitles/";

    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();

    Article[] articles;

    public String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public Article[] getArticlesDescription (){


        Request request = new Request.Builder()
                .url(articleDescriptionEndpoint)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                articles = gson.fromJson(response.body().string(), Article[].class);


            }
        });




        return articles;
    }


}
