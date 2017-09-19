package com.example.ashish.sylkar;

/**
 * Created by ashishpsah on 19.09.17.
 */

public class Inventory {
    String etTitle,etQuant,imageurl;

    public Inventory() {
    }

    public Inventory(String etTitle, String etQuant, String imageurl) {
        this.etTitle = etTitle;
        this.etQuant = etQuant;
        this.imageurl = imageurl;
    }

    public String getEtTitle() {
        return etTitle;
    }

    public void setEtTitle(String etTitle) {
        this.etTitle = etTitle;
    }

    public String getEtQuant() {
        return etQuant;
    }

    public void setEtQuant(String etQuant) {
        this.etQuant = etQuant;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
