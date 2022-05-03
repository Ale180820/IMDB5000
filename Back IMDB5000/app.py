# app.py
from distutils import errors
from flask import Flask, request, jsonify
from firebase import firebase
from cryptography.fernet import Fernet
import json

from matplotlib.pyplot import title
import imdb_recommender as recommender

import numpy as np
import pandas as pd

app = Flask(__name__)
firebase = firebase.FirebaseApplication(
    "https://proyecto-ia-ba83a-default-rtdb.firebaseio.com", None)

fernet = Fernet(b'zQg_8z-Jj1dv1Zk-DZRXn8W7tZFvS0l7y-fF8-f-zJg=')


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
        movies = movies["movieList"]
        movies = json.loads(movies)

        dbMovieList = []
        for movie in movies:
            if movie.get("movie_title") != None:
                standardActors = []
                for actor in movie.get("actors"):
                    standardActors.append(actor)
                newMovie = {
                    "color": movie.get("color"),
                    "movie_title": movie.get("movie_title"),
                    "director_name": movie.get("director_name"),
                    "genres": movie.get("genres").split("|"),
                    "plot_keywords": movie.get("plot_keywords"),
                    "language": movie.get("language"),
                    "imdb_score": movie.get("imdb_score"),
                    "plot_keywords": movie.get("plot_keywords").split("|"),
                    "title_year": movie.get("title_year"),
                    "actors": standardActors,
                }
                dbMovieList.append(newMovie)
        firebase.put('/', 'Movies', dbMovieList)
        return {"msg": "Movies uploaded (:"}, 201
    return {"error": "Request must be JSON"}, 415


@app.get("/recommend")
def recommend():
    username = request.args.get("username")
    movies = firebase.get('/Movies', '')
    for movie in movies:
        movie["actor_1_name"] = movie["actors"][0]
        movie["actor_2_name"] = movie["actors"][1]
        movie["actor_3_name"] = movie["actors"][2]
        movie["plot_keywords"] = "|".join(movie["plot_keywords"])
        movie["genres"] = "|".join(movie["genres"])
        movie["movie_title"]
    df = pd.DataFrame(movies)
    list_genres = []
    list_movies = {}
    if username != None:
        user = firebase.get(f'/Users/{username}', '')
        if "favCategories" in user.keys():
            list_genres = user["favCategories"]
        if "movies" in user.keys():
            list_movies = user["movies"]
    result = recommender.recommend(df, list_genres, list_movies)
    return jsonify(result.to_dict('records')), 200


@app.get("/movies")
def search_movies():
    category, query = request.args.get("category"), request.args.get("query")
    movies = firebase.get('/Movies', '')
    movieResult = []
    reverse = True
    if movies != None and query != None and category != None:
        if category == 'imdb_score':
            query = float(query)
            movies = [movie for movie in movies if float(movie.get(
                category)) + 1 >= query and float(movie.get(
                    category)) - 1 <= query]
        elif category in ["plot_keywords", "genres", "actors"]:
            for movie in movies:
                categoryResult = movie.get(category)
                if categoryResult != None:
                    for elem in categoryResult:
                        if query in elem:
                            print(query, elem)
                            movieResult.append(movie)
                            break
            for movie in movieResult:
                movie["plot_keywords"] = "|".join(movie.get("plot_keywords"))
                movie["genres"] = "|".join(movie.get("genres"))
            return jsonify(movieResult), 200
        else:
            query = query.lower()
            movies = [movie for movie in movies if query in movie.get(
                category).lower()]
            reverse = False
        for movie in movies:
            movie["plot_keywords"] = "|".join(movie.get("plot_keywords"))
            movie["genres"] = "|".join(movie.get("genres"))
        movies.sort(key=lambda x: x.get(category), reverse=reverse)
        return jsonify(movies), 200
    return {"error": "No movies found"}, 418


def get_db_categories():
    movies = firebase.get('/Movies', '')
    categories = []
    for movie in movies:
        for category in movie.get("genres"):
            if category not in categories:
                categories.append(category)
    categories.sort()
    return categories


@app.get("/categories")
def get_categories():
    print(get_db_categories())
    return jsonify(get_db_categories()), 200


@app.post("/categories")
def set_fav_categories():
    if request.is_json:
        req = request.get_json()
        categories, username = req["favCategories"], req["username"]
        user = firebase.get(f'/Users/{username}', '')
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
            movies = users[username].get("movies")
            if movies == None:
                users[username]["movies"] = {
                    movie: grade
                }
            else:
                users[username]["movies"][movie] = grade
            firebase.put('/', 'Users', users)
        return {"username": username, "movie": movie, "grade": grade}, 200
    return {"error": "Request must be JSON"}, 415
