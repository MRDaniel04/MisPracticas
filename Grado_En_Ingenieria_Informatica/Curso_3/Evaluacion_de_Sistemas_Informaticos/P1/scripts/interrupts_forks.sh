#!/bin/bash
#Script de obtención de los tiempos de espera de los procesos en la cola

#Rellenamos datos respectivos a la base de datos
DB_HOST="localhost"
DB_USER="root"
DB_PASS="somoselgrupo02*"
DB_PATH="esi_proyecto"

while true;do

#Obtenemos la fecha actual
timestamp=$(date '+%Y-%m-%d %H:%M:%S')

#Elimino los datos que tengan mas de un dia de antiguedad
mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "DELETE FROM interrupciones_forks where col_timestamp < NOW() - INTERVAL 1 DAY;"

num_interrupciones=$(vmstat -s | grep "interrupts" | awk '{print $1}')
forks=$(vmstat -s | grep "forks" | awk '{print $1}' )

last_forks=$(mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "SELECT tot_forks FROM interrupciones_forks" -s -N | tail -n 1)
last_interrupts=$(mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "SELECT tot_interrupts FROM interrupciones_forks" -s -N | tail -n 1)
if [ -z "$last_forks" ]; then
	int_forks=0
    int_interrupts=0
else
	int_interrupts=$((num_interrupciones - last_interrupts))
    int_forks=$((forks - last_forks))
fi

mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "INSERT INTO interrupciones_forks (col_timestamp, tot_interrupts, int_interrupts, tot_forks, int_forks) VALUES ('$timestamp',$num_interrupciones, $int_interrupts, $forks, $int_forks);"

sleep 60
done
