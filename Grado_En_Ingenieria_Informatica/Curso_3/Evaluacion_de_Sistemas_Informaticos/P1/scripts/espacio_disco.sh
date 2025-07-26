#!/bin/bash

#Script para la obtención del espacio en disco

DB_HOST="localhost"
DB_USER="root"
DB_PASS="somoselgrupo02*"
DB_PATH="esi_proyecto"

while true;do

#Obtenemos el timestamp
timestamp=$(date '+%Y-%m-%d %H:%M:%S')
id=0

#Elimino los datos que existan en la tabla para introducir los nuevos
mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "DELETE FROM espacio_disco;"

#Obtenemos el espacio en disco
df -h /dev/sda2 2>/dev/null | tail -n+2| while read linea; do

	usado=$(echo "$linea" | awk '{print $3}' | sed 's/[A-Za-z]//g')
	unidad=$(echo "$linea" | awk '{print $3}' | sed 's/[0-9,.]//g')
	case $unidad in 
		G) 
			usado_limpio=$(echo "$usado" | sed 's/,/./')
			;;
		M)	
			usado_limpio=$(echo "$usado" | awk '{print $1/$1024}' | sed 's/,/./')
			;;
		T)	
			usado_limpio=$(echo "$usado" | awk '{print $1*1024}' | sed 's/,/./')
			;;
		K)	
			usado_limpio=$(echo "$usado" | awk '{print $1/1048576}' | sed 's/,/.')
			;;
	esac	

	libre=$(echo "$linea" | awk '{print $4}' | sed 's/[A-Za-z]//g')
	unidad=$(echo "$linea" | awk '{print $4}' | sed 's/[0-9.,]//g')
	case $unidad in 
		G) 
			libre_limpio=$(echo "$libre" | sed 's/,/./')
			;;
		M)	
			libre_limpio=$(echo "$libre" | awk '{print $1/1024}' | sed 's/,/./')
			;;
		
		K)	
			libre_limpio=$(echo "$libre" | awk '{print $1/1048576}' | sed 's/,/.')
			;;
		T)	
			libre_limpio=$(echo "$libre" | akw '{print $1*1024}' | sed 's/,/./')
			;;

	esac

	nombre=$(echo "$linea" | awk '{print $1}')
	id=$((1+id))
	mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "INSERT INTO  espacio_disco VALUES ($usado_limpio,$libre_limpio,'$nombre',$id,'$timestamp');"
done

sleep 60
done

