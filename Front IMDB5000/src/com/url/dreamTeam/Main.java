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
            System.out.print("Seleccione una opción:  ");
            Scanner in = new Scanner(System.in);
            String option = in.nextLine();

            //Options in menu
            switch (option){
                case "1":
                    search();
                    break;
                case "2":
                    topMovies();
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
        System.out.println("╔═════════════════════════════════════════════════════════╗");
        System.out.println("║═════════════════════Buscar películas════════════════════║");
        System.out.println("║                                                         ║");
        System.out.println("║                                                         ║");
        System.out.println("║             Valora una película realizando              ║");
        System.out.println("║                  una búsqueda de esta.                  ║");
        System.out.println("║                                                         ║");
        System.out.println("║                                                         ║");
        System.out.println("╚═════════════════════════════════════════════════════════╝");
        System.out.print("Ingresa título de la película:  ");
        Scanner searchWord = new Scanner(System.in);
        String word = searchWord.nextLine();

        System.out.println("╔═════════════════════════════════════════════════════════╗");
        System.out.println("║═════════════════════Buscar películas════════════════════║");
        System.out.println("║                                                         ║");
        System.out.println("║                 Resultado de la búsqueda                ║");
        System.out.println("║                         *****                           ║");
        System.out.println("║                                                         ║");
        System.out.println("║           Ingresa la valoración de la película          ║");
        System.out.println("║                                                         ║");
        System.out.println("╚═════════════════════════════════════════════════════════╝");
        System.out.print("Ingresa tu valoración entre 1 y 10:  ");
        Scanner value = new Scanner(System.in);
        String rating = searchWord.nextLine();

        System.out.println("╔═════════════════════════════════════════════════════════╗");
        System.out.println("║═════════════════════Buscar películas════════════════════║");
        System.out.println("║                                                         ║");
        System.out.println("║                ¡Tu valoración se ha guardado            ║");
        System.out.println("║                        exitosamente!                    ║");
        System.out.println("║                                                         ║");
        System.out.println("╚═════════════════════════════════════════════════════════╝");

    }
    public static void topMovies() {
        System.out.println("╔═════════════════════════════════════════════════════════╗");
        System.out.println("║════════════════════Top 10 de películas══════════════════║");
        System.out.println("║           1.                                            ║");
        System.out.println("║           2.                                            ║");
        System.out.println("║                                                         ║");
        System.out.println("║                                                         ║");
        System.out.println("║                                                         ║");
        System.out.println("║                                                         ║");
        System.out.println("╚═════════════════════════════════════════════════════════╝");
        System.out.print("Ingresa la película que deseas valorar [1-10]:  ");
        Scanner option = new Scanner(System.in);
        String movie = option.nextLine();

        System.out.println("╔═════════════════════════════════════════════════════════╗");
        System.out.println("║════════════════════Top 10 de películas══════════════════║");
        System.out.println("║                                                         ║");
        System.out.println("║                         Película                        ║");
        System.out.println("║                          *****                          ║");
        System.out.println("║                                                         ║");
        System.out.println("║           Ingresa la valoración de la película          ║");
        System.out.println("║                                                         ║");
        System.out.println("╚═════════════════════════════════════════════════════════╝");
        System.out.print("Ingresa tu valoración entre 1 y 10:  ");
        Scanner value = new Scanner(System.in);
        String rating = value.nextLine();

        System.out.println("╔═════════════════════════════════════════════════════════╗");
        System.out.println("║═════════════════════Buscar películas════════════════════║");
        System.out.println("║                                                         ║");
        System.out.println("║                ¡Tu valoración se ha guardado            ║");
        System.out.println("║                        exitosamente!                    ║");
        System.out.println("║                                                         ║");
        System.out.println("╚═════════════════════════════════════════════════════════╝");
    }

    //GET - Rating
    public void getMovieSearch(){
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
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    System.out.println(jsonObject);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //POST - Rating

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
