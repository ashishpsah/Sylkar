package com.example.ashish.sylkar;

/**
 * Created by Ashish on 11-07-2017.
 */

public class Users {
    String etTitle,etAddress,etPhone,etIBAN,etJob,imageurl;
    public Users()
    {

    }

    public Users( String etTitle, String etAddress, String etPhone, String etIBAN, String etJob, String imageurl) {

        this.etTitle = etTitle;
        this.etAddress = etAddress;
        this.etPhone = etPhone;
        this.etIBAN = etIBAN;
        this.etJob =etJob;
        this.imageurl = imageurl;

    }
    //check this constructor
    public Users( String imageurl){


        this.imageurl = imageurl;
    }





    public String getName() {
        return etTitle;
    }

    public String getAddress() {
        return etAddress;
    }

    public String getPhone() {
        return etPhone;
    }

    public String getEtIBAN() {
        return etIBAN;
    }

    public String getJob() {
        return etJob;
    }


    public String getImageurl() {
        return imageurl;
    }


}
