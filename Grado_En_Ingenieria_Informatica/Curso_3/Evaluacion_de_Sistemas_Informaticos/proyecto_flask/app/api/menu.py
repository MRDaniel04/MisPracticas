from flask import Blueprint,jsonify,render_template
import traceback

#Blueprint para el menú de la aplicación que mostrará la información de la tienda
menu_bp = Blueprint('menu', __name__)

#Endpoint: GET /api/menu
#Devuelve información general de la tienda con sus productos
#Acceso público que no requiere de autenticación

@menu_bp.route('/menu',methods=['GET'])
def get_menu_info():
    try:
        menu_data = {
            "store_name": "Arte Visual",
            "store_description": "Tienda de arte visual",
            "categories":[{"id": "posters","name": "Pósters exclusivos"}]
        }

        #Devuelve datos en estado json con estado 200 (OK)

        return jsonify(menu_data), 200
    
    #En caso de error, devuelve estado 500 (Internal Server Error)
    except Exception as e:
        return jsonify({"error": "Internal Server Error", "message": "No se pudo obtener la informacion de la tienda"}), 500
