
/**
* Practica Tema 5: DAYTIME UDP
*
* San Segundo Alvarez, Francisco Ivan
* Garcia Salinas, Daniel
*
*/

//Includes necesarios para el programa //
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>
#include <stdbool.h>
#include <ctype.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netdb.h>

//Definicion de algunos mensajes de error y algunas constantes numericas
#define ERROR_IP_ERRONEA "La IP introducida como parametro no es correcta"
#define ERROR_FORMATO_PUERTO "El puerto introducido como parametro no esta en el formato correcto"
#define ERROR_PUERTO_ERRONEO "El puerto introducido como parametro no es correcto"
#define ERROR_NUMERO_ARGUMENTOS "El numero de argumentos introducidos es incorrecto"
#define ERROR_OPCION_INCORRECTA "La opcion pasada como argumento no es correcta"
#define ERROR_CREACION_SOCKET "Se ha producido un error en la creaccion del socket"
#define ERROR_BIND_SOCKET "Se ha producido un error en la union del socket"
#define ERROR_IP_INVALIDA "La IP de destino no es valida"
#define ERROR_ENVIO_DATOS "Se ha producido un error en el envio de datos"
#define ERROR_RECIBIMIENTO_DATOS "Se ha producido un error en el recibimiento de datos"
#define ERROR_BUSQUEDA_SERVICIO "El servicio solicitado no se ha encontrado"
#define MAX_TAM 1000

//Declaracion de funciones a utilizar para la comprobacion de parametros
bool ipValida(char *ip);
bool opcionCorrecta(char *opcion);
bool soloDigitos(char* puerto);
bool puertoValido(int puerto);


int main(int argc, char **argv){
	//Definicion de algunas variables como el socket local, direcciones de origen y destino, el puerto al que se va a mandar los mensajes, cadena para guardar la direccion IP de destino en formato IP y la longitud de la direccion IP del cliente 
	//Definicion de una variable para guardar el socket
	int socketfd;
	//Defincion de una variable para guardar la direccion destino,en este caso, del servidor
	struct sockaddr_in miDireccion;
	//Defincion de una variable para guardar la direccion origen,en este caso, del cliente
	struct sockaddr_in direccionServidor;
	//Definicion de una variable para guardar el puerto al que vamos a mandar los mensajes al servidor
	int puerto;
	//Defincion de una variable en la que vamos a guardar la cadena de direccion IP destino que introduce el usuario como parametro
	char *direccionDestino;
	//Definicion de una variable en la que vamos a guardar la pregunta que le vamos a mandar al servidor
	char pregunta[MAX_TAM]= "Que dia es hoy?";
	//Definicion de una variable en la que vamos a guardar la respuesta que vamos a recibir del servidor
    char respuesta[MAX_TAM];
	//Definicion de una variable en la que vamos a guardar informacion sobre el servcio de red daytime
	struct servent *servicio;
	//Definicion de una variable en la que vamos a guardar la longitud de la direccion IP del origen
	socklen_t miLen;	
	
	//Comprobacion de que el numero de argumentos este dentro del rango permitido y en caso de que no se encuentre dentro del rango mostramos un error
	if(argc < 2 || argc > 4 || argc == 3){
		perror(ERROR_NUMERO_ARGUMENTOS);
	}
	if(argc == 2){
		
		// En caso de que el numero de argumentos introducidos por el usuario sea 2, comprobamos que el formato de la IP sea valido y obtenemos el puerto por defecto con la variable getservbyname
		if(ipValida(argv[1]) == false){
			perror(ERROR_IP_ERRONEA);
		}
		
		//Obtenemos el número de puerto bien conocico al que esta asigando por defecto el servico daytime
		if((servicio = getservbyname("daytime","udp")) == NULL){
			perror(ERROR_BUSQUEDA_SERVICIO);
			exit(-1);
		}
		
		//Una vez que hemos obtenido el numero de puerto se lo asignamos a la variable puerto
		puerto = servicio->s_port;
	}
	else{
		
		// Si el numero de argumentos introducidos por el usuario no son 2, comprobamos que el formatio de la IP sea valido y en caso contrario, mostramos un error
		if(ipValida(argv[1]) == false){
			perror(ERROR_IP_ERRONEA);
			exit(-1);
		}
		
		//Comprobacion de que la opcion para introducir el puerto sea la correcta y en caso contrario, mostramos un error
		if(opcionCorrecta(argv[2]) == false){
			perror(ERROR_OPCION_INCORRECTA);
			exit(-1);
		}
		
		//Comprobacion de que el puerto que introduzca el usuario solo tenga digitos, en caso de que esto no sea así, mostramos un error
		if(soloDigitos(argv[3]) == false){
			perror(ERROR_FORMATO_PUERTO);
			exit(-1);
		}
		
		//Comprobacion de que el puerto introducido por el usuario este dentro del rango correcto y en caso contrario, mostramos un error
		if(puertoValido(atoi(argv[3])) == false){
			perror(ERROR_PUERTO_ERRONEO);
			exit(-1);
		}
		
		//Guardamos el puerto introducido por el usuario
		puerto = atoi(argv[3]);
	}
	
	//Guardamos en la siguiente variable la direccion IP introducida por el usuario
	direccionDestino = argv[1];

	//Creamos el socket con la funcion socket, en caso de que de un error mostramos unn error
	if((socketfd = socket(AF_INET,SOCK_DGRAM,0)) == -1){
		perror(ERROR_CREACION_SOCKET);
		exit(-1);
	}
	
	miDireccion.sin_family = AF_INET;
	
	//Inicializamos el puerto del cliente que al utilizar el 0, el sistema operativo asignara un puerto libre al hacer la vinculacion
	miDireccion.sin_port = 0;
	
	//Inicializamos la direccion del cliente, al utilizar INADDR_ANY se va a asociar con todas las interfaces de red disponibles en el sistema
	miDireccion.sin_addr.s_addr = INADDR_ANY;

	//Vinculamos el socket a una aplicacion de red y en caso de que esto produzca un error, mostramos un mensaje
	if((bind(socketfd, (struct sockaddr *) &(miDireccion), sizeof(miDireccion)))==-1){
		perror(ERROR_BIND_SOCKET);
		exit(-1);
	}

	direccionServidor.sin_family = AF_INET;
	
	//Inicializamos el puerto del servidor al que vamos a mandar los paquetes, con la funcion htons convertimos el puerto de tipo int a formato de red
	direccionServidor.sin_port=htons(puerto);
	
	//Convertimos la direccion IP introducida por el usuario en formato punto a formato de red y con ella inicializamos el campo de la direccion del servidor, en caso de que esta conversion de error, mostramos un mensaje
	if((inet_aton(direccionDestino,(struct in_addr *) &(direccionServidor.sin_addr.s_addr))) == 0){
		perror(ERROR_IP_INVALIDA);
		exit(-1);			
	}
	
	//Mandamos el paquete al servidor y en caso de que este no se mande correctamente y de un error, mostramos un mensaje
	if((sendto(socketfd,(void *) pregunta,sizeof(pregunta),0,(struct sockaddr *) &(direccionServidor),sizeof(direccionServidor))) == -1){
		perror(ERROR_ENVIO_DATOS);
		exit(-1);
	}
	
	miLen=sizeof(miDireccion);
	
	//Recibimos el paquete de respuesta del servidor y lo guardamos en la variable respuesta, en caso de que de error, mostramos un mensaje
	if((recvfrom(socketfd,respuesta,sizeof(respuesta),0,(struct sockaddr *) &(miDireccion),&miLen)) == -1){
		perror(ERROR_RECIBIMIENTO_DATOS);
		return 0;
	}
	
	//Cerramos el socket
	close(socketfd);
	
	//Mostramos por pantalla la respuesta del servidor
	printf("%s\n",respuesta);

	return 0;
}

