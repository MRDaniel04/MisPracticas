import os
import mariadb # O import mysql.connector, psycopg2, etc.
from flask import current_app, g

def get_db():
#Abrir conexion con la base de datos
    if 'db' not in g:
        try:
            g.db = pymysql.connect(
	    host=current_app.config['DB_HOST'],
	    user=current_app.config['DB_USER'],
	    password=current_app.config['DB_PASSWORD'],
	    database=current_app.config['DB_NAME'],
	    port=current_app.config['DB_PORT'],
	    cursorclass=pymysql.cursors.DictCursor # Devuelve filas como diccionarios
	    )
        except pymysql.Error as e:
            print(f"Error conectando a MariaDB: {e}")
            g.db = None # Marcar que la conexión falló
    return g.db

def close_db(e=None):
#Cierra la conexión a la base de datos al finalizar la solicitud.
	db = g.pop('db', None)
	if db is not None:
		db.close()

def init_app(app):
    # Registrar funciones para abrir/cerrar DB con la app
    # app.teardown_appcontext se llama automáticamente al final de cada request
    app.teardown_appcontext(close_db)
