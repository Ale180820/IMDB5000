import numpy as np
import pandas as pd
from random import random

def recommend(genres, movies):
    df = pd.read_csv("movie_metadata.csv")
    df = df.fillna(value="")
    df = df.loc[:, ["movie_title", "title_year", "genres", "director_name", "actor_1_name", "actor_2_name", "actor_3_name", "plot_keywords", "language", "imdb_score"]]
    if not genres and not movies:
        return blind_recommend(df)
    elif not movies:
        return pre_recommend(df, genres)
    return full_recommend(df, genres, movies)

def blind_recommend(df):
    result = pd.DataFrame(
        {
            'name': '',
            'year': 0,
            'score': np.array([0.0] * 10, dtype="float64")
        }
    )
    list_genres = []
    i = 0
    while (i < len(df.index)):
        df.iloc[i, 9] = df.iloc[i, 9] * desv() #Se agrega una ligera desviación para no dar siempre el mismo resultado
        i += 1
    i = 0
    j = 0
    df = df.sort_values(by='imdb_score', ascending =False)
    while (i < 10 and j < len(df.index)):
        genres = df.iloc[j, 2].split('|') # Géneros
        if contains_genres(list_genres, genres):
            list_genres.extend(genres)
            result.iloc[i, 0] = df.iloc[j, 0] # Título
            result.iloc[i, 1] = df.iloc[j, 1] # Año
            result.iloc[i, 2] = df.iloc[j, 9] / 10 # IMDB
            i += 1
        j += 1
    return result

def pre_recommend(df, genres):
    result = pd.DataFrame(
        {
            'name': '',
            'year': 0,
            'score': np.array([0.0] * 10, dtype="float64")
        }
    )
    i = 0
    while (i < len(df.index)):
        # Se le otorga un peso del 90% al gusto en géneros, y un peso del 10% al punteo de IMDB
        score = (genre_score(genres, df.iloc[i, 2].split('|')) * 0.9 + df.iloc[i, 9] / 100) * desv() # géneros + IMDB
        if score > result.iloc[9, 2]: # punteo total
            result.iloc[9, 0] = df.iloc[i, 0] # Título
            result.iloc[9, 1] = df.iloc[i, 1] # Año
            result.iloc[9, 2] = score
            result = result.sort_values(by='score', ascending=False)
        i += 1
    return result

def full_recommend(df, initial_genres, movies):
    result = pd.DataFrame(
        {
            'name': '',
            'year': 0,
            'score': np.array([0.0] * 10, dtype="float64")
        }
    )
    #La primera posición es para ham, y la segunda para spam
    genres = [initial_genres * 2, []] #Se multiplica para aumentar el peso de los géneros iniciales
    keywords = [[], []]
    actors = [[], []]
    directors = [[], []]
    languages = [[], []]

    #Matriz para obtener el peso dado el punteo de la película
    weight = [1, 5, 4, 3, 2, 1, 1, 2, 3, 4, 5]
    # Recopilación de datos de películas
    i = 0
    while (i < len(df.index)):
        '''
        Distribución del peso:
        - 40% géneros
        - 45% palabras clave
        - 10% actores
        -  3% director
        -  2% calificación IMDB
        '''
        if df.iloc[i, 0] in movies.keys():
            x = 0
            if movies[df.iloc[i, 0]] < 6:
                x = 1
            genres[x] = genres[x] + df.iloc[i, 2].split('|') * weight[movies[df.iloc[i, 0]]] #géneros
            keywords[x] = keywords[x] + df.iloc[i, 7].split('|')  * weight[movies[df.iloc[i, 0]]] #palabras clave
            actors[x] = actors[x] + [df.iloc[i, 4]] * weight[movies[df.iloc[i, 0]]] + [df.iloc[i, 5]] * weight[movies[df.iloc[i, 0]]] + [df.iloc[i, 6]] * weight[movies[df.iloc[i, 0]]] #actores
            directors[x] = directors[x] + [df.iloc[i, 3]] * weight[movies[df.iloc[i, 0]]] #director
            languages[x] = languages[x] + [df.iloc[i, 8]] * weight[movies[df.iloc[i, 0]]] #idioma
        i += 1
    
    #Creación de tablas de frecuencias
    #La primera posición es para ham, y la segunda para spam
    f_genres = [crear_tabla_frecuencias(genres[0]), crear_tabla_frecuencias(genres[1])]
    f_keywords = [crear_tabla_frecuencias(keywords[0]), crear_tabla_frecuencias(keywords[1])]
    f_actors = [crear_tabla_frecuencias(actors[0]), crear_tabla_frecuencias(actors[1])]
    f_directors = [crear_tabla_frecuencias(directors[0]), crear_tabla_frecuencias(directors[1])]
    f_languages = [crear_tabla_frecuencias(languages[0]), crear_tabla_frecuencias(languages[1])]
    
    #Cálculo de totales
    t_genres = [cant_palabras(f_genres[0]), cant_palabras(f_genres[1])]
    t_keywords = [cant_palabras(f_keywords[0]), cant_palabras(f_keywords[1])]
    t_actors = [cant_palabras(f_actors[0]), cant_palabras(f_actors[1])]
    t_directors = [cant_palabras(f_directors[0]), cant_palabras(f_directors[1])]
    t_languages = [cant_palabras(f_languages[0]), cant_palabras(f_languages[1])]
    
    #Cálculo de probabilidades
    #La primera posición es para ham, y la segunda para spam
    p_genres = [t_genres[0] / (t_genres[0] + t_genres[1]), t_genres[1] / (t_genres[0] + t_genres[1])]
    p_keywords = [t_keywords[0] / (t_keywords[0] + t_keywords[1]), t_keywords[1] / (t_keywords[0] + t_keywords[1])]
    p_actors = [t_actors[0] / (t_actors[0] + t_actors[1]), t_actors[1] / (t_actors[0] + t_actors[1])]
    p_directors = [t_directors[0] / (t_directors[0] + t_directors[1]), t_directors[1] / (t_directors[0] + t_directors[1])]
    p_languages = [t_languages[0] / (t_languages[0] + t_languages[1]), t_languages[1] / (t_languages[0] + t_languages[1])]
    
    #Cálculo de cpt
    #La primera posición es para ham, y la segunda para spam
    cpt_genres = [f_a_probabilidad(f_genres[0], t_genres[0]), f_a_probabilidad(f_genres[1], t_genres[1])]
    cpt_keywords = [f_a_probabilidad(f_keywords[0], t_keywords[0]), f_a_probabilidad(f_keywords[1], t_keywords[1])]
    cpt_actors = [f_a_probabilidad(f_actors[0], t_actors[0]), f_a_probabilidad(f_actors[1], t_actors[1])]
    cpt_directors = [f_a_probabilidad(f_directors[0], t_directors[0]), f_a_probabilidad(f_directors[1], t_directors[1])]
    cpt_languages = [f_a_probabilidad(f_languages[0], t_languages[0]), f_a_probabilidad(f_languages[1], t_languages[1])]
    
    i = 0
    while (i < len(df.index)):
        '''
        Distribución del peso:
        - 40% géneros
        - 45% palabras clave
        - 10% actores
        -  3% director
        -  2% calificación IMDB
        '''
        if df.iloc[i, 0] not in movies.keys():
            score = calc_score(df.iloc[i], cpt_genres, cpt_keywords, cpt_actors, cpt_directors, cpt_languages, p_genres, p_keywords, p_actors, p_directors, p_languages)
            if score > result.iloc[9, 2]: # punteo total
                result.iloc[9, 0] = df.iloc[i, 0] # Título
                result.iloc[9, 1] = df.iloc[i, 1] # Año
                result.iloc[9, 2] = score
                result = result.sort_values(by='score', ascending=False)
        i += 1
    return result

