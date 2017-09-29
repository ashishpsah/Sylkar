package com.example.ashish.sylkar;

/**
 * Created by Ashish on 11-07-2017.
 */

public class Users {
    String etTitle;
    String etAddress;
    String etPhone;
    String etIBAN;
    String etJob;
    String imageurl;

    public Users()
    {
        //Default constructor required
    }

    public Users( String etTitle, String etAddress, String etPhone, String etIBAN, String etJob, String imageurl) {
        this.etTitle = etTitle;
        this.etAddress = etAddress;
        this.etPhone = etPhone;
        this.etIBAN = etIBAN;
        this.etJob =etJob;
        this.imageurl = imageurl;
    }

    public String getEtTitle() {
        return etTitle;
    }

    public void setEtTitle(String etTitle) {
        this.etTitle = etTitle;
    }

    public String getEtAddress() {
        return etAddress;
    }

    public void setEtAddress(String etAddress) {
        this.etAddress = etAddress;
    }

    public String getEtPhone() {
        return etPhone;
    }

    public void setEtPhone(String etPhone) {
        this.etPhone = etPhone;
    }

    public String getEtIBAN() {
        return etIBAN;
    }

    public void setEtIBAN(String etIBAN) {
        this.etIBAN = etIBAN;
    }

    public String getEtJob() {
        return etJob;
    }

    public void setEtJob(String etJob) {
        this.etJob = etJob;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
