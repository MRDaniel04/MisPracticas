import pymysql
from flask import Flask, g, jsonify, current_app, render_template,request
from config import config_by_name
#from .api import products, auth, menu, cart

# Explicaciones personales:
# El objeto g es un objeto en Flask que actua como espacio de almacenamiento temporal, que solo existe durante una solicitud individual
	# Sirve para guardar y compartir datos que se puedan usar en distintos puntos del codigo
# @app.route es un decorador python, este añade funcionalidad a metodos
	# asocia una ruta con metodos HTTP (cuando a Flask le llegue la ruta indicada ejecutará la función)
# El objeto cursor crea la conexion a la base de datos, obteniendo las lineas necesarias

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

def create_app(config_name='default'):
    app = Flask(__name__)

    # Cargar la configuración desde el objeto importado
    # Necesitamos acceso a 'current_app' dentro de get_db, por eso cargamos config aquí
    app.config.from_object(config_by_name[config_name])

    # Registrar funciones para abrir/cerrar DB con la app
    # app.teardown_appcontext se llama automáticamente al final de cada request
    app.teardown_appcontext(close_db)


    # Registrar Blueprints de la API para que la app sepa que existen los diferentes módulos de rutas definidos en /api
    from .api import auth
    app.register_blueprint(auth.auth_bp, url_prefix='/api')

    from .api import products
    app.register_blueprint(products.products_bp, url_prefix='/api')

    from .api import cart
    app.register_blueprint(cart.cart_bp, url_prefix='/api')

    from .api import orders
    app.register_blueprint(orders.orders_bp, url_prefix='/api')

    from .api import menu
    app.register_blueprint(menu.menu_bp, url_prefix='/api')


    @app.route('/registro')
    def registro_page():
        return render_template('crearCuenta.html',page_title="Crear Cuenta") 
    
    @app.route('/login')
    def login_page():
        return render_template('inicioSesion.html',page_title="Inicio de Sesión") 
		
    @app.route('/menu')
    def menu_page():
        return render_template('menuPrueba.html',page_title="Menú")

    @app.route('/productos')
    def productos_page():
        return render_template('productosPrueba.html',page_title="Productos")

    @app.route('/carrito')
    def carrito_page():
        return render_template('carritoPrueba.html',page_title="Carrito")

    @app.route('/orders')
    def pedidos_page():
        return render_template('pedidosPrueba.html',page_title="Pedidos")
    
    @app.route('/editar')
    def editar_page():
        product_idurl_from_query = request.args.get('idurl')
        if not product_idurl_from_query:
            return "Error: ID de producto no especificado para editar.", 400
        return render_template('editarProductos.html',page_title="Editar Producto", product_idurl_from_editing=product_idurl_from_query)


    # Ruta simple de prueba (sin acceso a BD)
	# Cuando llegue una solicitud http a la ruta /hello (get por defecto), ejecutará la función hello
    @app.route('/hello')
    def hello():
        return 'Hello, World from Arte Visual App (Direct DB Mode)!'

    # Ruta de prueba para verificar conexión a BD
	# Cuando llegue una solicitud http a la ruta /db_test(get por defecto), ejecutará la función db_test	
    @app.route('/db_test')
    def db_test():
        db_conn = get_db()
        if db_conn is None:
            return jsonify({"status": "error", "message": "No se pudo conectar a la base de datos"}), 500

        try:
            with db_conn.cursor() as cursor:
                cursor.execute("SELECT VERSION()")
                version = cursor.fetchone()
            return jsonify({"status": "success", "db_version": version})
        except pymysql.Error as e:
             return jsonify({"status": "error", "message": f"Error en consulta: {e}"}), 500
    return app
