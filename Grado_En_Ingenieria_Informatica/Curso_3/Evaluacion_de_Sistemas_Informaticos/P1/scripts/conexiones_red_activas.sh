#!/bin/bash

#Rellenamos datos respectivos a la base de datos
DB_HOST="localhost"
DB_USER="root"
DB_PASS="somoselgrupo02*"
DB_PATH="esi_proyecto"

while true;do

#Borramos las conexiones que hubiera anteriormente para guardar las nuevas
mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "TRUNCATE TABLE conexiones_red"

indice=1

#Obtenemos la información de cada una de las conexiones red
ss -tupna | tail -n +2 | while read line; do
        #Obtenemos cada uno de los campos por cada uno de los procesos
	protocolo=$(echo "$line" | awk '{print $1}')
	estado=$(echo "$line" | awk '{print $2}')
	direccion_ip_local=$(echo "$line" | awk '{print $5}')
	direccion_ip_remota=$(echo "$line" | awk '{print $6}')

        #Insertamos una fila, añadiendo el campo del pid, para luego actualizar el resto de columnas de la tabla con el resto de valores
	mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "INSERT INTO conexiones_red (numero_conexion) VALUES ($indice)"
        mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "UPDATE conexiones_red SET protocolo='$protocolo',estado='$estado',direccion_ip_local='$direccion_ip_local',direccion_ip_remota='$direccion_ip_remota' where numero_conexion = $indice;"

indice=$((indice + 1))

done

sleep 60
done
