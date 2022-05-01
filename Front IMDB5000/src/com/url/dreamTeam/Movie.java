package com.url.dreamTeam;

public class Movie {
    private String movie_title;
    private String director_name;
    private String genres;
    private String actor_1;
    private String actor_2_name;
    private String actor_3_name;
    private String plot_keywords;
    private String language;
    private Double imdGb_score;

    public String getMovie_title() { return movie_title; }
    public String getDirector_name() { return director_name; }
    public String getActor_1() { return actor_1; }
    public String getActor_2_name() { return actor_2_name; }
    public String getActor_3_name() { return actor_3_name; }
    public String getGenres() { return genres; }
    public String getPlot_keywords() { return plot_keywords; }
    public String getLanguage() { return language; }
    public Double getImdGb_score() { return imdGb_score; }

    public void setMovie_title() { this.movie_title = movie_title; }
    public void setDirector_name() { this.director_name = director_name; }
    public void setActor_1() { this.actor_1 = actor_1; }
    public void setActor_2_name() { this.actor_2_name = actor_2_name; }
    public void setActor_3_name() { this.actor_3_name = actor_3_name; }
    public void setGenres() { this.genres = genres; }
    public void setPlot_keywords() { this.plot_keywords = plot_keywords; }
    public void setLanguage() { this.language = language; }
    public void setImdGb_score() { this.imdGb_score = imdGb_score; }

    public String toString() {
        return String.format("Titulo:%s | Director:%d | IMDB:%s | Lenguaje:%s", movie_title, director_name, imdGb_score, language);
    }
}
