from flask import Blueprint,flash,g,redirect,render_template,request,url_for,session,jsonify
from werkzeug.security import check_password_hash,generate_password_hash
from .. import get_db
auth_bp = Blueprint('auth', __name__)
menu_bp = Blueprint('menu', __name__)

@auth_bp.route('/login',methods=['GET','POST'])
def check_login():
	if request.method == 'POST':
		if not request.is_json:
			return jsonify({"success":False,"message":"Peticion invalida: Se esperaba un json"}),400
		data = request.get_json()
		email_form=data.get('username')
		contrasenya_form=data.get('password')
		print(f"[Auth API DEBUG] Email (de clave 'username'): '{email_form}', Contraseña recibida: {contrasenya_form}")
		if not email_form or not contrasenya_form:
			return jsonify({"success": False, "message": "Usuario y contraseña son requeridos."}), 400

		print(f"[Auth] Intento de login: Email='{email_form}'")
		db=get_db()
		error_db = None
		user_data = None

		if db is None:
			error_db="Error interno: No se pudo conectar a la base de datos"
			print("[Auth] Error: {e}")
			return jsonify({"success": False, "message": "Error interno: No se pudo conectar a la base de datos."}), 500

		else:
			try:
				cursor=db.cursor()
				sql= "SELECT id,email,password_hash,user_type from users where email = %s"
				cursor.execute(sql,(email_form,))
				user_data= cursor.fetchone()
				cursor.close
			except Exception as e:
				error_db=f"Error al consultar la base de datos"
				print(f"[Auth] Error DB: {e}")
				return jsonify({"success": False, "message": f"Error al consultar la base de datos: {str(e)}"}), 500

		login= False

		if user_data is None:
			print(f"[Auth] Login fallido: Email '{email_form}' no encontrado en la BD")
			return jsonify({"success": False, "message": "El email introducido no está registrado."}), 401
		else:
			print(f"CONTRASEÑAS: '{user_data['password_hash']} '{contrasenya_form}")
			if check_password_hash(user_data['password_hash'],contrasenya_form):
				login=True
				print(f"[Auth] Login exitoso para: {email_form} (ID: {user_data.get('id')})")
				session.clear()
				session['contrasenya']=user_data['password_hash']
				session['email']=user_data['email']
				session['user_id'] = user_data['id']
				session['user_type'] = user_data['user_type']
				return jsonify({
				"success": True,
				"message": "Login exitoso!",
				"user": {
					"contrasenya": user_data['password_hash'],
					"email": user_data['id'],
				}
				# Podrías añadir un token JWT aquí si lo usas
				}), 200
			else:
				print(f"[Auth] Login fallido: Contraseña incorrecta para '{email_form}'.")
				return jsonify({"success": False, "message": "La contraseña introducida no es correcta."}), 401
	if request.method == 'GET':
		return redirect(url_for('login_page'))
 
@auth_bp.route('/registro',methods=['GET','POST'])
def check_registro():
	if request.method == 'POST':
		usuario_form=request.form.get('usuario')
		contrasenya_form=request.form.get('contrasenya')
		contrasenya_hashed=generate_password_hash(contrasenya_form)
		email_form=request.form.get('email')

		print(f"[Auth] Intento de registro: Usuario='{usuario_form}' Email='{email_form}'")
		
		db=get_db()
		error_db=None
		user_data=None
			
		if db is None:
			error_db="Error interno: No se puede acceder a la base de datos"
			print(f"[Auth] Error {e}")		
		else:	
			try:
				cursor=db.cursor()
				sql="SELECT id,name,email,user_type from users where email=%s"
				cursor.execute(sql,(email_form,))
				user_data=cursor.fetchone()
				cursor.close
			except Exception as e:
				error_db="Error al consultar la base de datos"
				printf(f"[Auth] Error DB: {e}")
		registro=False
		
		if error_db: 
			flash(error_db,'danger')
			return redirect(url_for('registro_page'))
		elif user_data:
			print(f"[Auth] Registro fallido: Email '{email_form}' ya esta registrado en la base de datos")
			flash("El email introducido ya esta registrado en la base de datos",'danger')
			return redirect(url_for('registro_page'))
		else:
			try:
				cursor=db.cursor()
				sql="INSERT into users(name,email,password_hash) VALUES (%s,%s,%s)"
				valores=(usuario_form,email_form,contrasenya_hashed)
				cursor.execute(sql,valores)
				db.commit()
				cursor.close()
			except Exception as e:
				error_db="Error al insertar los datos en la base de datos"
				print(f"[Auth] Error DB: {e}")
		
			return redirect(url_for('login_page'))
	return redirect(url_for('registro_page'))
