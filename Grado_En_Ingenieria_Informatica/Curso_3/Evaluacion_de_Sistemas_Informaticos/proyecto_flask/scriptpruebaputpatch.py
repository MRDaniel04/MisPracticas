import requests
import json
import pprint
import uuid # Para generar idurls únicos si es necesario para crear productos

# --- Configuración ---
API_BASE_URL = "http://virtual.lab.inf.uva.es:31184/api"  # Ajusta si tu API corre en otro lugar
HEADERS = {"Content-Type": "application/json"}

# Helper para imprimir respuestas de forma bonita
pp = pprint.PrettyPrinter(indent=2)

def print_response(response):
    """Imprime la respuesta de la API de forma legible."""
    print(f"\n--- Respuesta del Servidor (Código: {response.status_code}) ---")
    try:
        pp.pprint(response.json())
    except json.JSONDecodeError:
        print("La respuesta no es JSON válido:")
        print(response.text)
    print("--------------------------------------")

def get_product(product_idurl):
    """Obtiene un producto para verificar su estado antes y después."""
    print(f"\n[INFO] Obteniendo producto: {product_idurl}...")
    try:
        response = requests.get(f"{API_BASE_URL}/productos/{product_idurl}")
        response.raise_for_status()
        print_response(response)
        return response.json() # Devuelve los datos del producto si es exitoso
    except requests.exceptions.RequestException as e:
        print(f"[ERROR] Al obtener producto {product_idurl}: {e}")
        if hasattr(e, 'response') and e.response is not None:
            print_response(e.response)
        return None

def test_put_product(product_idurl):
    """Prueba el endpoint PUT para actualizar completamente un producto."""
    print(f"\n========== INICIO PRUEBA PUT para '{product_idurl}' ==========")

    print("\n[PUT] Estado inicial del producto (antes de PUT):")
    initial_product_data = get_product(product_idurl)
    if not initial_product_data:
        print(f"[PUT-ERROR] No se pudo obtener el producto inicial '{product_idurl}'. Abortando prueba PUT.")
        print("====================================================")
        return

    put_data = {
        "title": f"Producto Actualizado (PUT) - {product_idurl[-6:]}", # Usar solo parte del idurl para no hacer títulos muy largos
        "description": "Esta es una descripción completamente actualizada por PUT.",
        "artist": "Artista Actualizado PUT",
        "price": 99.99,
        "stock": 50,
        "image_url": "http://example.com/updated_put_image.jpg"
    }
    print(f"\n[PUT] Enviando datos para actualizar (PUT):")
    pp.pprint(put_data)

    try:
        url = f"{API_BASE_URL}/productos/{product_idurl}"
        response = requests.put(url, headers=HEADERS, json=put_data)
        print_response(response)
        response.raise_for_status()

        print("\n[PUT] Verificando producto después de PUT:")
        updated_product_data = get_product(product_idurl)
        if updated_product_data:
            print("[PUT] ¡Prueba PUT parece exitosa!")

    except requests.exceptions.HTTPError as http_err:
        print(f"[PUT-ERROR] Error HTTP en la solicitud PUT: {http_err}")
    except requests.exceptions.RequestException as e:
        print(f"[PUT-ERROR] Error en la solicitud PUT: {e}")

    print(f"========== FIN PRUEBA PUT para '{product_idurl}' ==========")


def test_patch_product(product_idurl):
    """Prueba el endpoint PATCH para actualizar parcialmente un producto."""
    print(f"\n========== INICIO PRUEBA PATCH para '{product_idurl}' ==========")

    print("\n[PATCH] Estado inicial del producto (antes de PATCH):")
    initial_product_data = get_product(product_idurl)
    if not initial_product_data:
        print(f"[PATCH-ERROR] No se pudo obtener el producto inicial '{product_idurl}'. Abortando prueba PATCH.")
        print("======================================================")
        return

    patch_data = {
        "description": "Descripción actualizada parcialmente por PATCH.",
        "price": 123.45,
    }
    if initial_product_data and "stock" in initial_product_data and initial_product_data["stock"] is not None:
         patch_data["stock"] = initial_product_data["stock"] + 10 # Aumentar stock en 10
    else:
        patch_data["stock"] = 10 # Si no hay stock o es None, lo pone a 10


    print(f"\n[PATCH] Enviando datos para actualizar (PATCH):")
    pp.pprint(patch_data)

    try:
        url = f"{API_BASE_URL}/productos/{product_idurl}"
        response = requests.patch(url, headers=HEADERS, json=patch_data)
        print_response(response)
        response.raise_for_status()

        print("\n[PATCH] Verificando producto después de PATCH:")
        updated_product_data = get_product(product_idurl)
        if updated_product_data:
            # Comprobaciones (ejemplos)
            if updated_product_data.get("description") == patch_data["description"]:
                print("[PATCH-VERIFY] Descripción actualizada correctamente.")
            else:
                print(f"[PATCH-VERIFY-WARN] La descripción no coincide.")

            if updated_product_data.get("price") == patch_data["price"]:
                print("[PATCH-VERIFY] Precio actualizado correctamente.")
            else:
                print(f"[PATCH-VERIFY-WARN] El precio no coincide.")
            print("[PATCH] ¡Prueba PATCH parece exitosa!")

    except requests.exceptions.HTTPError as http_err:
        print(f"[PATCH-ERROR] Error HTTP en la solicitud PATCH: {http_err}")
    except requests.exceptions.RequestException as e:
        print(f"[PATCH-ERROR] Error en la solicitud PATCH: {e}")

    print(f"========== FIN PRUEBA PATCH para '{product_idurl}' ==========")


if __name__ == "__main__":
    # Solicitar directamente el idurl del producto de prueba al usuario
    product_id_to_test = input("Por favor, ingresa el 'product_idurl' del producto de prueba existente: ")

    if product_id_to_test: # Verifica que el usuario haya ingresado algo
        print(f"\n--- Ejecutando pruebas para product_idurl: '{product_id_to_test}' ---")

        # Probar PUT
        test_put_product(product_id_to_test)

        input("\nPresiona Enter para continuar con la prueba PATCH...") # Pausa para revisar

        # Probar PATCH
        # Nota: PATCH se ejecutará sobre el estado modificado por PUT (si PUT fue exitoso y el producto existe)
        test_patch_product(product_id_to_test)

        print("\n--- Pruebas completadas ---")
    else:
        print("No se proporcionó un 'product_idurl'. Pruebas no ejecutadas.")