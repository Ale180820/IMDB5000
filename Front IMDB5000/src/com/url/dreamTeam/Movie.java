package com.url.dreamTeam;

import com.opencsv.bean.CsvBindByName;
import org.apache.commons.lang3.StringUtils;

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

    private double score;
    private List<String> actors;

    public void getActorsList(){
        actors = List.of(actor_1_name, actor_2_name, actor_3_name);
    }

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

    public String getColor() {
        return color;
    }
    public String getDirector_name() {
        return director_name;
    }
    public String getActor_1_name() {
        return actor_1_name;
    }
    public String getActor_2_name() {
        return actor_2_name;
    }
    public String getActor_3_name() {
        return actor_3_name;
    }
    public String getMovie_title() { return movie_title; }
    public String getGenres() {
        return genres;
    }
    public String getPlot_keywords() {
        return plot_keywords;
    }
    public String getLanguage() {
        return language;
    }
    public Double getImdb_score() {
        return imdb_score;
    }
    public String getTitle_year() {
        return title_year;
    }
    public List<String> getActors() {
        return actors;
    }



    public String getStringActors(){
        return StringUtils.join(actors, "|");
    }
    public String toFormattedString(int category) {
        switch (category) {
            case 2:
                return movie_title + " - " + director_name;
            case 3:
                return movie_title + " - " + getStringActors();
            case 4:
                return movie_title + " - " + genres;
            case 5:
                return movie_title + " - " + plot_keywords;
            case 6:
                return movie_title + " - " + language;
            case 7:
                return movie_title + " - " + imdb_score;
            case 8:
                return movie_title + " - " + title_year;
            case 9:
                return movie_title + " - " + title_year + " - " + String.format("%.2f", score * 100.0) + "%";
            default:
                return movie_title ;
        }

    }
}
