package com.url.dreamTeam;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import java.util.Scanner;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
    public static void topMovies() {
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
