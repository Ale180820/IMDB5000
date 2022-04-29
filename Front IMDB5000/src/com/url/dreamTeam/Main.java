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
        printLogin();
        movieRating();
    }
    public static boolean printLogin(){

        boolean result = false;
        
        System.out.println("╔═════════════════════════════════════════════════════════╗");
        System.out.println("║═══════════════ BIENVENIDO A LA APLICACIÓN ══════════════║");
        System.out.println("║                                                         ║");
        System.out.println("║          USERNAME:                                      ║");
        System.out.println("║          PASSWORD:                                      ║");
        System.out.println("║                                                         ║");
        System.out.println("║            > presione 'x' para crear cuenta <           ║");
        System.out.println("║                                                         ║");
        System.out.println("╚═════════════════════════════════════════════════════════╝");
        System.out.print("  Escriba su username o 'x' : ");
        Scanner inUsername = new Scanner(System.in);
        String username = inUsername.nextLine();
        String sUsername = "";

        if (username.length() > 12){
            clearConsole();
            System.out.println("Username debe tener menos de 12 caracteres");
        }else {
            sUsername = String.format("%-12s", username);

            System.out.println("╔═════════════════════════════════════════════════════════╗");
            System.out.println("║═══════════════ BIENVENIDO A LA APLICACIÓN ══════════════║");
            System.out.println("║                                                         ║");
            System.out.println("║          USERNAME:    "+sUsername+"                      ║");
            System.out.println("║          PASSWORD:                                      ║");
            System.out.println("║                                                         ║");
            System.out.println("║            > presione 'x' para crear cuenta <           ║");
            System.out.println("║                                                         ║");
            System.out.println("╚═════════════════════════════════════════════════════════╝");
            System.out.print("  Escriba su password : ");
            Scanner inPassword = new Scanner(System.in);
            String password = inPassword.nextLine();
            String cPassword = "";
            
            if(password.length() > 12){
                clearConsole();
                System.out.println("Password debe tener menos de 12 caracteres");
            }else {
                result = true;
                for (var item : password.toCharArray()) {
                    cPassword += "*";
                }

                cPassword = String.format("%-12s", cPassword);

                System.out.println("╔═════════════════════════════════════════════════════════╗");
                System.out.println("║═══════════════ BIENVENIDO A LA APLICACIÓN ══════════════║");
                System.out.println("║                                                         ║");
                System.out.println("║          USERNAME:    "+sUsername+"                      ║");
                System.out.println("║          PASSWORD:    "+cPassword+"                      ║");
                System.out.println("║                                                         ║");
                System.out.println("║            > presione 'x' para crear cuenta <           ║");
                System.out.println("║                                                         ║");
                System.out.println("╚═════════════════════════════════════════════════════════╝");
            }
        }

        return result;
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

    public final static void clearConsole()
    {
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
}
