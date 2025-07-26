#!/bin/bash
# Script para obtener el uso instantáneo de CPU en % y guardarlo en la base de datos

# Configuración de la base de datos
DB_HOST="localhost"
DB_USER="root"
DB_PASS="somoselgrupo02*"
DB_PATH="esi_proyecto"

while true;do
# Sumar directamente el %CPU de todos los procesos
cpu_total=$(ps -eo %cpu --no-headers | awk '{s+=$1} END {print s}')

# Verificar si la variable está vacía o NULL
if [[ -z "$cpu_total" ]]; then
    echo "Error: No se pudo obtener el uso de CPU correctamente."
    exit 1
fi

# Obtener el número de núcleos lógicos
cores=$(nproc)
# Calcular el % normalizado
cpu_normalized=$(echo "$cpu_total / $cores" | bc -l)

# Insertar el valor normalizado
mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "INSERT INTO info_carga_cpu (carga_instantanea) VALUES ($cpu_normalized);"

sleep 60
done
