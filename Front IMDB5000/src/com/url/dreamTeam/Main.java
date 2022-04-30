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
import java.util.*;

public class Main {

    public static void main(String[] args) {

        int loginRes = printLogin();
        switch (loginRes) {
            case 1:
                // crear usuario
                System.out.println("CREAR USUARIO");
                break;
            case 2:
                // caso login valido
                System.out.println("LOGIN CORRECTO");
                movieRating();
                break;
            case 3:
                // caso login invalido
                System.out.println("LOGIN INCORRECTO");
                break;
            case 4:
                // caso de error
                System.out.println("ERROR");
                break;
        }
    }
    public static int printLogin(){

        int result = 0;
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
            result = 4;
        }else if (username.equals("x")){
            // se va a crear usuario
            result = 1;
        }
        else {
            sUsername = String.format("%-12s", username);

            System.out.println("╔═════════════════════════════════════════════════════════╗");
            System.out.println("║═══════════════ BIENVENIDO A LA APLICACIÓN ══════════════║");
            System.out.println("║                                                         ║");
            System.out.println("║          USERNAME:    "+sUsername+"                      ║");
            System.out.println("║          PASSWORD:                                      ║");
            System.out.println("║                                                         ║");
            System.out.println("║                                                         ║");
            System.out.println("║                                                         ║");
            System.out.println("╚═════════════════════════════════════════════════════════╝");
            System.out.print("  Escriba su password : ");
            Scanner inPassword = new Scanner(System.in);
            String password = inPassword.nextLine();
            String cPassword = "";
            
            if(password.length() > 12){
                clearConsole();
                System.out.println("Password debe tener menos de 12 caracteres");
                result = 4;
            }else {
                result = 2;
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
                System.out.println("║                                                         ║");
                System.out.println("║                                                         ║");
                System.out.println("╚═════════════════════════════════════════════════════════╝");

                // Validacion login y si es correcta la contrase;a y username regresa 2, sino regresa 3
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

    public static List<String> getCategories(){
        //obtener las categorias
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://127.0.0.1:5000/categories");
        List<String> categories = new ArrayList<String>();
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

                    String jsonstring = jsonArray.getString(i);
                    categories.add(jsonstring);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public static List<String> showCategories(){
//        List<String> categories = getCategories();
        List<String> categories = Arrays.asList("Miedo", "Sci-Fi", "three");
        System.out.println("Categorias");
        var num = 1;

        for( var category: categories){
            System.out.print(num + "."+ category + "   ");
            num++;
        }
        System.out.println();


        return categories;
    }

    public static void selectFavCategories(List<String> categories)  {
        boolean isValid=false;

            System.out.println("╔═════════════════════════════════════════════════════════════════════╗");
            System.out.println("║  Ingresa el índice de tus categorias favoritas separadas por comas  ║");
            System.out.println("╚═════════════════════════════════════════════════════════════════════╝");
            System.out.println("Ingresa X para regresar");
            Scanner in = new Scanner(System.in);
            String options = in.nextLine();
            options = options.toLowerCase();

            if(!options.contains("x")){
                String[] selectedCategoriesIndex = options.split(",");
                //Eliminar numeros duplicados
                LinkedHashSet linkedHashSet = new LinkedHashSet<String>(List.of(selectedCategoriesIndex));
                //Get back the array without duplicates
                selectedCategoriesIndex = (String[]) linkedHashSet.toArray(new String[] {});
                isValid = isValidCategories(selectedCategoriesIndex,categories.size());

                if(isValid){
                    List<String> selectedCategories = new ArrayList<String>();
                    for (String categoryIndex : selectedCategoriesIndex){

                                var cat = categories.get(Integer.valueOf(categoryIndex)-1);
                                System.out.println(cat);
                                selectedCategories.add(cat);
                    }

                    setFavCategories( selectedCategories);
                }

            }else if(options.equals("")){

            }



    }

    public static boolean isValidCategories(String[] categories, int maxValue ){

            try {
                for(var category: categories) {
                    System.out.println(category);
                    if (Integer.valueOf(category) > maxValue || Integer.valueOf(category) < 1) {
                        // chequear que no hayan numeros que no existan en las categorias
                        System.out.println("Ingresa categorias validas");
                        return false;
                    }
                }
            }catch (NumberFormatException ne){
                System.out.println("Ingresa los indices de las categorias");
                return false;
            }


        return true;
    }

    public static void setFavCategories(List<String> categories){

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://127.0.0.1:5000/categories");
        JSONObject json = new JSONObject();
        json.put("FavCategories", categories);
        json.put("username", "Eduarso");
        System.out.print(json);
        StringEntity entity = null;
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

    }


    public static void setUserRecommendation(){
        //mandar el usuario, para que en la api se busque si el usuario tiene generos fav
        //y devolver un array de 10 peliculas
    }

    private static void setRecommendation(String user){
        //setear los gustos

//        CloseableHttpClient client = HttpClients.createDefault();
//        HttpPost httpPost = new HttpPost("http://127.0.0.1:5000/countries");
//        JSONObject json = new JSONObject();
//        //String json = "{\"area\":1,\"name\":\"John\",\"capital\":\"Mayer\"}";
//        json.put("usuario", user);
//        json.put("area", 1);
//        json.put("name","Estuardo");
//        json.put("capital","Guate");
//        System.out.print(json);
//        StringEntity entity = null;
//        try {
//            entity = new StringEntity(json.toString());
//            httpPost.setEntity(entity);
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setHeader("Content-type", "application/json");
//            CloseableHttpResponse response = client.execute(httpPost);
//            client.close();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


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
