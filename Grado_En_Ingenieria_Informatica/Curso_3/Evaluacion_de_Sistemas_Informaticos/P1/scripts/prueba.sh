#!/bin/bash
# Script de prueba CORREGIDO

IPS_PRUEBA=(
    "95.123.174.83"    # Tu IP de ejemplo
    "8.8.8.8"          # Google DNS
    "192.168.1.1"      # IP privada
    "2001:4860:4860::8888"  # IPv6
)

for ip in "${IPS_PRUEBA[@]}"; do
    echo -e "\n\033[1;36m=== Probando IP: $ip ===\033[0m"
    
    # Consultar API
    respuesta=$(curl -s -A "Mozilla/5.0" --connect-timeout 5 "http://ip-api.com/json/$ip")
    echo "Respuesta cruda:"
    echo "$respuesta"
    
    # --- Corrección Clave: Nuevo método de extracción ---
    lat=$(echo "$respuesta" | awk -F'"lat":' '{print $2}' | awk -F'[,}]' '{print $1}' | tr -d ' ')
    lon=$(echo "$respuesta" | awk -F'"lon":' '{print $2}' | awk -F'[,}]' '{print $1}' | tr -d ' ')
    
    echo -e "\nDatos extraídos:"
    echo "Latitud: $lat"
    echo "Longitud: $lon"
    
    # Validación mejorada
    if [[ "$lat" =~ ^-?[0-9]+(\.[0-9]+)?$ && "$lon" =~ ^-?[0-9]+(\.[0-9]+)?$ ]]; then
        echo -e "\033[1;32m✓ Coordenadas válidas\033[0m"
    else
        echo -e "\033[1;31m✖ Error en coordenadas\033[0m"
    fi
done
