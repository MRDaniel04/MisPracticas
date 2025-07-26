/*Linea de creacion de la tabla correspondiente a los bytes enviados y recibidos, en ella guardamos el numero total de bytes enviados y recibidos, el timestamp para poder mostrar luego en la grafica los datos a lo largo del tiempo, el numero de bytes enviados y recibidos por minuto y establecemos como clave primaria la columna timestamp ya que esta va a ser unica para cada fila de la tabla */
create table bytes_red(
	bytes_enviados int,
	bytes_recibidos int,
	col_timestamp timestamp default current_timestamp on update current_timestamp,
	min_env int,
	min_rec int,
	primary key(col_timestamp));

/*Linea de creacion de la tabla correspondiente a las conexiones de red, en ella guardamos un numero de conexion que la va a identificar, el protocolo de la conexion de red, el estado de la conexion, las direcciones ip local y la direccion ip remota y establecemos como clave primaria la columna numero de conexion ya que esta va a ser unica para cada fila de la tabla */
create table conexiones_red(
	numero_conexion int,
        protocolo varchar(10),
        estado varchar(20),
        direccion_ip_local varchar(50),
        direccion_ip_remota varchar(50),
        primary key(numero_conexion));

/*Línea de creacion de la tabla correspondiente a los conexiones de los nodos, en ella guardamos el pid del proceso, el pid del proceso padre que le invoca, la combinacion del pid del proceso y el pid del proceso padre y establecemos como clave primaria la combinacion de las columnas pid del proceso y pid del padre para que esta clave sea unica para cada proceso de la tabla */
create table conexiones(
	pid int,
	parent_pid int,
	idrelacion varchar(100),
	primary key(pid,parent_pid));

/*Línea de creación de la tabla correspondiente al espacio en disco, en ella guardamos el espacio de disco usado, el espacio de disco libre, el nombre del directorio que ocupa tal espacio, un id para distinguir entre las distintas filas de la tabla, el timestamp para que los datos se vayan actualizando con el paso del tiempo y establecemos como clave primaria la columna id ya que esta va a ser única para cada fila de la tabla */
create table espacio_disco(
	usado float,
	libre float,
	nombre varchar(50),
	id int,
	col_timestamp timestamp default current_timestamp on update current_timestamp,
	primary key(id));

/*Línea de creación de la tabla correspondiente a la frecuencia de cpu, en ella guardamos un número de identificacion que vamos a usar para la clave primaria, la velocidad de la cpu y el tiemstamp para que los datos se vayan actualizando con el paso del tiempo */
create table frecuencia_cpu (
    id int AUTO_INCREMENT,
    velocidad decimal(5,2),
    col_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);


/*Línea de creación de la tabla correspondiente al uso de cpu y mem por parte de los hilos, en ella guardamos el pid del proceso, su consumo de cpu, su consumo de mem, el comando o procesos que esta ejecutando y el timestamp para poder mostrar los datos a lo largo del tiempo y establecemos como clave primaria la columna timestamp junto con el pid para poder identificar de manera unica cada fila de la tabla */
create table hilos(
	pid numeric(20),
	cpu float,
	mem float,
	command varchar(100),
	col_timestamp timestamp default current_timestamp on update current_timestamp,
	primary key(pid,col_timestamp));

/*Línea de creación de la tabla correspondiente a la carga de la cpu, en ella guardamos un identificador que vamos a usar como clave primaria, el timestamp para mostrar los datos a lo largo del tiempo, la carga instantanea de la cpu */
create table info_carga_cpu(
	id int,
	fecha timestamp default current_timestamp,
	carga_instantanea float,
	primary key(id));

/*Línea de creación de la tabla correspondiente a la informacion de los procesos, en ella guardamos el pid del proceso, el comando que lo ha ejecutado, el usuario que ha ejecutado dicho proceso, el estado del proceso, el porcentaje de cpu y mem que consume y la ruta del comando que esta ejecutando y establecemos como clave primaria la columna pid ya que esta va a ser única para cada fila de la tabla */
create table info_procesos(
	pid int,
	comando varchar(100),
	usuario varchar(30),
	estado varchar(30),
	cpu_porcentaje decimal(5,2),
	mem_porcentaje decimal(5,2),
	ruta_comando varchar(500),
	primary key(pid));

