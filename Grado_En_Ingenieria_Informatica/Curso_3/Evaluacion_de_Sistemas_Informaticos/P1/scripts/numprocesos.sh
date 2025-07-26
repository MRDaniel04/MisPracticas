#!/bin/bash
#Script de obtencion de informacion de procesos y guardarlos en la base de datos

#Rellenamos datos respectivos a la base de datos
DB_HOST="localhost"
DB_USER="root"
DB_PASS="somoselgrupo02*"
DB_PATH="esi_proyecto"

while true;do

#Obtenemos el timestamp
timestamp=$(date '+%Y-%m-%d %H:%M:%S')

#Elimino los datos que tengan mas de un dia de antiguedad
mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "DELETE FROM nprocesos_ejecuciones where col_timestamp < NOW() - INTERVAL 1 DAY;"

#Obtener el número de procesos en ejecucion
num_procesos=$(ps -eo pid | tail -n +2 | wc -l)

#Completar con datos de la base de datos
mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "INSERT INTO nprocesos_ejecuciones VALUES ($num_procesos,'$timestamp');"

sleep 60
done
