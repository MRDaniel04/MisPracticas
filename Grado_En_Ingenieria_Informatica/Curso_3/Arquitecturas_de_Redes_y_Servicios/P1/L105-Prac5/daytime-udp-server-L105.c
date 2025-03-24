
/**
* Practica Tema 5: DAYTIME UDP
*
* San Segundo Alvarez, Francisco Ivan
* Garcia Salinas, Daniel
*
*/


//includes

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <limits.h>
#include <string.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/ip.h>
#include <netdb.h>
#include <ctype.h>

//definimos constantes y mensajes de error

#define BUFF_SIZE 30
#define MESSAGE_SIZE 30
#define MESSAGE_CLIENT 50
#define MESSAGE_PARAMETROS_INVALIDOS "Parametros Invalidos\n"
#define MESSAGE_HOST_INVALIDO "Error obteniendo el nombre de host\n"
#define MESSAGE_ERROR_SOCKET "Error creando el socket\n"
#define MESSAGE_ERROR_RECEPCION "Error al recibir el request del cliente\n"
#define MESSAGE_ERROR_ENVIO "Error al enviar datos del cliente\n"

//declaracion de funciones

int compruebapuerto(char* puerto);

//programa principal

int main(int argc, char **argv){

	//declaracion de variables
	
	struct sockaddr_in structsocket;												//socket creado para la recepcion del mensaje del cliente
	struct sockaddr_in structsocketemisor;											//socket creado para el envio de datos al cliente
	int sockfd;																		//descriptor de sockets
	short puerto;																	//puerto por el que se va a establecer la conexion
	char hostname[HOST_NAME_MAX+1];													//string que guarda el nombre de host
	FILE *fd;																		//descriptor de fichero, que sirve para almacenar temporalmente los datos que se van a enviar al cliente
	char buff[BUFF_SIZE],buffmessage[MESSAGE_SIZE],buffenvio[MESSAGE_CLIENT];		//distintos buffers que almacenaran temporalmente informacion (cadena recibida por el cliente, fecha obtenida del servicio date y mensaje a enviar al cliente respectivamente)
	socklen_t cliente_len;															//almacena la longitud de direcciones del socket															

	//comprobacion de parametros
	//sin especificacion del puerto
	if(argc==1){
		puerto = getservbyname("daytime","udp")->s_port;
	}
	//con especificacion del puerto
	else if(argc==3&&(strcmp(argv[1],"-p")==0)){
		if(compruebapuerto(argv[2])==0){
			puerto = atoi(argv[2]);
		}
		else{
			perror(MESSAGE_PARAMETROS_INVALIDOS);
			return 1;
		}
	}
	else{
		perror(MESSAGE_PARAMETROS_INVALIDOS);
		return 1;
	}
	//obtener el nombre del servidor
	if(gethostname(hostname,sizeof(hostname))<0){
		perror(MESSAGE_HOST_INVALIDO);
		return 1;
	}
	//creacion de un descriptor de socket para el intercambio de mensajes con el cliente
	if((sockfd = socket(AF_INET,SOCK_DGRAM,0))<0){
		perror(MESSAGE_ERROR_SOCKET);
		return 1;		
	}
	
	//Definimos la estructura structsocket con los parametros que se asociaran al socket
	
	structsocket.sin_family = AF_INET;
	structsocket.sin_port = htons(puerto);	//modificamos el valor del puerto con el de por defecto del servicio daytime o el especificado por el usuario (depeniendo de las opciones de inicializacion del servidor)
	structsocket.sin_addr.s_addr = INADDR_ANY;
	
	//Enlazar un socket con la direccion
	
	bind(sockfd,(struct sockaddr *) &structsocket,sizeof(structsocket));
	cliente_len = sizeof(structsocketemisor);
	
	//bucle de recepcion de mensajes
	while(1){
		
		//recepcion de la frase del emisor

		if(recvfrom(sockfd,buffmessage,sizeof(buffmessage),0,(struct sockaddr*)&structsocketemisor,&cliente_len)<0){
			perror(MESSAGE_ERROR_RECEPCION);
		}
		else{
			
			//obtenemos la fecha actual y la guardamos en el archivo temporal
			
			system("date > /tmp/date.txt");
			fd = fopen("/tmp/date.txt","r");
			if(fgets(buff,BUFF_SIZE,fd)==NULL){
				printf("Error");
				return 1;
			}
		}

		//formamos la cadena que se enviara finalmente al usuario

		strcpy(buffenvio,hostname);
		strcat(buffenvio,": ");
		strcat(buffenvio,buff);

		//envio de la cadena mediante la funcion sendto al cliente

		if(sendto(sockfd,buffenvio,sizeof(buffenvio),0,(struct sockaddr*)&structsocketemisor,cliente_len)<0){
			perror(MESSAGE_ERROR_ENVIO);
		}
	}
	close(sockfd);
	return 0;
}

//funcion para comprobar si el argumento puerto (argv[2]) es valido

int compruebapuerto (char* puerto){
	
	int i;
	int puertonum;

	//comprobamos que el valor del puerto no sea nulo

	if(puerto==NULL){
		return 1;
	}

	//comprobamos que el puerto sea un numero
	for(i=0;i<strlen(puerto);i++){
		if(!isdigit(puerto[i])){
			return 1;	
		}
	}

	//comprobamos que el puerto este entre los numeros posibles de puerto
	puertonum=atoi(puerto);
	if(puertonum<0 || puertonum>65535){
		return 1;
	}
	return 0;
}
