from flask import Blueprint, jsonify, abort,request
from .. import get_db
from decimal import Decimal

products_bp = Blueprint('products', __name__)

@products_bp.route('/productos',methods=['GET'])
def get_productos():
    #Devuelve la lista de todos los productos disponibles. No requiere de login para obtenerla
    db = None
    cursor = None
    try:
        #Obtenemos la conexion con la base de datos
        db = get_db()
        #Devuelve error en caso de que no se pueda obtener
        if db is None:
            print("No se pudo obtener conexión con la base de datos en la funcion get_productos()")
            return jsonify({"error": "Internal Server Error", "message": "Conexion fallida con base de datos"}), 500

        #Usamos el objeto cursor que nos permite obtener los valores de la consulta

        cursor = db.cursor()

        #Construimos la sentencia SQL para obtener los datos de los productos en nuestra base de datos

        sql = """SELECT id, idurl, title, description, artist, price, stock, image_url FROM products;"""

        #Ejecutamos y obtenemos los valores devueltos

        cursor.execute(sql)
        products_list = cursor.fetchall()

        #Convertimos los decimales a float para que puedan ser serializados en JSON

        for product in products_list:
            if 'price' in product and isinstance(product['price'], Decimal):
                product['price'] = float(product['price'])

        #Print de comprobacion de los productos

        print(f"Productos obtenidos: {len(products_list)}")
        #Return con los productos obtenidos
        return jsonify(products_list), 200

    #En caso de fallo

    except Exception as e:
        print(f"Error al obtener productos: {e}")
        return jsonify({"error": "Internal Server Error", "message": "No se pudo obtener los productos"}), 500

    #Hacer siempre

    finally:
        if cursor:
            cursor.close()


