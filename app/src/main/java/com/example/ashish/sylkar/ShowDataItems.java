package com.example.ashish.sylkar;

/**
 * Created by Ashish on 16-08-2017.
 */

public class ShowDataItems {
    private String imageurl,name;

    public ShowDataItems() {
    }

    public ShowDataItems(String imageurl, String name) {
        this.imageurl = imageurl;
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
