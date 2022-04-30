package com.url.dreamTeam;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import com.google.gson.Gson;

public class Main {

    public static void main(String[] args) {

        movieRating();
    }
    public static void movieRating(){
        int option = 0;
        while (option != 3){
            System.out.println("╔═════════════════════════════════════════════════════════╗");
            System.out.println("║═════════════════Valoración de Películas═════════════════║");
            System.out.println("║                                                         ║");
            System.out.println("║                   1. Buscar película                    ║");
            System.out.println("║                       2. Volver                         ║");
            System.out.println("║                                                         ║");
            System.out.println("╚═════════════════════════════════════════════════════════╝");
            System.out.print("Seleccione una opción:  ");
            Scanner in = new Scanner(System.in);
            option = Integer.parseInt(in.nextLine());

            //Options in menu
            switch (option){
                case 1:
                    search();
                    break;
                case 2:
                    topMovies();
                    break;
                case 3:
                    break;
                default:
                    System.out.print("Opción incorrecta, intentelo nuevamente.");
                    Scanner error = new Scanner(System.in);
                    in.nextLine();
                    clearConsole();
                    break;
            }
        }
    }
    public final static void clearConsole() {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e)
        {
            //  Handle any exceptions.
        }
    }

    //----------2. Rating--------------
    public static void search(){
        //Inicio de busqueda
        clearConsole();
        System.out.println("╔═════════════════════════════════════════════════════════╗");
        System.out.println("║═════════════════════Buscar películas════════════════════║");
        System.out.println("║                                                         ║");
        System.out.println("║                                                         ║");
        System.out.println("║             Valora una película realizando              ║");
        System.out.println("║                      una búsqueda                       ║");
        System.out.println("║               Ingresa tìtulo y categorìa                ║");
        System.out.println("║                                                         ║");
        System.out.println("╚═════════════════════════════════════════════════════════╝");
        System.out.print("Ingresa título de la película:  ");
        Scanner searchWord = new Scanner(System.in);
        String word = searchWord.nextLine();
        System.out.print("Ingresa la categorìa de la película:  ");
        String category = searchWord.nextLine();
        clearConsole();

        //Buscar pelicula
        String movieS = MovieSearch(category,word);
        System.out.println("╔═════════════════════════════════════════════════════════╗");
        System.out.println("║═════════════════════Buscar películas════════════════════║");
        System.out.println("║                                                         ║");
        System.out.println("║                 Resultado de la búsqueda                ║");
        System.out.println(movieS);
        System.out.println("║                                                         ║");
        System.out.println("║           Ingresa la valoración de la película          ║");
        System.out.println("║                                                         ║");
        System.out.println("╚═════════════════════════════════════════════════════════╝");
        System.out.print("Ingresa tu valoración entre 1 y 10:  ");
        int rating = Integer.parseInt(searchWord.nextLine());
        clearConsole();

        //Valorar pelicula
        sendRating(word, rating);
        System.out.println("╔═════════════════════════════════════════════════════════╗");
        System.out.println("║═════════════════════Buscar películas════════════════════║");
        System.out.println("║                                                         ║");
        System.out.println("║                ¡Tu valoración se ha guardado            ║");
        System.out.println("║                        exitosamente!                    ║");
        System.out.println("║                                                         ║");
        System.out.println("╚═════════════════════════════════════════════════════════╝");
        searchWord.nextLine();
    }
    public static void topMovies() {
        List<Movie> topTen = topTen();
        System.out.println("╔═════════════════════════════════════════════════════════╗");
        System.out.println("║════════════════════Top 10 de películas══════════════════║");
        for (var movie: topTen) {
            System.out.println("║" + movie.toString());
        }
        System.out.println("║                                                         ║");
        System.out.println("╚═════════════════════════════════════════════════════════╝");
        System.out.print("Presiona enter para volver al inicio.");
        Scanner option = new Scanner(System.in);
        option.nextLine();
    }

    //GET y POST
    //POST - Search
    public static String MovieSearch(String category, String word){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://127.0.0.1:5000/countries");
        JSONObject json = new JSONObject();

        // json
        json.put("category", category);
        json.put("search",word);
        StringEntity entity = null;
        InputStream inputStream = null;
        String result = null;
        try {
            entity = new StringEntity(json.toString());
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = httpclient.execute(httpPost);

            HttpEntity ee = response.getEntity();
            inputStream = ee.getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();
            JSONObject jsonR = new JSONObject(result);
            httpclient.close();

            return (String) jsonR.get("search");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "No encontrado";
    }
    //POST - Rating
    public static void sendRating(String title, int rating){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://127.0.0.1:5000/countries");
        JSONObject json = new JSONObject();

        // json
        json.put("title", title);
        json.put("rating",rating);
        StringEntity entity = null;
        InputStream inputStream = null;
        String result = null;
        try {
            entity = new StringEntity(json.toString());
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = httpclient.execute(httpPost);
            httpclient.close();
        } catch (UnsupportedEncodingException | ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //GET - Top 10
    public static List<Movie> topTen(){
        List<Movie> topTen = new ArrayList<>();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://127.0.0.1:5000/countries");
        try {
            HttpResponse httpresponse = httpclient.execute(httpget);
            var entity = httpresponse.getEntity();
            StringBuilder builder = new StringBuilder();

            if (entity != null) {
                InputStream inputStream = entity.getContent();
                var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                for (String line = null; (line = bufferedReader.readLine()) != null;) {
                    builder.append(line).append("\n");
                }
                //Exception getting thrown in below line
                JSONArray jsonArray = new JSONArray(builder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject innerObj = jsonArray.getJSONObject(i);
                    Movie movie = new Gson().fromJson(String.valueOf(innerObj), Movie.class);
                    topTen.add(movie);
                }
                return topTen;
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return topTen;
    }



    public static String loginCreate (String username, String password, boolean create){

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost;
        JSONObject json = new JSONObject();

        // json
        json.put("username", username);
        json.put("password",password);
        System.out.print(json);

        StringEntity entity = null;

        if (create){
            // caso create account
            httpPost = new HttpPost("http://127.0.0.1:5000/countries");

            try {
                entity = new StringEntity(json.toString());
                httpPost.setEntity(entity);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                CloseableHttpResponse response = client.execute(httpPost);
                client.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //validaciones
        } else {
            // caso login
            httpPost = new HttpPost("http://127.0.0.1:5000/countries");

            try {
                entity = new StringEntity(json.toString());
                httpPost.setEntity(entity);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                CloseableHttpResponse response = client.execute(httpPost);
                client.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //validaciones
        }

        return json.toString();
    }
}
