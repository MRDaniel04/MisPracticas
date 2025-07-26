#!/bin/bash
#Script de obtencion de informacion de procesos y guardarlos en la base de datos

#Rellenamos datos respectivos a la base de datos
DB_HOST="localhost"
DB_USER="root"
DB_PASS="somoselgrupo02*"
DB_PATH="esi_proyecto"

#Borramos los procesos que hubiera anteriormente para guardar los nuevos
mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "TRUNCATE TABLE info_procesos"
mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "TRUNCATE TABLE conexiones"
#Obtenemos la información de cada uno de los procesos del sistema
mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "INSERT INTO info_procesos (pid,comando,usuario,estado,cpu_porcentaje,mem_porcentaje,ruta_comando,pid_father,tamano) VALUES (0,'kernel','-','-',0,0,'-',NULL,10);"
ps -eo pid,comm,user,state,%cpu,%mem,args --no-headers | while read line; do
	#Obtenemos cada uno de los campos por cada uno de los procesos
	id=$(echo "$line" | awk '{print $1}')
	comando=$(echo "$line" | awk '{print $2}')
	usuario=$(echo "$line" | awk '{print $3}')
	estado=$(echo "$line" | awk '{print $4}')
	cpu_porcentaje=$(echo "$line" | awk '{print $5}')
	mem_porcentaje=$(echo "$line" | awk '{print $6}')
	ruta_comando=$(echo "$line" | awk '{print $7}')
	parent_pid=$(ps -o ppid= -p $id)
	noderadius=($cpu_porcentaje+1)*5
	#Asignar valores predeterminados si el campo pid_parent
	if [ -z "$parent_pid" ]; then
		parent_pid=-1
	fi

	if [ -z "$id" ]; then
		id=-1
	fi

	#Insertamos una fila, añadiendo el campo del pid, para luego actualizar el resto de columnas de la tabla con el resto de valores
	mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "INSERT INTO info_procesos (pid) VALUES ($id)"
	mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "UPDATE info_procesos SET comando='$comando',usuario='$usuario',estado='$estado',cpu_porcentaje=$cpu_porcentaje,mem_porcentaje=$mem_porcentaje,ruta_comando='$ruta_comando',pid_father='$parent_pid',tamano=$noderadius where pid = $id;"

done
result=$(mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "SELECT pid, pid_father FROM info_procesos WHERE pid_father != -1 ORDER BY pid_father ASC" -s -N)

while read -r line; do
	# Extraer los valores de pid, parent_pid
	pid=$(echo "$line" | awk '{print $1}')
	parent_pid=$(echo "$line" | awk '{print $2}')
	idedge="$pid:$parent_pid"
	mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASS" "$DB_PATH" -e "INSERT INTO conexiones (pid, parent_pid, idrelacion) VALUES ($pid, $parent_pid, '$idedge');"
done  <<< "$result"



