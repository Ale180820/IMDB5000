package com.url.dreamTeam;

import com.google.gson.Gson;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class Main {


    public static String user;
    public static void main(String[] args) { initProgram(); }

    public static void initProgram() {
        try {
            user = "";
            int loginRes = printLogin();
            switch (loginRes) {
                case 1:
                    // crear usuario
                    System.out.println("CREAR USUARIO");
                    if(printCreateProfile()){
                        selectFavCategories();
                        menuPrincipal();
                    }else{
                        System.out.println("No se registro el usuario");
                    }

                    break;
                case 2:
                    // caso login valido
                    System.out.println("  .--.      .-'.      .--.      .--.      .--.      .--.      .`-.      .--.");
                    System.out.println(":::::.\\::::::::.\\::::::::::::.BIENVENIDO::::::::::.\\::::::::.\\::::::::.\\");
                    System.out.println("'      `--'      `.-'      `--'      `--'      `--'      `-.'      `--'      `");
                    menuPrincipal();
                    break;
                case 3:
                    // caso login invalido
                    System.out.println("INCORRECTO LOGIN");
                    System.out.println("    __.-._");
                    System.out.println("    '-._\"7'");
                    System.out.println("     /'.-c");
                    System.out.println("     |  /T");
                    System.out.println("    _)_/LI");
                    initProgram();
                    break;
                case 4:
                    // caso de error
                    System.out.println("XXXXX----- ERROR -----XXXXX");

                    initProgram();
                    break;
            }
            new Scanner(System.in).nextLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Menu principal
    public static void menuPrincipal() throws FileNotFoundException {
        int option = 0;
        while (option != 5) {
            System.out.println("╔═════════════════════════════════════════════════════════╗");
            System.out.println("║══════════════════════════Menú═══════════════════════════║");
            System.out.println("║                                                         ║");
            System.out.println("║                   1. Calificar peliculas                ║");
            System.out.println("║                 2. Obtener recomendaciones              ║");
            System.out.println("║                3. Cargar csv con películas              ║");
            System.out.println("║                      4. Cerrar Sesión                   ║");
            System.out.println("║                                                         ║");
            System.out.println("╚═════════════════════════════════════════════════════════╝");
            System.out.print("Seleccione una opción:  ");
            Scanner in = new Scanner(System.in);
            try {
                option = Integer.parseInt(in.nextLine());

                // Options in menu
                switch (option) {
                    case 1:
                        search();
                        break;
                    case 2:
                        topMovies();
                        break;
                    case 3:
                        System.out.print("Ingrese la dirección del archivo:  ");
                        var fileAddress = in.nextLine();
                        fileAddress = fileAddress.replaceAll("\"", "");
                        if (sendCSVMovies(fileAddress)){
                            System.out.println("Archivo cargado correctamente");
                        }
                        break;
                    case 4:
                        initProgram();
                        break;
                    default:
                        System.out.println("Opción incorrecta, intentelo nuevamente.");
                        new Scanner(System.in).nextLine();
                        clearConsole();
                        break;
                }
            } catch (NumberFormatException | FileNotFoundException e) {
                System.out.println("Opción incorrecta, intentelo nuevamente.");
                new Scanner(System.in).nextLine();
            }
        }
    }

    // 1. Login y creación de cuenta
    public static int printLogin() {

        int result;
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
        String username = inUsername.nextLine().toLowerCase();
        String sUsername;

        if (username.length() > 12) {
            clearConsole();
            System.out.println("Username debe tener menos de 12 caracteres");
            result = 4;
        } else if (username.equals("x")) {
            // se va a crear usuario
            result = 1;
        } else {
            sUsername = String.format("%-12s", username);

            int sizeOfString = ("          USERNAME:    " + sUsername + "                      ").length();
            System.out.println("╔"+ formatStringHorizontal(sizeOfString)+"╗");
            System.out.println("║"+ formatStringSize("══════ INICIAR SESIÓN ══════", sizeOfString)+"║");
            System.out.println("║"+ formatStringSize(" ", sizeOfString)+"║");
            System.out.println("║"+ formatStringSize(" USERNAME:    " + sUsername + " ", sizeOfString)+"║");
            System.out.println("║"+ formatStringSize(" PASSWORD: ", sizeOfString)+"║");
            System.out.println("║"+ formatStringSize(" ", sizeOfString)+"║");
            System.out.println("╚"+ formatStringHorizontal(sizeOfString)+"╝");
            System.out.print("  Ingrese su password : ");
            Scanner inPassword = new Scanner(System.in);
            String password = inPassword.nextLine();
            String cPassword;

            if (password.length() > 12) {
                clearConsole();
                System.out.println("Password debe tener menos de 12 caracteres");
                result = 4;
            } else {
                cPassword = "*".repeat(password.length());

                int sizeOfPassString = ("║          PASSWORD:    " + cPassword + "                      ║").length();
                sizeOfString = Math.max(sizeOfPassString, sizeOfString);

                System.out.println("╔"+ formatStringHorizontal(sizeOfString)+"╗");
                System.out.println("║"+ formatStringSize("══════ INICIAR SESIÓN ══════", sizeOfString)+"║");
                System.out.println("║"+ formatStringSize(" ", sizeOfString)+"║");
                System.out.println("║"+ formatStringSize(" USERNAME:    " + sUsername + " ", sizeOfString)+"║");
                System.out.println("║"+ formatStringSize(" PASSWORD:    " + cPassword + " ", sizeOfString)+"║");
                System.out.println("║"+ formatStringSize(" ", sizeOfString)+"║");
                System.out.println("╚"+ formatStringHorizontal(sizeOfString)+"╝");
                // Validacion login y si es correcta la contrase;a y username regresa 2, sino
                // regresa 3
                result = loginCreate(sUsername.trim(), password.trim(), false) ? 2 : 3;
            }
        }
        user = username;
        return result;
    }

    public static boolean printCreateProfile() {
        boolean result  = false;

        System.out.println("╔═════════════════════════════════════════════════════════╗");
        System.out.println("║═════════════════ CREACIÓN DE USUARIOS ══════════════════║");
        System.out.println("║                                                         ║");
        System.out.println("║          USERNAME:                                      ║");
        System.out.println("║          PASSWORD:                                      ║");
        System.out.println("║                                                         ║");
        System.out.println("╚═════════════════════════════════════════════════════════╝");
        System.out.print("  Ingrese su username: ");
        Scanner inUsername = new Scanner(System.in);
        String username = inUsername.nextLine().toLowerCase();
        String sUsername;

        if (username.length() > 12) {
            clearConsole();
            System.out.println("Username debe tener menos de 12 caracteres");
        } else {
            sUsername = String.format("%-12s", username);

            int sizeOfString = ("          USERNAME:    " + sUsername + "                      ").length();
            System.out.println("╔"+ formatStringHorizontal(sizeOfString)+"╗");
            System.out.println("║"+ formatStringSize("═════════════════ CREACIÓN DE USUARIOS ══════════════════", sizeOfString)+"║");
            System.out.println("║"+ formatStringSize("                                                         ", sizeOfString)+"║");
            System.out.println("║"+ formatStringSize("          USERNAME:    " + sUsername + "                 ", sizeOfString)+"║");
            System.out.println("║"+ formatStringSize("          PASSWORD:                                      ", sizeOfString)+"║");
            System.out.println("║"+ formatStringSize("                                                         ", sizeOfString)+"║");
            System.out.println("╚"+ formatStringHorizontal(sizeOfString)+"╝");
            System.out.print("  Ingrese su password : ");
            Scanner inPassword = new Scanner(System.in);
            String password = inPassword.nextLine();
            String cPassword;

            if (password.length() > 12) {
                clearConsole();
                System.out.println("Password debe tener menos de 12 caracteres");
            } else {
                cPassword = "*".repeat(password.length());

                cPassword = String.format("%-12s", cPassword);

                int sizeOfPassString = ("║          PASSWORD:    " + cPassword + "                      ║").length();
                sizeOfString = Math.max(sizeOfPassString, sizeOfString);

                System.out.println("╔"+ formatStringHorizontal(sizeOfString)+"╗");
                System.out.println("║"+ formatStringSize("═════════════════ CREACIÓN DE USUARIOS ══════════════════", sizeOfString)+"║");
                System.out.println("║"+ formatStringSize("                                                         ", sizeOfString)+"║");
                System.out.println("║"+ formatStringSize("          USERNAME:    " + sUsername + "                 ", sizeOfString)+"║");
                System.out.println("║"+ formatStringSize("          PASSWORD:    " + cPassword + "                 ", sizeOfString)+"║");
                System.out.println("║"+ formatStringSize("                                                         ", sizeOfString)+"║");
                System.out.println("╚"+ formatStringHorizontal(sizeOfString)+"╝");

                // Crear cuenta, si se creo correctamente devolver 1, sino devolver 2
                result = loginCreate(sUsername.trim(), password.trim(), true);
            }
        }
        user = username;
        return result;
    }

//    // 2. Rating de películas
//    public static void movieRating() throws IOException {
//        int option = 0;
//        while (option != 2) {
//            System.out.println("╔═════════════════════════════════════════════════════════╗");
//            System.out.println("║═════════════════Valoración de Películas═════════════════║");
//            System.out.println("║                                                         ║");
//            System.out.println("║                   1. Buscar película                    ║");
//            System.out.println("║                       2. Volver                         ║");
//            System.out.println("║                                                         ║");
//            System.out.println("╚═════════════════════════════════════════════════════════╝");
//            System.out.print("Seleccione una opción:  ");
//            Scanner in = new Scanner(System.in);
//            option = Integer.parseInt(in.nextLine());
//            if (option == 1) {
//                search();
//            } else {
//                System.out.print("Opción incorrecta, intentelo nuevamente.");
//                System.in.read();
//            }
//        }
//    }

    public static void search() {
        // Inicio de busqueda
        clearConsole();
        System.out.println("╔═════════════════════════════════════════════════════════╗");
        System.out.println("║═════════════════════Buscar películas════════════════════║");
        System.out.println("║                                                         ║");
        System.out.println("║                                                         ║");
        System.out.println("║             Valora una película realizando              ║");
        System.out.println("║                      una búsqueda                       ║");
        System.out.println("║                       Categorías                        ║");
        System.out.println("║             1. Título                                   ║");
        System.out.println("║             2. Nombre del director                      ║");
        System.out.println("║             3. Actor                                    ║");
        System.out.println("║             4. Genéro                                   ║");
        System.out.println("║             5. Palabras clave                           ║");
        System.out.println("║             6. Lenguaje                                 ║");
        System.out.println("║             7. IMDB                                     ║");
        System.out.println("║             8. Año                                      ║");
        System.out.println("║                                                         ║");
        System.out.println("║           Ingresa tu búsqueda y categorìa               ║");
        System.out.println("║                                                         ║");
        System.out.println("╚═════════════════════════════════════════════════════════╝");
        Scanner searchWord = new Scanner(System.in);
        try
        {
            System.out.print("Ingresa la categorìa de la película [1-8]:  ");
            int category = Integer.parseInt(searchWord.nextLine());
            if (category < 1 || category > 8){
                System.out.println("Opción inválida.");
                menuPrincipal();
            }
            if(category==4)
                showCategories(false);
            System.out.print("Ingresa el valor a buscar:  ");
            String word = searchWord.nextLine();

            clearConsole();

            // Buscar pelicula
            List<Movie> movieList = MovieSearch(category, word);
            if (movieList.size() == 0){
                System.out.println("Ninguna película coincidió con la búsqueda");
                System.out.print("Presiona enter para volver al inicio.");
                new Scanner(System.in).nextLine();
                try {
                    menuPrincipal();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            var longestMovie = Collections.max(movieList, Comparator.comparingInt((Movie o) -> o.getMovie_title().length()));
            var sizeOfString = (longestMovie.getMovie_title()).length();
            switch (category) {
                case 2:
                    sizeOfString += Collections.max(movieList, Comparator.comparingInt((Movie o) -> o.getDirector_name().length()))
                            .getDirector_name().length();
                    break;
                case 3:
                    sizeOfString += Collections.max(movieList, Comparator.comparingInt((Movie o) -> o.getStringActors().length()))
                            .getStringActors().length();
                    break;
                case 4:

                    sizeOfString += Collections.max(movieList, Comparator.comparingInt((Movie o) -> o.getGenres().length()))
                            .getGenres().length();
                    break;
                case 5:
                    sizeOfString += Collections.max(movieList, Comparator.comparingInt((Movie o) -> o.getPlot_keywords().length()))
                            .getPlot_keywords().length();
                    break;
                case 6:
                    sizeOfString += Collections.max(movieList, Comparator.comparingInt((Movie o) -> o.getLanguage().length()))
                            .getLanguage().length();
                    break;
                case 7:
                    sizeOfString += 5;
                    break;
                case 8:
                    sizeOfString += Collections.max(movieList, Comparator.comparingInt((Movie o) -> o.getTitle_year().length()))
                            .getTitle_year().length();
                    break;
                default:
                    break;
            }

            sizeOfString += String.valueOf(movieList.size()).length() + 8;
            sizeOfString = Collections.max(List.of(sizeOfString, 40));

            System.out.println("╔"+ formatStringHorizontal(sizeOfString)+"╗");
            System.out.println("║"+ formatStringSize("══════ Buscar películas ══════", sizeOfString)+"║");
            System.out.println("║"+ formatStringSize(" ", sizeOfString)+"║");
            System.out.println("║"+ formatStringSize("Resultado de la búsqueda", sizeOfString)+"║");
            System.out.println("║"+ formatStringSize("~", sizeOfString)+"║");
            System.out.println("║"+ formatStringSize(" ", sizeOfString)+"║");
            for (int i = 0; i < movieList.size(); i++) {
                System.out.println("║" + formatStringSize((i + 1) + ". " + movieList.get(i).toFormattedString(category), sizeOfString) + "║");
            }
            System.out.println("║"+ formatStringSize(" ", sizeOfString)+"║");
            System.out.println("║"+ formatStringSize("~", sizeOfString)+"║");
            System.out.println("║"+ formatStringSize(" Ingrese la película y su valoración ", sizeOfString)+"║");
            System.out.println("║"+ formatStringSize(" ", sizeOfString)+"║");
            System.out.println("╚"+ formatStringHorizontal(sizeOfString)+"╝");
            System.out.print("Ingrese la película [1-" + movieList.size()+"]: ");
            int moviesSelection = Integer.parseInt(searchWord.nextLine());
            System.out.print("Ingrese tu valoración entre 1 y 10 (enteros):  ");

            int rating = Integer.parseInt(searchWord.nextLine());
            clearConsole();
            if (rating < 1 || rating > 10){
                System.out.println("Error: La calificación se encuentra fuera del rango.");
                menuPrincipal();
            }
            if (moviesSelection - 1 < 0 || moviesSelection> movieList.size()) {
                System.out.println("Error: La película se encuentra fuera del rango.");
                menuPrincipal();
            }
            // Valorar pelicula
            if(sendRating(movieList.get(moviesSelection-1).getMovie_title(), rating)){
                System.out.println("╔═════════════════════════════════════════════════════════╗");
                System.out.println("║═════════════════════Buscar películas════════════════════║");
                System.out.println("║                                                         ║");
                System.out.println("║                ¡Tu valoración se ha guardado            ║");
                System.out.println("║                        exitosamente!                    ║");
                System.out.println("║                                                         ║");
                System.out.println("╚═════════════════════════════════════════════════════════╝");
            }else{
                System.out.println("╔═════════════════════════════════════════════════════════╗");
                System.out.println("║═════════════════════Buscar películas════════════════════║");
                System.out.println("║                                                         ║");
                System.out.println("║             Tu valoración no se ha guardado.            ║");
                System.out.println("║                    Intentalo nuevamente.                ║");
                System.out.println("║                                                         ║");
                System.out.println("╚═════════════════════════════════════════════════════════╝");
            }
            System.out.print("Presiona enter para volver al inicio.");
            searchWord.nextLine();
        } catch (NumberFormatException e) {
            System.out.print("Error: El valor ingresado no coincide con las opciones. Intentelo nuevamente.");
            new Scanner(System.in).nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 3. Recomendaciones
    public static void topMovies() {
        List<Movie> topTen = topTen();
        if (topTen.size() == 0){
            System.out.println("No hay películas disponibles :(");
            new Scanner(System.in).nextLine();
            try {
                menuPrincipal();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        var longestMovie = Collections.max(topTen, Comparator.comparingInt((Movie o) -> o.getMovie_title().length()));
        var sizeOfString = (longestMovie.getMovie_title()).length();
        sizeOfString += String.valueOf(topTen.size()).length() + 19;
        System.out.println("╔"+ formatStringHorizontal(sizeOfString)+"╗");
        System.out.println("║"+ formatStringSize("══════ Top 10 de películas ══════", sizeOfString)+"║");
        System.out.println("║"+ formatStringSize(" ", sizeOfString)+"║");
        for (int i = 0; i < topTen.size(); i++) {
            System.out.println("║" + formatStringSize((i + 1) + ". " + topTen.get(i).toFormattedString(9), sizeOfString) + "║");
        }
        System.out.println("║"+ formatStringSize(" ", sizeOfString)+"║");
        System.out.println("╚"+ formatStringHorizontal(sizeOfString)+"╝");
        System.out.print("Presiona enter para volver al inicio.");
        Scanner option = new Scanner(System.in);
        option.nextLine();
    }
    // GET y POST

    // POST - Search
    public static List<Movie> MovieSearch(int category, String word) {
        String categorySend = "";
        switch (category) {
            case 1:
                categorySend = "movie_title";
                break;
            case 2:
                categorySend = "director_name";
                break;
            case 3:
                categorySend = "actors";
                break;
            case 4:
                categorySend = "genres";
                break;
            case 5:
                categorySend = "plot_keywords";
                break;
            case 6:
                categorySend = "language";
                break;
            case 7:
                categorySend = "imdb_score";
                break;
            case 8:
                categorySend = "title_year";
                break;
            default:
                break;
        }

        List<Movie> topTen = new ArrayList<>();

        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(new URIBuilder("http://127.0.0.1:5000/movies")
                    .addParameter("category", categorySend)
                    .addParameter("query", word).build());
            HttpResponse httpresponse = httpclient.execute(httpget);
            var entity = httpresponse.getEntity();
            var reasonPhrase = httpresponse.getStatusLine();
            if (reasonPhrase.getStatusCode() == 418) {
                System.out.println(httpresponse.getStatusLine().getReasonPhrase());
                System.out.println("             ;,'");
                System.out.println("     _o_    ;:;'");
                System.out.println(" ,-.'---`.__ ;");
                System.out.println("((j`=====',-'");
                System.out.println(" `-\\     /");
                System.out.println("    `-=-' ");
            }
            StringBuilder builder = new StringBuilder();

            if (entity.getContent() != null) {
                InputStream inputStream = entity.getContent();
                var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                for (String line; (line = bufferedReader.readLine()) != null;) {
                    builder.append(line).append("\n");
                }
                // Exception getting thrown in below line
                JSONArray jsonArray = new JSONArray(builder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject innerObj = jsonArray.getJSONObject(i);
                    Movie movie = new Gson().fromJson(String.valueOf(innerObj), Movie.class);
                    topTen.add(movie);
                }
                return topTen;
            }

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return topTen;
    }

    // POST - Rating
    public static boolean sendRating(String title, double rating) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://127.0.0.1:5000/movies/grading");
        JSONObject json = new JSONObject();

        // json
        json.put("movie", title);
        json.put("grade", rating);
        json.put("username", user);
        StringEntity entity;
        try {
            entity = new StringEntity(json.toString());
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = httpclient.execute(httpPost);
            httpclient.close();
            return response.getStatusLine().getStatusCode() == 200;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // GET - Top 10
    public static List<Movie> topTen() {
        List<Movie> topTen = new ArrayList<>();
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpGet httpget = new HttpGet(new URIBuilder("http://127.0.0.1:5000/recommend")
                    .addParameter("username", user)
                    .build());
            HttpResponse httpresponse = httpclient.execute(httpget);
            var entity = httpresponse.getEntity();

            StringBuilder builder = new StringBuilder();

            if (entity != null) {
                InputStream inputStream = entity.getContent();
                var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                for (String line; (line = bufferedReader.readLine()) != null;) {
                    builder.append(line).append("\n");
                }
                // Exception getting thrown in below line
                JSONArray jsonArray = new JSONArray(builder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject innerObj = jsonArray.getJSONObject(i);
                    Movie movie = new Gson().fromJson(String.valueOf(innerObj), Movie.class);
                    topTen.add(movie);
                }
                return topTen;
            }

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return topTen;
    }

    // POST - Login and create
    public static boolean loginCreate(String username, String password, boolean create) {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost;
        JSONObject json = new JSONObject();

        // json
        json.put("username", username);
        json.put("password", password);

        StringEntity entity;

        if (create) {
            // caso create account
            httpPost = new HttpPost("http://127.0.0.1:5000/register");

            try {
                entity = new StringEntity(json.toString());
                httpPost.setEntity(entity);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                CloseableHttpResponse response = client.execute(httpPost);
                client.close();
                return response.getStatusLine().getStatusCode() == 201;

            } catch (IOException e) {
                e.printStackTrace();
            }

            // validaciones
        } else {
            // caso login
            httpPost = new HttpPost("http://127.0.0.1:5000/login");

            try {
                entity = new StringEntity(json.toString());
                httpPost.setEntity(entity);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                CloseableHttpResponse response = client.execute(httpPost);
                client.close();
                return response.getStatusLine().getStatusCode() == 200;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static List<String> getCategories() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://127.0.0.1:5000/categories");
        List<String> categories = new ArrayList<>();
        try {
            HttpResponse httpresponse = httpclient.execute(httpget);
            var entity = httpresponse.getEntity();
            StringBuilder builder = new StringBuilder();

            if (entity != null) {
                InputStream inputStream = entity.getContent();
                var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                for (String line; (line = bufferedReader.readLine()) != null;) {
                    builder.append(line).append("\n");
                }
                // Exception getting thrown in below line
                JSONArray jsonArray = new JSONArray(builder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {

                    String jsonString = jsonArray.getString(i);
                    categories.add(jsonString);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public static List<String> showCategories(boolean conIndice) {
        List<String> categories = getCategories();
        System.out.println();
        System.out.println("╔═════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                Géneros                              ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        var num = 1;

        for (var category : categories) {
            if(conIndice){
                System.out.print(formatStringSize(num + "." + category, 15));
            }else{
                System.out.print(formatStringSize("•" + category, 15));
            }

            if(num%5==0)
                System.out.println();
            num++;
        }
        System.out.println();
        System.out.println();

        return categories;
    }

    public static void selectFavCategories() {
        boolean isValid;
        boolean reintentar = true;
        while(reintentar){
            List<String>categories = showCategories(true);
            System.out.println("╔═════════════════════════════════════════════════════════════════════╗");
            System.out.println("║    Ingresa el índice de tus generos favoritos separadas por comas   ║");
            System.out.println("╚═════════════════════════════════════════════════════════════════════╝");
            System.out.println("Ingresa x para omitir");
            Scanner in = new Scanner(System.in);
            String options = in.nextLine();
            options = options.toLowerCase();

            if (!options.contains("x")) {
                String[] selectedCategoriesIndex = options.split(",");
                // Eliminar numeros duplicados
                LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>(List.of(selectedCategoriesIndex));
                // Get back the array without duplicates
                selectedCategoriesIndex = linkedHashSet.toArray(new String[] {});
                isValid = isValidCategories(selectedCategoriesIndex, categories.size());

                if (isValid) {
                    List<String> selectedCategories = new ArrayList<>();
                    for (String categoryIndex : selectedCategoriesIndex) {

                        var cat = categories.get(Integer.parseInt(categoryIndex) - 1);
                        System.out.println(cat);
                        selectedCategories.add(cat);
                    }

                    setFavCategories(selectedCategories);
                    // no debe de reintentar nuevamente
                    reintentar = false;
                }

            }
        }
    }

    public static boolean isValidCategories(String[] categories, int maxValue) {

        try {
            for (var category : categories) {

                if (Integer.parseInt(category) > maxValue || Integer.parseInt(category) < 1) {
                    // chequear que no hayan numeros que no existan en las categorias
                    System.out.println("Ingresa categorias validas");
                    return false;
                }
            }
        } catch (NumberFormatException ne) {
            System.out.println("Ingresa los indices de las categorias");
            return false;
        }

        return true;
    }

    public static void setFavCategories(List<String> categories) {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://127.0.0.1:5000/categories");
        JSONObject json = new JSONObject();
        json.put("favCategories", categories);
        json.put("username", user);
        StringEntity entity;
        try {
            entity = new StringEntity(json.toString());
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            client.execute(httpPost);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception e) {
            // Handle any exceptions.
        }
    }

    public static Boolean sendCSVMovies(String path) throws FileNotFoundException {

        List<Movie> movies = new CsvToBeanBuilder<Movie>(new FileReader(path))
                .withType(Movie.class)
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .build()
                .parse();

        for (var movie: movies) {
            movie.TrimAll();
            movie.getActorsList();
        }

        JSONObject json = new JSONObject();
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://127.0.0.1:5000/movies");

            json.put("movieList", new Gson().toJson(movies));
            StringEntity entity = new StringEntity(json.toString());
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            client.close();

            return response.getStatusLine().getStatusCode() == 201;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String formatStringSize(String text, int number) {
        return StringUtils.center(text, number);
    }

    public static String formatStringHorizontal(int number) {
        return StringUtils.repeat("═", number);
    }
}
