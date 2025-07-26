#!/bin/bash

#Script para obtener el uso de memoria RAM y memoria swap

DB_HOST="localhost"
DB_USER="root"
DB_PASS="somoselgrupo02*"
DB_PATH="esi_proyecto"

while true;do

#Obtener el timestamp

timestamp=$(date '+%Y-%m-%d %H:%M:%S')
usada_ram=0
usada_swap=0
libre_ram=0
libre_swap=0

#Elimino los datos que tengan mas de un dia de antiguedad
mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "DELETE FROM uso_ram_swap where col_timestamp < NOW() - INTERVAL 1 DAY;"

while read -r linea;do
	tipo=$(echo "$linea" | awk '{print $1}')
	usada=$(echo "$linea" | awk '{print $3}' | sed 's/[A-Za-z]//g')
	unidad=$(echo "$linea" | awk '{print $3}' | sed 's/[0-9,.]//g')
	case $unidad in 
		Gi) 
			usada_limpio=$(echo "$usada" | awk '{print $1/1024}' | sed 's/,/./')
			;;	
		Ki)
			usada_limpio=$(echo "$usada" | awk '{print $1/1024}' | sed 's/,/./')
			;;
		Mi)	
			usada_limpio=$(echo "$usada" | sed 's/,/./')
			;;
		Ti)	
			usada_limpio=$(echo "$usada" | awk '{print $1*1024*1024}' | sed 's/,/./')
			;;
	esac

	libre=$(echo "$linea" | awk '{print $4}' | sed 's/[A-Za-z]//g')
	unidad=$(echo "$linea" | awk '{print $4}' | sed 's/[0-9.,]//g')
	case $unidad in 
		Gi) 
			libre_limpio=$(echo "$libre" | awk '{print $1*1024}' |sed 's/,/./')
			;;
		Ki)
			libre_limpio=$(echo "$libre" | awk '{print $1/1024}' | sed 's/,/./')
			;;
		Mi)	
			libre_limpio=$(echo "$libre" | sed 's/,/./')
			;;
		Ti)	
			libre_limpio=$(echo "$libre" | awk '{print $1*1024*1024}' | sed 's/,/./')
			;;

	esac
	if [ "$tipo" = "Mem:" ]; then
		usada_ram=$(echo "$usada_limpio")
		libre_ram=$(echo "$libre_limpio")
	else
		usada_swap=$(echo "$usada_limpio")
		libre_swap=$(echo "$libre_limpio")
	fi
done < <(free -h | tail -n +2)

	mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "INSERT INTO uso_ram_swap VALUES ($usada_ram,$libre_ram,$usada_swap,$libre_swap,'$timestamp');"

sleep 60 
done
