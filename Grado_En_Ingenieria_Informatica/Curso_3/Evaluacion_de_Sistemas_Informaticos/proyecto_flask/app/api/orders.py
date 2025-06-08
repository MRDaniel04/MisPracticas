from flask import Blueprint, Flask, request, jsonify, session, abort
from .. import get_db
from decimal import Decimal
from functools import wraps
import time

#Define el Blueprint para los pedidos

orders_bp = Blueprint('pedidos', __name__)

#Esta funcion se asegura de que el usuario esté autenticado antes de permitirle ver los pedidos

def login_required(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if 'user_id' not in session:
            return jsonify({'error': 'Unauthorized'}), 401
        return f(*args, **kwargs)
    return decorated_function

#Endpoint que permite procesar la compra
#POST /api/checkout

@orders_bp.route('/checkout', methods=['POST'])
@login_required
def checkout():
    user_id = session.get('user_id')
    print(f"Usuario {user_id} iniciando checkout")

    db = None
    max_retries = 3
    current_retry = 0

    while current_retry < max_retries:
        try:
            if db is None:
                db = get_db()
            
            if db is None:
                print(f"Usuario {user_id}: Error en la base de datos al obtener conexión.")
                # Este error es crítico y no relacionado con deadlock, así que no reintentamos
                return jsonify({"error": "Internal Server Error", "message": "Database connection failed"}), 500

            with db.cursor() as cursor:
                cart_sql = """SELECT p.id, p.title, p.price, p.stock, ci.quantity 
                              FROM cart_items ci
                              JOIN products p ON ci.product_id = p.id
                              WHERE ci.user_id = %s FOR UPDATE;"""
                cursor.execute(cart_sql, (user_id,))
                cart_items = cursor.fetchall()

                if not cart_items:
                    print(f"Usuario {user_id}: Carrito vacío, no se puede procesar.")
                    return jsonify({"error": "Bad request", "message": "Carrito vacío no se puede procesar"}), 400

                total_amount = Decimal(0)
                order_items_details = [] 

                for item in cart_items:
                    if item['stock'] < item['quantity']:
                        db.rollback() 
                        print(f"Usuario {user_id}: Producto {item['title']} sin stock suficiente.")
                        return jsonify({"error": "Conflict", "message": f"Producto {item['title']} sin stock suficiente"}), 409 

                    current_price = Decimal(str(item['price'])) # Asegurar que es Decimal
                    total_amount += current_price * item['quantity']
                    order_items_details.append({'product_id': item['id'], 'quantity': item['quantity'], 'price': current_price})

                print(f"Usuario {user_id}: Stock validado. Total calculado: {total_amount}")

                order_sql = "INSERT INTO orders (user_id, total_amount, status) VALUES (%s, %s, %s);"
                cursor.execute(order_sql, (user_id, total_amount, 'completed'))
                new_order_id = cursor.lastrowid
                print(f"Usuario {user_id}: Pedido creado con ID {new_order_id}")

                if order_items_details: 
                    order_items_sql = "INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase) VALUES (%s, %s, %s, %s);"
                    order_items_values = [(new_order_id, item['product_id'], item['quantity'], item['price']) for item in order_items_details]
                    cursor.executemany(order_items_sql, order_items_values)

                if order_items_details: 
                    update_stock_sql = "UPDATE products SET stock = stock - %s WHERE id = %s;"
                    
                    update_stock_values = sorted(
                        [(item['quantity'], item['product_id']) for item in order_items_details],
                        key=lambda x: x[1] # Ordenar por product_id
                    )
                    for quantity, product_id_val in update_stock_values:
                        cursor.execute(update_stock_sql, (quantity, product_id_val))
                
                delete_cart_sql = "DELETE FROM cart_items WHERE user_id = %s;"
                cursor.execute(delete_cart_sql, (user_id,))
                print(f"Usuario {user_id}: Carrito vaciado después de procesar el pedido")
                db.commit()
                print(f"Usuario {user_id}: Pedido procesado y cambios confirmados para el pedido {new_order_id}")
                return jsonify({"message": "Pedido procesado con éxito", "order_id": new_order_id}), 200
        
        except pymysql.err.OperationalError as e:
            if db: 
                db.rollback()
            
            error_code = e.args[0]
            if error_code == 1213:
                current_retry += 1
                print(f"Usuario {user_id}: Deadlock detectado (intento {current_retry}/{max_retries}). Error: {e}")
                if current_retry >= max_retries:
                    print(f"Usuario {user_id}: Máximo de reintentos por deadlock alcanzado.")
                    return jsonify({"error": "Service Unavailable", "message": "Error temporal al procesar el pedido, por favor intente de nuevo."}), 503
                
               
                time.sleep(0.1 * (2**current_retry)) 
                continue 
            else:
             
                print(f"Usuario {user_id}: Error operacional de DB (no deadlock): {e}")
               
                return jsonify({"error": "Internal Server Error", "message": "Error de base de datos al procesar el pedido."}), 500
        
        except Exception as e:
            if db:
                db.rollback()
            print(f"Usuario {user_id}: Error inesperado durante el checkout: {e}")
           
            return jsonify({"error": "Internal Server Error", "message": "Error inesperado al procesar el pedido."}), 500
        

    
    if db: 
        db.rollback()
    print(f"Usuario {user_id}: Fallaron todos los intentos de checkout.")
    return jsonify({"error": "Internal Server Error", "message": "No se pudo procesar el pedido después de múltiples intentos."}), 500

#Endpoint que permite obtener los pedidos del usuario
#GET /api/pedidos

@orders_bp.route('/pedidos', methods=['GET'])
@login_required
def get_orders():

    #Obtenemos la conexión a la base de datos y el ID del usuario de la sesión

    user_id = session.get('user_id')
    print(f"Usuario {user_id} solicitando pedidos")
    db = None
    cursor = None
    try:
        db = get_db()
        if db is None:
            return jsonify({"error": "Internal Server Error", "message": "Database connection failed"}), 500

        cursor = db.cursor()

        # Realizamos la consulta para obtener los pedidos del usuario
        sql = """SELECT id, total_amount, status, created_at FROM orders WHERE user_id = %s ORDER BY created_at DESC;"""
        
        cursor.execute(sql, (user_id,))
        orders_raw = cursor.fetchall()

        #Convertimos todo a JSON para poder devolverlo al cliente
        #Si el campo total_amount es un Decimal, lo convertimos a float
        #Con isoformat() convertimos el campo created_at a un formato ISO 8601
        #Agregamos cada pedido a una lista de pedidos

        order_list = []
        for order in orders_raw:
            if 'total_amount' in order and isinstance(order['total_amount'], Decimal):
                order['total_amount'] = float(order['total_amount'])
            if 'created_at' in order and order['created_at'] is not None:
                order['created_at'] = order['created_at'].isoformat()
            order_list.append(order)
        return jsonify(order_list), 200

    except Exception as e:
        return jsonify({'error': "Internal Server Error", "message": "Failed to retrieve orders"}), 500

    finally:
        if cursor:
            cursor.close()

#Endpoint que permite obtener los detalles de un pedido específico
#GET /api/pedidos/<int:order_id>

@orders_bp.route('/pedidos/<int:order_id>', methods=['GET'])
@login_required
def get_order_details(order_id):

    #Obtenemos la conexión a la base de datos y el ID del usuario de la sesión

    user_id = session.get('user_id')
    print(f"Usuario {user_id} solicitando detalles del pedido {order_id}")
    db = None
    cursor = None
    try:
        db = get_db()
        if db is None:
            return jsonify({"error": "Internal Server Error", "message": "Database connection failed"}), 500

        cursor = db.cursor()

        #Realizamos la consulta para obtener los detalles del pedido

        order_sql = """SELECT id, user_id, total_amount, status, created_at FROM orders WHERE id = %s;"""
        
        cursor.execute(order_sql, (order_id,))
        order_summary = cursor.fetchone()

        #Comprobaciones de seguridad, si no se encuentra el pedido o si el usuario no tiene permiso para verlo, devolvemos un error 404 o 403 respectivamente

        if not order_summary:
            return jsonify({"error": "Not Found", "message": "Pedido no encontrado"}), 404
        if order_summary['user_id'] != user_id:
            return jsonify({"error": "Forbidden", "message": "No tienes permiso para ver este pedido"}), 403
        
        #Realizamos la consulta para obtener los items del pedido

        items_sql = """SELECT oi.product_id,oi.quantity, oi.price_at_purchase, p.id, p.idurl, p.title, p.image_url
                    FROM order_items oi
                    JOIN products p ON oi.product_id = p.id
                    WHERE oi.order_id = %s;"""
        
        #Ejecutamos la consulta y obtenemos los resultados

        cursor.execute(items_sql, (order_id,))
        order_items_raw = cursor.fetchall()

        #Convertimos los resultados a valores validos

        order_details = order_summary
        if isinstance(order_details['total_amount'], Decimal):
            order_details['total_amount'] = float(order_details['total_amount'])
        if order_details['created_at']:
            order_details['created_at'] = order_details['created_at'].isoformat()
	

        order_items_processed = []
        for item in order_items_raw:
             if isinstance(item['price_at_purchase'], Decimal):
                 item['price_at_purchase'] = float(item['price_at_purchase'])
             order_items_processed.append(item)

        order_details['items'] = order_items_processed
        return jsonify(order_details), 200
    except Exception as e:
        return jsonify({'error': "Internal Server Error", "message": "No se pudo obtener los datos del order"}), 500

    finally:
        if cursor:
            cursor.close()