#Endpoint que permite obtener un producto dado su id
@products_bp.route('/productos/<string:product_idurl>', methods=['GET','PUT','PATCH'])
def get_product_by_idurl(product_idurl):
    #Tambien accesible por usuarios no registrados

    #Devuelve la lista de todos los productos disponibles. No requiere de login para obtenerla
    db = None
    cursor = None

    #Comprobacion de parametros: revisar que haya un product_idurl
    if not product_idurl:
        return jsonify({"error": "Bad Request", "message": "Se debe especificar un idurl"}), 400

    #Comprobacion de buena obtencion de idurl
    print(f"Solicitud para producto con idurl: {product_idurl}")

    try:
        #Obtenemos la conexion con la base de datos
        db = get_db()
        #Devuelve error en caso de que no se pueda obtener
        if db is None:
            print("No se pudo obtener conexión con la base de datos en la funcion get_productos()") # El nombre de la función aquí es get_productos, podría ser un copy-paste
            return jsonify({"error": "Internal Server Error", "message": "Conexion fallida con base de datos"}), 500

        #Usamos un objeto cursor que nos permite obtener los valores de la consulta

        cursor = db.cursor()

        if request.method == 'GET':
            #Construimos la sentencia SQL para obtener los datos de los productos en nuestra base de datos
            #NOTA: %s previene inyecciones SQL

            sql = """SELECT id, idurl, title, description, artist, price, stock, image_url, created_at, updated_at FROM products WHERE idurl = %s;"""


            #Ejecutamos y obtenemos los valores devueltos

            cursor.execute(sql,(product_idurl,))
            product = cursor.fetchone()

            #Si existe un producto con ese id

            if product:
                #Convertimos los decimales a float para que puedan ser serializados en JSON
                if 'price' in product and isinstance(product['price'], Decimal):
                    product['price'] = float(product['price'])
                print(f"[Products] Producto encontrado: {product['title']}")
                return jsonify(product), 200

            #Si el id del producto no corresponde con ningun producto

            else:
                print(f"Producto no encontrado: {product_idurl}")
                # El f-string en el mensaje de error no funcionará como está escrito, necesita llaves para la variable
                return jsonify({"error": "Not found", "message": f"No se encontro el producto con id:{product_idurl}"}), 404 # Corregido f-string

        elif request.method == 'PATCH':
            data=request.get_json()
            if not data:
                return jsonify({"error": "Bad request", "message": "No se enviaron datos JSON  para actualizar"}), 400
            allowed_fields = {
                "title": "title = %s",
                "description": "description = %s",
                "artist": "artist = %s",
                "price": "price = %s",
                "stock": "stock = %s",
                "image_url": "image_url = %s",
            }
            update_values = []
            update_parts = []
            for key, value in data.items():
                if key in allowed_fields:
                    update_parts.append(allowed_fields[key])
                    if key == "price":
                        try:
                            update_values.append(Decimal(str(value)))
                        except ValueError:
                            return jsonify({"error": "Bad request", "message": f"El valor de {key} no es un número válido"}), 400
                    elif key == "stock":
                        try:
                            update_values.append(int(value))
                        except ValueError:
                            return jsonify({"error": "Bad request", "message": f"El valor de {key} no es un número válido"}), 400
                    else:
                        update_values.append(value)

            if not update_parts:
                return jsonify({"error":"Bad request", "message": "No se enviaron campos válidos para actualizar"}), 400
            update_values.append(product_idurl)
            campos_set = ', '.join(update_parts)
            sql = f"""UPDATE products SET {campos_set} WHERE idurl = %s;"""
            print(f"SQL generado: {sql}")
            print(f"Número de placeholders %s en SQL: {sql.count('%s')}")
            try:
                cursor.execute(sql,tuple(update_values))
                if cursor.rowcount == 0:
                    return jsonify({"error":"Not found", "message": f"No se encontro el producto con id {product_idurl}"})
                db.commit()
                print(f"Producto con {product_idurl} parcialmente actualizado correctamente")
                sql_obtener = """SELECT id, idurl, title, description, artist, price, stock, image_url, created_at, updated_at FROM products WHERE idurl = %s;"""
                cursor.execute(sql_obtener,(product_idurl,))
                product = cursor.fetchone()
                if product:
                    #Convertimos los decimales a float para que puedan ser serializados en JSON
                    if 'price' in product and isinstance(product['price'], Decimal):
                        product['price'] = float(product['price'])
                        print(f"[Products] Producto encontrado: {product['title']}")
                        return jsonify(product), 200
                else:
                    print(f"Producto no encontrado: {product_idurl}")
                    # El f-string en el mensaje de error no funcionará como está escrito, necesita llaves para la variable
                    return jsonify({"error": "Not found", "message": f"No se encontro el producto con id:{product_idurl}"}), 404
            except Exception as e:
                db.rollback()
                print(f"Error al actualizar producto: {e}")
                return jsonify({"error": "Internal Server Error", "message": "No se pudo actualizar el producto"}), 500
            finally:
                if cursor:
                    cursor.close()

        elif request.method == 'PUT':
            data= request.get_json()
            if not data:
                return jsonify({"error": "Bad request", "message": "No se enviaron datos JSON para actualizar"}), 400
            requestes__fields = ['title','description','artist','price','stock','image_url']
            if not all(field in data for field in requestes__fields):
                return jsonify({"error": "Bad request", "message": "Faltan campos requeridos para la actualización"}), 400
            sql = """ UPDATE products SET title = %s, description = %s, artist = %s, price = %s, stock = %s, image_url = %s WHERE idurl = %s;"""
            try:
                price = Decimal(data['price'])
                stock = int(data['stock'])
                cursor.execute(sql,(data['title'],data['description'],data['artist'],data['price'],data['stock'],data['image_url'],product_idurl))
                #if cursor.rowcount == 0:
                #return jsonify({"error": "Not found", "message": f"No se encontro el producto con id {product_idurl}"}), 404
                db.commit()
                print(f"Producto con {product_idurl} actualizado correctamente")
                sql_obtener="""SELECT id, idurl, title, description, artist, price, stock, image_url, created_at, updated_at FROM products WHERE idurl = %s;"""
                cursor.execute(sql_obtener,(product_idurl,))
                product = cursor.fetchone()
                print(f"Tipo de 'product' obtenido de la BD: {type(product)}")
                print(f"Contenido de 'product': {product}")
                if product:
                    #Convertimos los decimales a float para que puedan ser serializados en JSON
                    if 'price' in product and isinstance(product['price'], Decimal):
                        product['price'] = float(product['price'])
                        print(f"[Products] Producto encontrado: {product['title']}")
                        return jsonify(product), 200
                    else:
                        print(f"Producto no encontrado: {product_idurl}")
                        # El f-string en el mensaje de error no funcionará como está escrito, necesita llaves para la variable
                        return jsonify({"error": "Not found", "message": f"No se encontro el producto con id:{product_idurl}"}), 404
            except (ValueError,TypeError) as ve:
                db.rollback()
                return jsonify({"error": "Bad request", "message": f"Error en los datos enviados (precio/stock): {ve}"}), 400
            except Exception as e:
                db.rollback()
                print(f"Error al actualizar producto: {e}")
                return jsonify({"error": "Internal Server Error", "message": "No se pudo actualizar el producto"}), 500
            finally:
                if cursor:
                    cursor.close()

    #En caso de fallo
    except Exception as e:
        print(f"Error al obtener producto: {product_idurl}: {e}")
        return jsonify({"error": "Internal Server Error", "message": "No se pudo obtener detalles del producto"}), 500 # Cambiado 404 a 500 para Internal Server Error

    finally:
        if cursor:
            cursor.close()