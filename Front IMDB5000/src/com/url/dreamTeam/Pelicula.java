
package com.url.dreamTeam;

import java.util.ArrayList;

public class Pelicula {

    String titulo;
    ArrayList<String> generos;
    ArrayList<String> actores;
    ArrayList<String> directores;
    Integer puntuacion;

    public Pelicula(){

    }

    @Override
    public String toString() {
        return this.titulo + "  " + this.generos+ "  " + this.actores + "  " + this.directores + "  " + puntuacion;
    }
}