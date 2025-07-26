#!/bin/bash
#Script de obtencion de número de bytes enviados y recibidos a traves de la red

#Rellenamos datos respectivos a la base de datos
DB_HOST="localhost"
DB_USER="root"
DB_PASS="somoselgrupo02*"
DB_PATH="esi_proyecto"

while true;do
#Obtenemos el timestamp
timestamp=$(date '+%Y-%m-%d %H:%M:%S')

#Elimino los datos que tengan mas de un dia de antiguedad
mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "DELETE FROM bytes_red where col_timestamp < NOW() - INTERVAL 1 DAY;"

#Obtenemos los bytes enviados y recibido
bytes_enviados=$(ip -s link show ens18 | awk '/TX:/ {getline; print $1}')
bytes_recibidos=$(ip -s link show ens18 | awk '/RX:/ {getline; print $1}')

last_env=$(mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "SELECT bytes_enviados from bytes_red;" -s -N | tail -n 1)
if [ -z "$last_env" ]; then
	min_env=0;
	min_rec=0;
else
	min_env=$((bytes_enviados-last_env))
	last_rec=$(mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "SELECT bytes_recibidos from bytes_red;" -s -N | tail -n 1)
	min_rec=$((bytes_recibidos-last_rec))
fi

#Completar con datos de la base de datos
mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "INSERT INTO bytes_red VALUES ($bytes_enviados, $bytes_recibidos, '$timestamp',$min_env,$min_rec);"

sleep 60
done
