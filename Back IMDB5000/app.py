# app.py
from flask import Flask, request, jsonify
from firebase import firebase
import imdb_recommender as recommender

app = Flask(__name__)
firebase = firebase.FirebaseApplication(
    "https://proyecto-ia-ba83a-default-rtdb.firebaseio.com", None)

# countries = [
#     {"id": 1, "name": "Thailand", "capital": "Bangkok", "area": 513120},
#     {"id": 2, "name": "Australia", "capital": "Canberra", "area": 7617930},
#     {"id": 3, "name": "Egypt", "capital": "Cairo", "area": 1010408},
# ]


# def _find_next_id():
#     return max(country["id"] for country in countries) + 1


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
    return users[username] != None


@app.post("/register")
def register_user():
    if request.is_json:
        user = request.get_json()
        if user["username"] != None and user["password"] != None:
            users = firebase.get('/Users', '')
            if not check_if_user_exists(users, user["username"]):
                users[user.username] = user.password
                firebase.put('/', 'Users', users)
                return 201
            return 403
        return 500
    return {"error": "Request must be JSON"}, 415


@app.post("/login")
def login():
    if request.is_json:
        user = request.get_json()
        if user["username"] != None and user["password"] != None:
            users = firebase.get('/Users', '')
            if check_if_user_exists(users, user["username"]):
                if user["password"] == users[user["username"]].password:
                    return 200
            return 403
        return 500
    return {"error": "Request must be JSON"}, 415


@app.post("/movies")
def add_movies():
    if request.is_json:
        movies = request.get_json()
        movies = movies["movies"]
        firebase.put('/', 'Movies', movies)
        return 201
    return {"error": "Request must be JSON"}, 415


@app.get("/recommend")
def recommend():
    return jsonify(recommender.recommend([], []))


@app.post("/movies/search")
def search_movies():
    if request.is_json:
        query = request.get_json()
        value, category = query["value"], query["category"]
        movies = firebase.get('/Movies')
        movies = list(filter(lambda m: m[category] == value, movies))
        return jsonify(movies)
    return {"error": "Request must be JSON"}, 415


@app.get("/categories")
def get_categories():
    movies = firebase.get('/Movies')
    categories = []
    for movie in movies:
        for category in movie.categories:
            if category not in categories:
                categories.append(category)
    return jsonify(categories)


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
