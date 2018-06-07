package com.example.admin.broadcastwithservice;

/**
 * Created by admin on 5/4/2017.
 */

public class Source {
    String id;
    String name;
    String url;
    String category;

    public Source(String id, String name, String url, String category){
        this.id=id;
        this.name=name;
        this.url=url;
        this.category=category;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getUrl(){
        return url;
    }

    public String getCategory(){
        return category;
    }

    @Override
    public String toString(){
        return id+" "+name+" "+url+" "+category;
    }

}
