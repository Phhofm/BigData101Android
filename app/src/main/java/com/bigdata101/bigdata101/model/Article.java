package com.bigdata101.bigdata101.model;

/**
 * Created by Silas on 4/17/2018.
 */

public class Article {

    private String id;
    private String title;
    private String article;
    private String author;
    private long requestTime;
    private String category;

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArticle() {
        return article;
    }

    public String getAuthor() {
        return author;
    }

    public long getRequestTime() {
        return requestTime;
    }
}
