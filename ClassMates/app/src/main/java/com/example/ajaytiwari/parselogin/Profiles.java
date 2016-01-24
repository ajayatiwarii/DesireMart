package com.example.ajaytiwari.parselogin;

import android.graphics.Bitmap;

import com.parse.ParseUser;

/**
 * Created by ajay.tiwari on 9/8/2015.
 */
public class Profiles {
    public Profiles() {

    }

    long id;
    ParseUser device_id;
    String name;
    String gender;
    String status;
    String is_active;
    int pass_year;
    String profession;
    String mobile;
    Boolean edu_help;
    Boolean sales_help;
    Boolean marketing_help;
    Bitmap bitmap;
    String fbID;
    String desigina;
    String location;
    String company;
    String interests;
    String profile_pic_path;
    String batch;
    String organi;
    String college;
    String education;

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getEdu_help() {
        return edu_help;
    }

    public void setEdu_help(Boolean edu_help) {
        this.edu_help = edu_help;
    }

    public Boolean getSales_help() {
        return sales_help;
    }

    public void setSales_help(Boolean sales_help) {
        this.sales_help = sales_help;
    }

    public Boolean getMarketing_help() {
        return marketing_help;
    }

    public void setMarketing_help(Boolean marketing_help) {
        this.marketing_help = marketing_help;
    }

    public String getOrgani() {
        return organi;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setOrgani(String organi) {
        this.organi = organi;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDesigina() {
        return desigina;
    }

    public void setDesigina(String desigina) {
        this.desigina = desigina;
    }

    public int getPass_year() {
        return pass_year;
    }

    public void setPass_year(int pass_year) {
        this.pass_year = pass_year;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ParseUser getDevice_id() {
        return device_id;
    }

    public void setDevice_id(ParseUser device_id) {
        this.device_id = device_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getProfile_pic_path() {
        return profile_pic_path;
    }

    public void setProfile_pic_path(String profile_pic_path) {
        this.profile_pic_path = profile_pic_path;
    }

    public String getFbID() {
        return fbID;
    }

    public void setFbID(String fbID) {
        this.fbID = fbID;
    }

    public Profiles(ParseUser device_id, String name, String gender, String status, String is_active, String interests, String profile_pic_path) {

        this.profile_pic_path = profile_pic_path;
        this.interests = interests;
        this.is_active = is_active;
        this.status = status;
        this.gender = gender;
        this.name = name;
        this.device_id = device_id;

    }


}
