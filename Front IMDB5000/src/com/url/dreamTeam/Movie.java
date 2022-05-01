package com.url.dreamTeam;

import com.opencsv.bean.CsvBindByName;

import java.util.List;

public class Movie {
    @CsvBindByName
    private String color;
    @CsvBindByName
    private String movie_title;
    @CsvBindByName
    private String director_name;
    @CsvBindByName
    private String genres;
    @CsvBindByName
    private String actor_1_name;
    @CsvBindByName
    private String actor_2_name;
    @CsvBindByName
    private String actor_3_name;
    @CsvBindByName
    private String plot_keywords;
    @CsvBindByName
    private String language;
    @CsvBindByName
    private Double imdb_score;
    @CsvBindByName
    private String title_year;
    private List<String> actors;

    public void getActorsList(){
        actors = List.of(actor_1_name, actor_2_name, actor_3_name);
    }

    public String getMovie_title() { return movie_title; }
    public String getDirector_name() { return director_name; }
    public String getActor_2_name() { return actor_2_name; }
    public String getActor_3_name() { return actor_3_name; }
    public String getGenres() { return genres; }
    public String getPlot_keywords() { return plot_keywords; }
    public String getLanguage() { return language; }

    public void setMovie_title() { this.movie_title = movie_title; }
    public void setDirector_name() { this.director_name = director_name; }
    public void setActor_2_name() { this.actor_2_name = actor_2_name; }
    public void setActor_3_name() { this.actor_3_name = actor_3_name; }
    public void setGenres() { this.genres = genres; }
    public void setPlot_keywords() { this.plot_keywords = plot_keywords; }
    public void setLanguage() { this.language = language; }

    public Movie(){
        //Empty
    }

//    public String toString() {
//        return String.format("Titulo:%s | Director:%d | IMDB:%s | Lenguaje:%s", movie_title, director_name, imdb_score, language);
//    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getImdb_score() {
        return imdb_score;
    }

    public void setImdb_score(Double imdb_score) {
        this.imdb_score = imdb_score;
    }

    public String getTitle_year() {
        return title_year;
    }

    public void setTitle_year(String title_year) {
        this.title_year = title_year;
    }

    public String getActor_1_name() {
        return actor_1_name;
    }

    public void setActor_1_name(String actor_1_name) {
        this.actor_1_name = actor_1_name;
    }
}
