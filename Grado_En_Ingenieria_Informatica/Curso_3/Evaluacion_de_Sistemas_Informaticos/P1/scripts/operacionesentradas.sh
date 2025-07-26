#!/bin/bash
#Script de obtencion de la cantidad de operaciones de entrada/salida

#Rellenamos datos respectivos a la base:
DB_HOST="localhost"
DB_USER="root"
DB_PASS="somoselgrupo02*"
DB_PATH="esi_proyecto"

while true;do

#Obtenemos la fecha actual
timestamp=$(date '+%Y-%m-%d %H:%M:%S')

#Elimino los datos que tengan mas de un dia de antiguedad
mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "DELETE FROM operaciones_es where col_timestamp < NOW() - INTERVAL 1 DAY;"

#Obtenemos el numero de operaciones de E/S, desde el fichero /proc/diskstats y vamos acumulandolas en sum
operaciones_es=$(awk '{ sum += ($4 + $8)*2 } END { print sum }' /proc/diskstats)
lectura_es=$(awk '{ sum += $4*2} END { print sum }' /proc/diskstats)
escritura_es=$(awk '{ sum += $8*2} END { print sum }' /proc/diskstats)

last_kbwr=$(mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "SELECT kb_escritos FROM operaciones_es;" -s -N | tail -n 1)
if [ -z "$last_kbwr" ]; then
	min_wr=0
	min_read=0
	operaciones_tot=0
else
	min_wr=$((escritura_es - last_kbwr))
	last_kbread=$(mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "SELECT kb_leidos FROM operaciones_es;" -s -N | tail -n 1)
	min_read=$((lectura_es - last_kbread))
	last_tot=$(mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "SELECT operaciones_segundo FROM operaciones_es;" -s -N | tail -n 1)
	operaciones_tot=$((operaciones_es - last_tot))
fi
#Insertamos en base de datos de mysql
mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "INSERT INTO operaciones_es VALUES ($operaciones_es,$lectura_es,$escritura_es,'$timestamp',$min_read,$min_wr,$operaciones_tot);"

sleep 60
done
