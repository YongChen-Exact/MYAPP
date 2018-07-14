package com.example.a24270.myapp;

public class Title_Collector {
    private String author;
    private String link;
    private String title;
    private int id;
    public Title_Collector(String author,String link,String title,int id){
        this.author = author;
        this.link = link;
        this.title = title;
        this.id = id;
    }
    public String getAuthor(){
        return author;
    }

    public String getLink() {
        return link;
    }

    public String getTitle(){
        return title;
    }
    public int getId(){return id;}
}