def desv():
    return (random() / 10) + 0.9 # Devuelve una desviación entre 90% y 100%

def contains_genres(list_genres, genres):
    for genre in genres:
        if genre not in list_genres:
            return True
    return False

def genre_score(list_genres, genres):
    score = 0
    for genre in genres:
        if genre in list_genres:
            score += 1
        else:
            score -= 1
    return (score + len(genres)) / (2 * len(genres))

def crear_tabla_frecuencias(tokens):
    frecuencias = {}
    for token in tokens:
        if token not in frecuencias.keys():
            frecuencias[token] = 1
        else:
            frecuencias[token] += 1
    return frecuencias

def cant_palabras(tabla):
    cant = 0
    for f in tabla:
        cant += tabla[f]
    return cant

def f_a_probabilidad(frecuencias, total):
    cpt_equivalente = {}
    for k,v in frecuencias.items():
        probabilidad = v / total
        cpt_equivalente[k] = probabilidad
    return cpt_equivalente

def suav(token, cpt):
    if token in cpt.keys():
        return cpt[token]
    else:
        return  0

def score_bow(listado, cpt, p):
    tokens = listado.split("|")
    p_l_ham = 1
    for token in tokens:
        p_l_ham *= suav(token, cpt[0])
    p_l_spam = 1
    for token in tokens:
        p_l_spam *= suav(token, cpt[1])
    val = (p_l_ham * p[0]) + (p_l_spam * p[1])
    if val == 0:
        return 0
    else:
        return (p_l_ham * p[0]) / val

def calc_score(movie, cpt_genres, cpt_keywords, cpt_actors, cpt_directors, cpt_languages, p_genres, p_keywords, p_actors, p_directors, p_languages):
    score = 0
    score += score_bow(movie.iloc[2], cpt_genres, p_genres) * 0.2        # 20% géneros
    score += score_bow(movie.iloc[7], cpt_keywords, p_keywords) * 0.225   # 22.5% palabras clave
    score += score_bow(movie.iloc[4] + '|' + movie.iloc[5] + '|' + movie.iloc[6], cpt_actors, p_actors) * 0.05 # 5% actores
    score += score_bow(movie.iloc[3], cpt_directors, p_directors) * 0.015 #  1.5% directores
    score += score_bow(movie.iloc[8], cpt_languages, p_languages) * 0.5 #  50% idioma
    score += movie.iloc[9] / 1000                                        #  1% calificación IMDB
    return score * desv()