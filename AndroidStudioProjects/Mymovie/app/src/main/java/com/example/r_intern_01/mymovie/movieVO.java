package com.example.r_intern_01.mymovie;

public class movieVO {
    private String image;
    private String title;
    private String stars;
    private String year;
    private String director;
    private String actor;
    private String link;


    public String getLink(){return link;}

    public void setLink(String link){
        this.link=link;
    }

    public String getImage(){return image;}

    public void setImage(String image){
        this.image=image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }
}

