package com.url.dreamTeam;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        movieRating();
    }
    public static void searchMovie(){

    }
    public static void movieRating(){
        while (true){
            System.out.println("╔═════════════════════════════════════════════════════════╗");
            System.out.println("║═════════════════Valoración de Películas═════════════════║");
            System.out.println("║                                                         ║");
            System.out.println("║                   1. Buscar película                    ║");
            System.out.println("║             2. Mostrar top 10 de películas              ║");
            System.out.println("║                       3. Volver                         ║");
            System.out.println("║                                                         ║");
            System.out.println("╚═════════════════════════════════════════════════════════╝");
            System.out.print("  Seleccione una opción:  ");
            Scanner in = new Scanner(System.in);
            String option = in.nextLine();

            //Options in menu
            switch (option){
                case "1":
                    System.out.println("Opción1");
                    break;
                case "2":
                    System.out.println("Opción2");
                    break;
                case "3":
                    System.out.println("Opción3");
                    break;
                default:
                    System.out.print("Opción incorrecta, intentelo nuevamente.");
                    Scanner error = new Scanner(System.in);
                    in.nextLine();
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    break;
            }
        }
    }

    public static void search(){

    }
    public static void topMovies(){

    }
}
