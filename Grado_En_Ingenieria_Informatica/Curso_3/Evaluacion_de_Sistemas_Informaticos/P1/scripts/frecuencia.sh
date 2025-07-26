#!/bin/bash

# Configuración de la base de datos (sin cambios)
DB_HOST="localhost"
DB_USER="root"
DB_PASS="somoselgrupo02*"
DB_PATH="esi_proyecto"

while true;do

# Obtener frecuencia y convertir a GHz (¡usa LC_NUMERIC=C!)
cpu_freq=$(grep "cpu MHz" /proc/cpuinfo | awk '{print $4}' | head -n1)
cpu_freq_ghz=$(LC_NUMERIC=C echo "scale=2; $cpu_freq / 1000" | bc)

# Insertar en la base de datos (¡con comillas alrededor del valor!)
mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e \
    "INSERT INTO frecuencia_cpu (velocidad) VALUES (${cpu_freq_ghz});"

sleep 60
done
