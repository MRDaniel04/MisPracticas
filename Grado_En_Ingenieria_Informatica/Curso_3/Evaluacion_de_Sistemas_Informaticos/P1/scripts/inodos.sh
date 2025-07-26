#!/bin/bash

#Script para obtener el numeros de inodos usados y libres

DB_HOST="localhost"
DB_USER="root"
DB_PASS="somoselgrupo02*"
DB_PATH="esi_proyecto"

while true;do

#Obtenemos el timestamp
timestamp=$(date '+%Y-%m-%d %H:%M:%S')
total_libres=0
total_usados=0

#Elimino los datos que tengan mas de un dia de antiguedad
mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "DELETE FROM inodos where col_timestamp < NOW() - INTERVAL 1 DAY;"

#Obtenemos el numero de inodos usados y libres
while read linea; do
    inodos_usados=$(echo $linea | awk {'print $3'})
    inodos_libres=$(echo $linea | awk {'print $4'})

    total_usados=$(($inodos_usados+$total_usados))
    total_libres=$(($inodos_libres+$total_libres))

done < <(df -i | tail -n +2)
mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "INSERT INTO inodos VALUES ($total_usados,$total_libres,'$timestamp')"

sleep 60
done
