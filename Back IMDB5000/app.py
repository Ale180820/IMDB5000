# app.py
from flask import Flask, request, jsonify
from firebase import firebase
from cryptography.fernet import Fernet
import imdb_recommender as recommender

app = Flask(__name__)
firebase = firebase.FirebaseApplication(
    "https://proyecto-ia-ba83a-default-rtdb.firebaseio.com", None)

fernet = Fernet(b'zQg_8z-Jj1dv1Zk-DZRXn8W7tZFvS0l7y-fF8-f-zJg=')

# @app.get("/countries")
# def get_countries():
#     firebase.put('/', "Usuarios", "Eduarso biyeda (:")
#     return jsonify(countries)


# @app.post("/countries")
# def add_country():
#     if request.is_json:
#         country = request.get_json()
#         country["id"] = _find_next_id()
#         countries.append(country)
#         return country, 201
#     return {"error": "Request must be JSON"}, 415

def check_if_user_exists(users, username):
    if users == None:
        return False
    return users.get(username) != None


@app.post("/register")
def register_user():
    if request.is_json:
        req = request.get_json()
        username, password = req["username"], req["password"]
        password = fernet.encrypt(password.encode())
        password = password.decode('utf-8')
        if username != None and password != None:
            users = firebase.get('/Users', '')
            if not check_if_user_exists(users, username):
                if users == None:
                    users = {}
                users[username] = {
                    "password": password
                }
                firebase.put('/', 'Users', users)
                return jsonify(users[username]), 201
            return {"error": "User already exists"}, 403
        return 500
    return {"error": "Request must be JSON"}, 415


@app.post("/login")
def login():
    if request.is_json:
        req = request.get_json()
        username, password = req["username"], req["password"]
        if username != None and password != None:
            users = firebase.get('/Users', '')
            if check_if_user_exists(users, req["username"]):
                current_pass = users[username]["password"]
                current_pass = current_pass.encode('utf-8')
                current_pass = fernet.decrypt(current_pass).decode()
                if password == current_pass:
                    return {"msg": "Welcome back, fella (:"}, 200
        return {"error": "Wrong credentials"}, 403
    return {"error": "Request must be JSON"}, 415


@app.post("/movies")
def add_movies():
    if request.is_json:
        movies = request.get_json()
        print(movies)
        movies = movies["movieList"]
        print(movies)
        #firebase.put('/', 'Movies', movies)
        return movies, 201
    return {"error": "Request must be JSON"}, 415


@app.get("/recommend")
def recommend():
    return jsonify(recommender.recommend([], []))


@app.get("/movies")
def search_movies():
    category, query = request.args.get("category"), request.args.get("query")
    movies = firebase.get('/Movies', '')
    if movies != None:
        movies = list(filter(lambda m: m[category] == query, movies))
        return jsonify(movies)
    return {"error": "No movies found"}, 418


def get_db_categories():
    movies = firebase.get('/Movies')
    categories = []
    for movie in movies:
        for category in movie.categories:
            if category not in categories:
                categories.append(category)
    return categories


@app.get("/categories")
def get_categories():
    return jsonify(get_db_categories())


@app.post("/categories")
def set_fav_categories():
    if request.is_json:
        req = request.get_json()
        categories, username = req["favCategories"], req["username"]
        user = firebase.get('/Users/'+username, '')
        if user != None:
            user["favCategories"] = categories
            firebase.put('/Users', username, user)
            return jsonify(user["favCategories"]), 201
        return {"error": "You don't have acces, srry ):"}, 403
    return {"error": "Request must be JSON"}, 415


@app.post("/movies/grading")
def grading_movie():
    if request.is_json:
        req = request.get_json()
        username, movie, grade = req["username"], req["movie"], req["grade"]
        users = firebase.get('/Users', '')
        if check_if_user_exists(users, username):
            movies = users[username]["movies"]
            movies[movie] = grade
            firebase.put('/', 'Users', users)
        return 201
    return {"error": "Request must be JSON"}, 415
