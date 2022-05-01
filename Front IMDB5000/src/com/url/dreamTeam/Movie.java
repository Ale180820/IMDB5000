package com.url.dreamTeam;

import com.opencsv.bean.CsvBindByName;

import java.nio.charset.StandardCharsets;
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

    public void setMovie_title(String movie_title) { this.movie_title = movie_title; }

    public Movie(){
        //Empty
    }

    public void TrimAll() {
        color = color == null ? null : new String(color.getBytes(), StandardCharsets.US_ASCII);
        movie_title = movie_title == null ? null : new String(movie_title.getBytes(), StandardCharsets.US_ASCII);
        director_name = director_name == null ? null : new String(director_name.getBytes(), StandardCharsets.US_ASCII);
        genres = genres == null ? null : new String(genres.getBytes(), StandardCharsets.US_ASCII);
        actor_1_name = actor_1_name == null ? null : new String(actor_1_name.getBytes(), StandardCharsets.US_ASCII);
        actor_2_name = actor_2_name == null ? null : new String(actor_2_name.getBytes(), StandardCharsets.US_ASCII);
        actor_3_name = actor_3_name == null ? null : new String(actor_3_name.getBytes(), StandardCharsets.US_ASCII);
        plot_keywords = plot_keywords == null ? null : new String(plot_keywords.getBytes(), StandardCharsets.US_ASCII);
        language = language == null ? null : new String(language.getBytes(), StandardCharsets.US_ASCII);
        title_year = title_year == null ? null : new String(title_year.getBytes(), StandardCharsets.US_ASCII);
    }
}
