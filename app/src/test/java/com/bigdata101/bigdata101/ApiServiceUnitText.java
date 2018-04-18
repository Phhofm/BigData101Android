package com.bigdata101.bigdata101;

import com.bigdata101.bigdata101.service.ApiService;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by Silas on 4/17/2018.
 */

public class ApiServiceUnitText {

    ApiService apiService = new ApiService();

    @Test
    public void getRequest(){
        try {
           System.out.print(apiService.run("http://www.google.ch"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void gson(){
        apiService.getArticlesDescription();
        System.out.println("hello");
    }
}
