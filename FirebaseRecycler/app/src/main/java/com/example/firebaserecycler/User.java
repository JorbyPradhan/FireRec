package com.example.firebaserecycler;

public class User {
    private String name,year,major,id;
    private String ImageUrl;

    public User() {
    }

    public User(String id, String name,String ImageUrl, String year, String major) {
        this.id = id;
        this.name = name;
        this.ImageUrl = ImageUrl;
        this.year = year;
        this.major = major;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
