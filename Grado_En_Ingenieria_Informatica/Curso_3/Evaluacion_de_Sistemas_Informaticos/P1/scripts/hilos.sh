#!/bin/bash
# Script de obtención de los tiempos de espera de los procesos en la cola, y el uso de CPU y Memoria

# Rellenamos datos respectivos a la base de datos
DB_HOST="localhost"
DB_USER="root"
DB_PASS="somoselgrupo02*"
DB_PATH="esi_proyecto"

while true;do

#Obtenemos el timestamp
timestamp=$(date '+%Y-%m-%d %H:%M:%S')

#Elimino los datos que tengan mas de un dia de antiguedad
mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "DELETE FROM hilos where col_timestamp < NOW() - INTERVAL 1 DAY;"

top -H -b -n 1 | tail -n +8 | while read -r linea; do
    PID=$(echo $linea | awk {'print $1'})    
    CPU=$(echo $linea | awk {'print $9'} | sed 's/,/./') 
    MEM=$(echo $linea | awk {'print $10'} | sed 's/,/./') 
    COMMAND=$(echo $linea | awk {'print $12'}) 
    mysql -h $DB_HOST -u $DB_USER -p$DB_PASS $DB_PATH -e "INSERT INTO hilos VALUES ('$PID', $CPU, $MEM, '$COMMAND', '$timestamp')"
done

sleep 60
done
