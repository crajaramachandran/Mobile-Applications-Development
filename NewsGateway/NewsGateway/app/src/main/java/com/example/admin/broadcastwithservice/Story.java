package com.example.admin.broadcastwithservice;

import android.os.Parcel;
import android.os.Parcelable;

public class Story implements Parcelable {

    private String author;
    private String title;
    private String description;
    private String urlToImage;
    private String publishedAt;
    private String url;

    public Story(String author,String title,String description,String urlToImage,String publishedAt,String url){
        this.author=author;
        this.title=title;
        this.description=description;
        this.urlToImage=urlToImage;
        this.publishedAt=publishedAt;
        this.url=url;
    }

    protected Story(Parcel in) {
        author = in.readString();
        title = in.readString();
        description = in.readString();
        urlToImage = in.readString();
        publishedAt = in.readString();
        url=in.readString();
    }

    public static final Creator<Story> CREATOR = new Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel in) {
            return new Story(in);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };

    public String getAuthor(){
        return author;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getUrlToImage(){
        return urlToImage;
    }

    public String getPublishedAt(){
        return publishedAt;
    }

    public String getUrl(){return url;}

    public String toString(){
        return author+"  "+title+"  "+description+"  "+urlToImage+"  "+publishedAt+"    "+url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(urlToImage);
        dest.writeString(publishedAt);
        dest.writeString(url);

    }
}
