package com.url.dreamTeam;

import com.google.gson.Gson;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
    public static void main(String[] args) throws FileNotFoundException { menuPrincipal(); }

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
                    System.out.println("LOGIN CORRECTO");
                    menuPrincipal();
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
            new Scanner(System.in).nextLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Menu principal
    public static void menuPrincipal() throws FileNotFoundException {
        int option = 0;
        while (option != 3) {
            System.out.println("╔═════════════════════════════════════════════════════════╗");
            System.out.println("║══════════════════════════Menú═══════════════════════════║");
            System.out.println("║                                                         ║");
            System.out.println("║                   1. Calificar peliculas                ║");
            System.out.println("║                 2. Obtener recomendaciones              ║");
            System.out.println("║                      3. Borrar datos                    ║");
            System.out.println("║                4. Cargar csv con películas              ║");
            System.out.println("║                      5. Cerrar Sesión                   ║");
            System.out.println("║                                                         ║");
            System.out.println("╚═════════════════════════════════════════════════════════╝");
            System.out.print("Seleccione una opción:  ");
            Scanner in = new Scanner(System.in);
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
                    deleteData();
                    break;
                case 4:
                    System.out.print("Ingrese la dirección del archivo:  ");
                    var fileAddress = in.nextLine();
                    fileAddress = fileAddress.replaceAll("\"", "");
                    if (sendCSVMovies(fileAddress)){
                        System.out.println("Archivo cargado correctamente");
                    }
                    break;
                case 5:
                    initProgram();
                    break;
                default:
                    System.out.print("Opción incorrecta, intentelo nuevamente.");
                    new Scanner(System.in).nextLine();
                    clearConsole();
                    break;
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

            System.out.println("╔═════════════════════════════════════════════════════════╗");
            System.out.println("║═══════════════ BIENVENIDO A LA APLICACIÓN ══════════════║");
            System.out.println("║                                                         ║");
            System.out.println("║          USERNAME:    " + sUsername + "                      ║");
            System.out.println("║          PASSWORD:                                      ║");
            System.out.println("║                                                         ║");
            System.out.println("╚═════════════════════════════════════════════════════════╝");
            System.out.print("  Escriba su password : ");
            Scanner inPassword = new Scanner(System.in);
            String password = inPassword.nextLine();
            String cPassword = "";

            if (password.length() > 12) {
                clearConsole();
                System.out.println("Password debe tener menos de 12 caracteres");
                result = 4;
            } else {
                for (int i = 0; i < password.length(); i++) {
                    cPassword += "*";
                }

                cPassword = String.format("%-12s", cPassword);

                System.out.println("╔═════════════════════════════════════════════════════════╗");
                System.out.println("║═══════════════ BIENVENIDO A LA APLICACIÓN ══════════════║");
                System.out.println("║                                                         ║");
                System.out.println("║          USERNAME:    " + sUsername + "                      ║");
                System.out.println("║          PASSWORD:    " + cPassword + "                      ║");
                System.out.println("║                                                         ║");
                System.out.println("╚═════════════════════════════════════════════════════════╝");

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
        String sUsername = "";

        if (username.length() > 12) {
            clearConsole();
            System.out.println("Username debe tener menos de 12 caracteres");
        } else {
            sUsername = String.format("%-12s", username);

            System.out.println("╔═════════════════════════════════════════════════════════╗");
            System.out.println("║═════════════════ CREACIÓN DE USUARIOS ══════════════════║");
            System.out.println("║                                                         ║");
            System.out.println("║          USERNAME:    " + sUsername + "                      ║");
            System.out.println("║          PASSWORD:                                      ║");
            System.out.println("║                                                         ║");
            System.out.println("╚═════════════════════════════════════════════════════════╝");
            System.out.print("  Ingrese su password : ");
            Scanner inPassword = new Scanner(System.in);
            String password = inPassword.nextLine();
            String cPassword = "";

            if (password.length() > 12) {
                clearConsole();
                System.out.println("Password debe tener menos de 12 caracteres");
            } else {
                for (int i = 0; i < password.length(); i++) {
                    cPassword += "*";
                }

                cPassword = String.format("%-12s", cPassword);

                System.out.println("╔═════════════════════════════════════════════════════════╗");
                System.out.println("║═════════════════ CREACIÓN DE USUARIOS ══════════════════║");
                System.out.println("║                                                         ║");
                System.out.println("║          USERNAME:    " + sUsername + "                      ║");
                System.out.println("║          PASSWORD:    " + cPassword + "                      ║");
                System.out.println("║                                                         ║");
                System.out.println("╚═════════════════════════════════════════════════════════╝");

                // Crear cuenta, si se creo correctamente devolver 1, sino devolver 2
                result = loginCreate(sUsername.trim(), password.trim(), true);
            }
        }
        user = username;
        return result;
    }

    // 2. Rating de películas
    public static void movieRating() throws IOException {
        int option = 0;
        while (option != 2) {
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
            if (option == 1) {
                search();
            } else {
                System.out.print("Opción incorrecta, intentelo nuevamente.");
                System.in.read();
            }
        }
    }

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
        System.out.print("Ingresa la categorìa de la película [1-7]:  ");
        int category = Integer.parseInt(searchWord.nextLine());
        System.out.print("Ingresa la palabra a buscar:  ");
        String word = searchWord.nextLine();

        clearConsole();

        // Buscar pelicula
        List<Movie> movieList = MovieSearch(category, word);
        var longestMovie = Collections.max(movieList, Comparator.comparingInt((Movie o) -> o.getMovie_title().length()));

        System.out.println("╔═════════════════════════════════════════════════════════╗");
        System.out.println("║═════════════════════Buscar películas════════════════════║");
        System.out.println("║                                                         ║");
        System.out.println("║                 Resultado de la búsqueda                ║");
        for (var movie: movieList) {
            System.out.println("║" + movie.toFormattedString(longestMovie.getMovie_title().length()) + "║");
        }
        System.out.println("║                                                         ║");
        System.out.println("║           Ingresa la valoración de la película          ║");
        System.out.println("║                                                         ║");
        System.out.println("╚═════════════════════════════════════════════════════════╝");
        System.out.print("Ingresa tu valoración entre 1 y 10:  ");
        int rating = Integer.parseInt(searchWord.nextLine());
        clearConsole();

        // Valorar pelicula
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

    // 3. Recomendaciones
    public static void topMovies() {
        List<Movie> topTen = topTen();
        System.out.println("╔═════════════════════════════════════════════════════════╗");
        System.out.println("║════════════════════Top 10 de películas══════════════════║");
        for (var movie : topTen) {
            System.out.println("║" + movie.toString());
        }
        System.out.println("║                                                         ║");
        System.out.println("╚═════════════════════════════════════════════════════════╝");
        System.out.print("Presiona enter para volver al inicio.");
        Scanner option = new Scanner(System.in);
        option.nextLine();
    }

    // 4. Borrar datos
    public static void deleteData() {

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
                categorySend = "year";
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
            System.out.println(httpresponse.getStatusLine().getReasonPhrase());
            StringBuilder builder = new StringBuilder();

            if (entity.getContent() != null) {
                InputStream inputStream = entity.getContent();
                var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                for (String line = null; (line = bufferedReader.readLine()) != null;) {
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

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return topTen;
    }

    // POST - Rating
    public static void sendRating(String title, int rating) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://127.0.0.1:5000/countries");
        JSONObject json = new JSONObject();

        // json
        json.put("title", title);
        json.put("rating", rating);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // GET - Top 10
    public static List<Movie> topTen() {
        List<Movie> topTen = new ArrayList<>();
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpGet httpget = new HttpGet(new URIBuilder("http://127.0.0.1:5000/movies")
                    .addParameter("user", user)
                    .build());
            HttpResponse httpresponse = httpclient.execute(httpget);
            var entity = httpresponse.getEntity();

            StringBuilder builder = new StringBuilder();

            if (entity != null) {
                InputStream inputStream = entity.getContent();
                var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                for (String line = null; (line = bufferedReader.readLine()) != null;) {
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

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
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

        StringEntity entity = null;

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
                for (String line = null; (line = bufferedReader.readLine()) != null;) {
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

    public static List<String> showCategories() {
        List<String> categories = getCategories();
        System.out.println();
        System.out.println("╔═════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              Categorias                             ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        var num = 1;

        for (var category : categories) {
            System.out.print(num + "." + category + "   ");
            if(num%5==0)
                System.out.println();
            num++;
        }
        System.out.println();

        return categories;
    }

    public static void selectFavCategories() {
        boolean isValid;
        boolean reintentar = true;
        while(reintentar){
            List<String>categories = showCategories();
            System.out.println("╔═════════════════════════════════════════════════════════════════════╗");
            System.out.println("║  Ingresa el índice de tus categorias favoritas separadas por comas  ║");
            System.out.println("╚═════════════════════════════════════════════════════════════════════╝");
            System.out.println("Ingresa x para omitir");
            Scanner in = new Scanner(System.in);
            String options = in.nextLine();
            options = options.toLowerCase();

            if (!options.contains("x")) {
                String[] selectedCategoriesIndex = options.split(",");
                // Eliminar numeros duplicados
                LinkedHashSet linkedHashSet = new LinkedHashSet<>(List.of(selectedCategoriesIndex));
                // Get back the array without duplicates
                selectedCategoriesIndex = (String[]) linkedHashSet.toArray(new String[] {});
                isValid = isValidCategories(selectedCategoriesIndex, categories.size());

                if (isValid) {
                    List<String> selectedCategories = new ArrayList<>();
                    for (String categoryIndex : selectedCategoriesIndex) {

                        var cat = categories.get(Integer.valueOf(categoryIndex) - 1);
                        System.out.println(cat);
                        selectedCategories.add(cat);
                    }

                    setFavCategories(selectedCategories);
                    // no debe de reintentar nuevamente

                }

            } else if (options.equals("")) {
                System.out.println("Ingresa alguna de las categorias");
                //debe reintentar
            }
            //no debe de reinternar
            reintentar = false;
        }

    }

    public static boolean isValidCategories(String[] categories, int maxValue) {

        try {
            for (var category : categories) {

                if (Integer.valueOf(category) > maxValue || Integer.valueOf(category) < 1) {
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

    public final static void clearConsole() {
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

        List<Movie> movies = new CsvToBeanBuilder(new FileReader(path))
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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
