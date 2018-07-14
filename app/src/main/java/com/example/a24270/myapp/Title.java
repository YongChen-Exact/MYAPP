package com.example.a24270.myapp;

/**
 * Created by 24270 on 2018/5/30.
 */

public class Title {
    private String author;
    private String chapterName;
    private String niceDate;
    private String title;
    private String link;
    private int id;
    public Title(String author,String chapterName,String niceDate,String title,String link,int id){
        this.author = author;
        this.chapterName = chapterName;
        this.niceDate = niceDate;
        this.title = title;
        this.link = link;
        this.id = id;
    }
    public String getAuthor(){
        return author;
    }
    public String getChapterName(){
        return chapterName;
    }
    public String getNiceDate(){
        return niceDate;
    }
    public String getTitle(){
            return title;
    }
    public String getLink() {
                return link;
    }
    public int getId(){return id;}
}