/*Línea de creación de la tabla correspondiente a la diferencia de inodos, en ella guardamos el numero de inodos usados y libres, el timestamp para mostrar los datos a lo largo del tiempo y establecemos como clave primaria la columna timestamp ya que esta va a ser única para cada fila de la tabla */
create table inodos(
	inodos_usados bigint,
	inodos_libres bigint,
	col_timestamp timestamp default current_timestamp on update current_timestamp,
	primary key(col_timestamp));

/*Línea de creación de la tabla correspondiente a las interrupciones de los forks, en ella guardamos el timestamp para mostrar los datos a lo largo del tiempo, el numero total de interrupciones, el numero de interrupciones en un intervalo de tiempo y el numero de forks en un intervalo de tiempo y establecemos como clave primaria la columna timestamp ya que esta va a ser única para cada fila de la tabla */
create table interrupciones_forks(
	col_timestamp timestamp default current_timestamp on update current_timestamp,
	tot_interrupts int,
	int_interrupts int,
	tot_forks int,
	int_forks int,
	primary key(col_timestamp));

/*Línea de creación de la tabla correspondiente al numero de procesos en ejecucion, en ella guardamos el numero de procesos, el timestamp para mostrar los datos a lo largo del tiempo y establecemos como clave primaria la columna timestamp ya que esta va a ser única para cada fila de la tabla */
create table nprocesos_ejecuciones(
	numero_procesos int,
	col_timestamp timestamp default current_timestamp on update current_timestamp,
	primary key(col_timestamp));


/*Línea de creación de la tabla correspondiente a las operaciones de entrada y salida, en ella guardamos el numero de operaciones por segundo, el numero de kb leidos, el numero de kb escritos, el timestamp para mostrar los datos a lo largo del tiempo, el numero de kb leidos y escritos por minuto y el numero de operaciones totales y establecemos como clave primaria la columna timestamp ya que esta va a ser única para cada fila de la tabla */
create table operaciones_es(
	operaciones_segundo int,
	kb_leidos int,
	kb_escritos int,
	col_timestamp timestamp default current_timestamp on update current_timestamp,
	kb_leidos1min int,
	kb_escritos1min int,
	operaciones_tot int,
	primary key(col_timestamp));

/*Línea de creación de la tabla correspondiente al registro de las sesiones, en ella guardamos un identificador para establecer como clave primaria, el timestamp para que los datos se actualicen con el tiempo, el usuario que realiza la conexion a la maquina, la ip de origen de la conexion, la longitud  y latitud de la misma para poder establecer su localizaion geografica */
create table registro_sesiones(
	id int AUTO_INCREMENT,
	fecha_registro timestamp default current_timestamp,
	nombre_servidor varchar(255),
	usuario varchar(100),
	ip_origen varchar(45),
	latitud decimal(10,8),
	longitud decimal(11,8),
	primary key(id));

/*Línea de creación de la tabla correspondiente al uso de cpu, en ella guardamos el uso de la cpu, el timestamp para ir mostrando los datos a lo largo del tiempo y establecemos como clave primaria la columna timestamp ya que esta va a ser única para cada fila de la tabla */
create table uso_cpu(
	uso_cpu int,
	col_timestamp timestamp default current_timestamp on update current_timestamp,
	primary key(col_timestamp));

/*Línea de creación de la tabla correspondiente al uso de la ram y la swap, en ella guardamos la memoria ram usada y libre, la memoria swap usada y libre y el timestamp para ir mostrando los datos a lo largo del tiempo y establecemos como clave primaria la columna timestamp ya que esta va a ser única para cada fila de la tabla */
create table uso_ram_swap(
	usada_ram float,
	libre_ram float,
	usada_swap float,
	libre_swap float,
	col_timestamp timestamp default current_timestamp on update current_timestamp,
	primary key(col_timestamp));

