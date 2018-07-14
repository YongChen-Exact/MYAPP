package com.example.a24270.myapp;

/**
 * Created by 24270 on 2018/5/26.
 */

public class App
{
    private String imagePath;
    private String title;
    private String url;
    public  App(String title,String url,String imagePath) {
        this.title = title;
        this.imagePath = imagePath;
        this.url = url;
    }
    public String getTitle(){
        return title;
    }
    public String getImagePath(){
        return imagePath;
    }
    public String getUrl(){
        return url;
    }
}