//Funcion para comprobar si la IP es valida y nos devuelve true en caso de que la direccion IP sea correcta y false en caso contrario
bool ipValida(char * ip){
	
	//Definicion de algunas variables necesarias para la funcion
	char direccion[MAX_TAM];
	bool valida = true;
	char *token = NULL;
	const char delimitador[2] = ".";
	int longitud = 0;

	//Copiamos la direccion IP introducida por el usuario en otra variable para luego separarla por campos
	strcpy(direccion,ip);
	
	//Separamos la cadena por tokens con el delimitador "." para obtener asi cada octeto de la direccion IP
	token = strtok(direccion, delimitador);
	
	//Bucle para comprobar si cada campo esta dentro del rango permitido para la direccion IP
	while(token != NULL){
		
		//Comprobamos que cada octeto no es menor que 0 y que no es mayor que 255 ya que este es el ultimo valor permitido
		if((strcmp(token, "0") < 0 || strcmp(token,"255") > 0) && strlen(token) >= strlen("255")){
			valida = false;
		}
		
		//Obtenemos el siguiente octeto de la direccion IP
		token = strtok(NULL, delimitador);
		longitud++;
	}

	//Si la direccion IP no tiene 4 octetos, la direccion IP no se considera valida
	if(longitud != 4){
		valida = false;
	}

	return valida;
}

//Funcion que comprueba si la opcion introducida para introducir el puerto es correcta
bool opcionCorrecta(char *opcion){
	
	bool valida = true;

	//Comparamos la opcion introducida por el usuario con la cadena "-p" para saber si son iguales o no
	if(strcmp(opcion,"-p") != 0){
		valida = false;
	}

	return valida;
}

//Funcion que comprueba si el puerto introducido por el usuario solo contiene digitos
bool soloDigitos(char *puerto){
	
	//Declaracion de algunas variables necesarias para la funcion
	int i = 0;
	bool valido = true;

	//Recorremos cada caracter de la cadena introducida por el usuario como puerto
	while(puerto[i] != '\0' && valido == true){
		
		//Comprobamos que ese caracter de la cadena es numerico
		if(!isdigit(puerto[i])){
			valido = false;
		}
		i++;
	}

	return valido;
}

//Funcion que comprueba si el puerto introducido por el usuario esta dentro del rango permitido
bool puertoValido(int puerto){
	bool valido = true;
	
	//Comprobamos si el puerto no es menos que 0 ni mayor que 65535 ya que este es el numero del ultimo puerto disponible en todas las maquinas
	if(puerto < 0 || puerto > 65535){
		valido=false;
	}
	
	return valido;
}

