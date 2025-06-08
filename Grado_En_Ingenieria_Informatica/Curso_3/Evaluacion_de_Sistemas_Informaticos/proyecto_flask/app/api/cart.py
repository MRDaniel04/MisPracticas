from flask import Blueprint, jsonify, request, session, abort, render_template
from .. import get_db
from decimal import Decimal
from functools import wraps
import traceback # Movido aquí para estar disponible globalmente si se necesita en varios sitios, o dejarlo en el except. Por convención, mejor arriba.

# Definimos
cart_bp = Blueprint('cart', __name__)

# Definimos la funcion para verificar si el usuario esta logueado
def login_required(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if 'user_id' not in session:
            return jsonify({"error": "Unauthorized", "message": "Se necesita de login"}), 401
        return f(*args, **kwargs)
    return decorated_function

# Definimos la funcion para añadir un producto al carrito
# Endpoint: POST /api/carrito/agregar
@cart_bp.route('/cart/add', methods=['POST'])
@login_required
def add_to_cart():
    # Obtenemos el id del usuario que va a intentar añadir productos a su carrito
    user_id = session['user_id']
    print(f"Usuario {user_id} intentando añadir producto.")

    # Obtenemos los datos del producto a añadir al carrito
    data = request.get_json()
    if not data:
        return jsonify({"error": "Bad Request", "message": "Request no sigue el formato especifico"}), 400

    # Obtenemos los datos especificos del producto una vez que ya sabemos que estan en el formato correcto
    product_id = data.get('product_id')
    quantity = data.get('quantity', 1)

    # Comprobamos que los datos del producto sean validos
    if not product_id or not isinstance(product_id, int) or product_id <= 0:
        return jsonify({"error": "Bad Request", "message": "Product_id inválido"}), 400
    if not isinstance(quantity, int) or quantity <= 0:
        return jsonify({"error": "Bad Request", "message": "Quantity inválida"}), 400

    # Inicializamos los valores de la base de datos y el cursor para realizar la consulta
    db = None
    cursor = None

    try:
        # Obtenemos la conexion con la base de datos
        db = get_db()
        
        # Devuelve error en caso de que no se pueda obtener
        if db is None:
            return jsonify({"error": "Internal Server Error", "message": "Conexion fallida con base de datos"}), 500

        # Usamos el objeto cursor que nos permite obtener los valores de la consulta
        cursor = db.cursor()
            
        if cursor is None:
            return jsonify({"error": "Internal Server Error", "message": "Conexion fallida con base de datos"}), 500
        # Ejecutamos la consulta SQL para obtener el producto especificado por su id
        cursor.execute("""SELECT id, stock FROM products WHERE id = %s""", (product_id,))
        product = cursor.fetchone()

        # Comprobamos que el producto exista en la base de datos
        if not product:
            print(f"Comprobar que no existe el producto en la base de datos: {product_id}")
            return jsonify({"error": "Not Found", "message": f"Producto con id {product_id} no encontrado"}), 404

        # Verificamos si el item ya se encuentra en el carrito del usuario
        # En caso de que se encuentre, actualizamos la cantidad en lugar de añadir un nuevo item
        cursor.execute("""SELECT id, quantity FROM cart_items WHERE user_id = %s AND product_id = %s""", (user_id, product_id))
        cart_item = cursor.fetchone()
        needed_stock = quantity
            
        # Obtenemos la cantidad de producto
        if cart_item:
            needed_stock += cart_item['quantity']

        # Hay que asegurarse de que la cantidad de producto sea menor o igual al stock disponible
        print("Antes de comprobar que hay stock")
        if needed_stock > product['stock']:
            return jsonify({"error": "Bad Request", "message": "No hay suficiente stock disponible"}), 400

        # Actualizar el carrito en caso de que el usuario ya tenga el producto en el carrito
        if cart_item:
            new_quantity = cart_item['quantity'] + quantity
            cursor.execute("""UPDATE cart_items SET quantity = %s WHERE id = %s""", (new_quantity, cart_item['id']))
            print(f"Producto {product_id} actualizado en el carrito del usuario {user_id}")
        # Añadir el producto al carrito en caso de que el usuario no tenga el producto en el carrito
        else:
            cursor.execute("""INSERT INTO cart_items (user_id, product_id, quantity) VALUES (%s, %s, %s)""", (user_id, product_id, quantity))
            print(f"Producto {product_id} añadido al carrito del usuario {user_id} con cantidad {quantity}")
    
        # Confirmamos los cambios en la base de datos
        db.commit()
    
        # Devolvemos el carrito del usuario
        return jsonify({"message": "Producto añadido al carrito"}), 200
    except pymysql.err.OperationalError as e:
        if db: # Asegurarse de que db existe antes de hacer rollback
            db.rollback() # ¡Muy importante revertir en caso de error de BD!
        
        error_code, _ = e.args # e.args es una tupla, el primer elemento es el código de error
        if error_code == 1213: # Código de error de Deadlock para MySQL
            current_retry += 1
            print(f"Deadlock detectado. Reintentando ({current_retry}/{max_retries})... Error: {e}")
            if current_retry >= max__retries:
                print("Máximo de reintentos alcanzado por deadlock.")
                # Devolver un error específico o relanzar para el manejador general
                return jsonify({"error": "Service Unavailable", "message": "Error temporal en el servidor debido a deadlock, por favor intente de nuevo."}), 503
            
            # Espera exponencial para dar tiempo a que se resuelva el conflicto
            # (0.1s, 0.2s, 0.4s...)
            time.sleep(0.1 * (2**current_retry))
            # continue implícito al final del bloque del bucle while
        else:
            # Otro OperationalError, no es deadlock, así que no reintentamos esto.
            # Lo dejamos caer al manejador de excepciones general.
            print(f"Error operacional de base de datos (no deadlock): {e}")
            traceback.print_exc()
            return jsonify({"error": "Internal Server Error", "message": "Error de base de datos al añadir producto al carrito"}), 500
    except Exception as e:
        if db:
            db.rollback()
        print(f"Error al añadir producto al carrito: {e}")
        traceback.print_exc() # Útil para debugging
        return jsonify({"error": "Internal Server Error", "message": "Error al añadir producto al carrito"}), 500
    finally:
        if cursor:
            cursor.close()

# Endpoint que permite obtener el contenido del carrito de compras de un usuario
# Endpoint: GET /api/carrito/
@cart_bp.route('/carrito', methods=['GET'])
@login_required
def view_cart():
    # Obtenemos el id del usuario que va a intentar añadir productos a su carrito
    user_id = session['user_id']
    print(f"Usuario {user_id} intentando ver su carrito.")

    # Inicializamos los valores de la base de datos y el cursor para realizar la consulta
    db = None
    cursor = None
    try:
        # Obtenemos la conexion con la base de datos
        db = get_db()

        # Devuelve error en caso de que no se pueda obtener
        if db is None:
            return jsonify({"error": "Internal Server Error", "message": "Conexion fallida con base de datos"}), 500

        # Usamos el objeto cursor que nos permite obtener los valores de la consulta
        cursor = db.cursor()

        # Ejecutamos la consulta SQL para obtener los items del carrito del usuario
        cursor.execute("""SELECT ci.quantity, p.id, p.idurl, p.title, p.price, p.image_url FROM cart_items ci JOIN products p ON ci.product_id = p.id WHERE ci.user_id = %s""", (user_id,))
        cart_items_raw = cursor.fetchall()

        # Obtenemos la suma total
        total_price = Decimal('0.0')
        cart_items_processed = []

        # Procesamos los datos para asegurarnos de que los precios sean float y no Decimal
        # Ademas los añadimos a la lista cart_items_processed una vez que ya han sido procesados
        for item in cart_items_raw:
            if 'price' in item and isinstance(item['price'], Decimal):
                price_float = float(item['price'])
                total_price += item['price'] * item['quantity']
                item['price'] = price_float # Modificar el item directamente puede ser confuso, mejor crear uno nuevo o usar una copia. Pero solo se pide indentación.
            cart_items_processed.append(item)
        
        # Print para asegurarnos de que los datos obtenidos sean correctos
        print(f"Usuario {user_id} ha obtenido su carrito con {len(cart_items_processed)} productos. Total: {total_price}")

        # Devolvemos el carrito del usuario
        return jsonify({"cart_items": cart_items_processed, "total_price": float(total_price)}), 200

    # En caso de fallo
    except Exception as e:
        # import traceback # Ya está importado arriba
        traceback.print_exc()
        print(f"Error al obtener el carrito: {e}")
        return jsonify({"error": "Internal Server Error", "message": "Error al obtener el carrito"}), 500
    
    # Hacer siempre
    finally:
        if cursor:
            cursor.close()  

# Endpoint que permite vaciar el carrito
# DELETE /api/carrito/vaciar
@cart_bp.route('/carrito/vaciar', methods=['DELETE']) 
@login_required
def empty_cart():
    # Obtenemos el id del usuario que va a intentar añadir productos a su carrito
    user_id = session['user_id']
    print(f"Usuario {user_id} intentando vaciar su carrito.")

    # Inicializamos los valores de la base de datos y el cursor para realizar la consulta
    db = None
    cursor = None

    try:
        # Obtenemos la conexion con la base de datos
        db = get_db()

        # Devuelve error en caso de que no se pueda obtener
        if db is None:
            return jsonify({"error": "Internal Server Error", "message": "Conexion fallida con base de datos"}), 500
        
        # Usamos el objeto cursor que nos permite obtener los valores de la consulta
        cursor = db.cursor()

        # Eliminamos todos los items del carrito para ese usuario
        # cursor.execute devuelve el número de filas afectadas, no el resultado.
        cursor.execute("DELETE FROM cart_items WHERE user_id = %s", (user_id,))
        rows_deleted = cursor.rowcount # Así se obtiene el número de filas
        db.commit()

        # Print para asegurarnos de que los datos obtenidos sean correctos
        print(f"Usuario {user_id}. Se vacio el carrito. Filas eliminadas: {rows_deleted}")

        # Devolvemos el carrito del usuario
        return jsonify({"message": "Carrito vaciado"}), 200

    # En caso de fallo
    except Exception as e:
        if db:
            db.rollback()
        print(f"Error al vaciar el carrito: {e}")
        traceback.print_exc()
        return jsonify({"error": "Internal Server Error", "message": "Error al vaciar el carrito"}), 500

    # Hacer siempre
    finally:
        if cursor:
            cursor.close()
