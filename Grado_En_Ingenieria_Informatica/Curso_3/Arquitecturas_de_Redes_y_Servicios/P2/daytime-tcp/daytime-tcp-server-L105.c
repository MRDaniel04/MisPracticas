/**
 * Practica Tema 6: DAYTIME TCP SERVER
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
#include <signal.h>
#include <sys/wait.h>

//definimos constantes y mensajes de error

#define BUFF_SIZE 30
#define MESSAGE_SIZE 30
#define MESSAGE_CLIENT 50
#define MESSAGE_PARAMETROS_INVALIDOS "Parametros Invalidos\n"
#define MESSAGE_HOST_INVALIDO "Error obteniendo el nombre de host\n"
#define MESSAGE_ERROR_SOCKET "Error creando el socket\n"
#define MESSAGE_ERROR_BIND "Error en la vinculacion del socket\n"
#define MESSAGE_ERROR_RECEPCION "Error al recibir el request del cliente\n"
#define MESSAGE_ERROR_ENVIO "Error al enviar datos del cliente\n"
#define MESSAGE_FIN "Fin de la conexion\n"
#define MESSAGE_FIN_SERVIDOR "Fin del servidor\n"
#define MAX_CONNECTIONS 10
#define MESSAGE_ERROR_LISTEN "Error al activar el socket en modo escucha\n"
#define MESSAGE_ERROR_ACCEPT "Error al aceptar la conexion entrante del cliente\n"
#define MESSAGE_ERROR_FORK "Error al crear el proceso hijo\n"
#define ERROR_APERTURA_FICH "Error abriendo el fichero \n"

//variables globales

int sockfd;

//declaracion de funciones

int compruebapuerto(char* puerto);
void signal_handler(int sig);

//programa principal

int main(int argc, char **argv){

//declaracion de variables
	
	struct sockaddr_in structsocket;
	struct sockaddr_in structsocketemisor;
	int serverfd;
	short puerto;
	char hostname[HOST_NAME_MAX+1];
	FILE *fd;
	char buff[BUFF_SIZE],buffmessage[MESSAGE_SIZE],buffenvio[MESSAGE_CLIENT];
	socklen_t cliente_len;
	int pid;
	char filename[256],comando[256];


//comprobacion de parametros
	//en caso de que el numero de argumentos sea nulo (unicamente el ejecutable), debemos obtener el puerto mediante getservbyname con la opción de tcp
	if(argc==1){
		puerto = getservbyname("daytime","tcp")->s_port;
	}
	//en caso contrario, comprobaremos que el primer argumento sea correcto (-p) y que el segundo corresponda a un numero de puerto valido
	else if(argc==3&&(strcmp(argv[1],"-p")==0)){
		if(compruebapuerto(argv[2])==0){
			puerto = htons(atoi(argv[2]));
		}
		//mensaje de error en caso de que el numero de puerto no sea correcto
		else{
			fprintf(stderr,MESSAGE_PARAMETROS_INVALIDOS);
			exit(-1);
		}
	}
	//mensaje de error en caso de que las especificaciones de los parametros no se cumplan
	else{
		fprintf(stderr,MESSAGE_PARAMETROS_INVALIDOS);
		exit(-1);
	}

	//creacion de un descriptor de socket
	if((sockfd = socket(AF_INET,SOCK_STREAM,0))<0){
		fprintf(stderr,MESSAGE_ERROR_SOCKET);
		exit(-1);		
	}
	
	//Definimos la estructura structsocket con los parametros que se asociaran al socket
	structsocket.sin_family = AF_INET;
	structsocket.sin_port = puerto;
	structsocket.sin_addr.s_addr = INADDR_ANY;
	
	//Enlazar un socket con la direccion mediante bind
	if((bind(sockfd,(struct sockaddr *) &structsocket,sizeof(structsocket)))==-1){
		fprintf(stderr,MESSAGE_ERROR_BIND);
		exit(-1);
	}

	//obtenemos la longitud de la estructura de socket que almacenara las caracteristicas del cliente para poder usarla mas tarde
	cliente_len = sizeof(structsocketemisor);

	//Socket se pone en estado de escucha gracias a la funcion listen y establecemos el tamano de la cola de solicitudes
	if(listen(sockfd,MAX_CONNECTIONS)<0){
		fprintf(stderr,MESSAGE_ERROR_LISTEN);
		close(sockfd);
		exit(-1);
	}

	//bucle de recepcion de mensajes
	while(1){
		//se acepta la conexion del cliente
		if((serverfd=accept(sockfd,(struct sockaddr*)&structsocketemisor,&cliente_len))<0){
			fprintf(stderr,MESSAGE_ERROR_ACCEPT);
			continue;
		}
		//fork para poder procesar varias solicitudes en el servidor al mismo tiempo
		if((pid=fork())==-1){
			fprintf(stderr,MESSAGE_ERROR_FORK);
			//en caso de error, mostramos mensaje de error y shutdown + recv + close antes de finalizar el programa para evitar fallos
			shutdown(serverfd,SHUT_RDWR);
			if(recv(serverfd,buffmessage,sizeof(buffmessage),0)<0){
				fprintf(stderr,MESSAGE_ERROR_RECEPCION);
			}
			close(serverfd);
			exit(-1);
		}
		//en caso de que sea el hijo, procedemos con la funcionalidad del servidor
		else if(pid==0){
			//Obtenemos el nombre de la maquina
			if(gethostname(hostname,sizeof(hostname))<0){
				//en caso de error, mostramos mensaje de error y shutdown + recv + close antes de finalizar el programa para evitar fallos
				fprintf(stderr,MESSAGE_HOST_INVALIDO);	
				shutdown(serverfd,SHUT_RDWR);
				if(recv(serverfd,buffmessage,sizeof(buffmessage),0)<0){
					fprintf(stderr,MESSAGE_ERROR_RECEPCION);
				}		
				close(serverfd);
				exit(-1);
			}
			//recepcion del mensaje del emisor
			if(recv(serverfd,buffmessage,sizeof(buffmessage),0)<0){
				//en caso de error, mostramos mensaje de error y shutdown + recv + close antes de finalizar el programa para evitar fallos
				fprintf(stderr,MESSAGE_ERROR_RECEPCION);
				shutdown(serverfd,SHUT_RDWR);
				if(recv(serverfd,buffmessage,sizeof(buffmessage),0)<0){
					fprintf(stderr,MESSAGE_ERROR_RECEPCION);
				}
				close(serverfd);
				exit(-1);
			}
			//creamos un fichero temporal que va a almacenar el mensaje que se le enviara al cliente
			sprintf(filename,"/tmp/date.txt_%d.txt",getpid());
			//creamos el comando que ejecutaremos para poder obtener la fecha y almacenarla en el fichero temporal
			sprintf(comando,"date>%s",filename);
			system(comando);
			//abrimos el fichero temporal
			if((fd=fopen(filename,"r"))==NULL){
				//en caso de error, mostramos mensaje de error y shutdown + recv + close antes de finalizar el programa para evitar fallos
				printf(ERROR_APERTURA_FICH);
				shutdown(serverfd,SHUT_RDWR);
				if(recv(serverfd,buffmessage,sizeof(buffmessage),0)<0){
					fprintf(stderr,MESSAGE_ERROR_RECEPCION);
				}
				close(serverfd);
				exit(-1);
			}
			//obtenemos la fecha almacenada en el fichero temporal para almacenarla en el buffer de envio
			if(fgets(buff,BUFF_SIZE,fd)==NULL){
				printf("Errorobteniendofecha\n");
				//en caso de error, mostramos mensaje de error y shutdown + recv + close antes de finalizar el programa para evitar fallos
				shutdown(serverfd,SHUT_RDWR);
				if(recv(serverfd,buffmessage,sizeof(buffmessage),0)<0){
					fprintf(stderr,MESSAGE_ERROR_RECEPCION);
				}
				close(serverfd);
				exit(-1);
			}
			///cerramos el fichero
			fclose(fd);
			//formamos la cadena que enviaremos finalmente al usuario (como se encuentra especificado en la practica)
			strcpy(buffenvio,hostname);
			strcat(buffenvio,": ");
			strcat(buffenvio,buff);
			//mandamos la cadena ya formateada al cliente
			if(send(serverfd,buffenvio,sizeof(buffenvio),0)<0){
				//en caso de error, mostramos mensaje de error y shutdown + recv + close antes de finalizar el programa para evitar fallos
				fprintf(stderr,MESSAGE_ERROR_ENVIO);	
				shutdown(serverfd,SHUT_RDWR);
				if(recv(serverfd,buffmessage,sizeof(buffmessage),0)<0){
					fprintf(stderr,MESSAGE_ERROR_RECEPCION);
				}
				close(serverfd);
				exit(-1);
			}
			//cerramos la conexion
			shutdown(serverfd,SHUT_RDWR);
			printf(MESSAGE_FIN);
			//cerramos el socket del cliente
			close(serverfd);
			//nos vamos sin ningun error
			exit(0);
		}
	}
	return 0;
}

//funcion para comprobar si el argumento puerto (argv[2]) es valido
int compruebapuerto (char* puerto){
	//declaracion de variables para la funcion
	int i;
	int puertonum;
	//comprobamos que el puerto no sea nulo
	if(puerto==NULL){
		return 1;
	}

	//comprobamos que el puerto sea un numero
	for(i=0;i<strlen(puerto);i++){
		if(!isdigit(puerto[i])){
			return 1;	
		}
	}
	//obtenemos el valor numerico del puerto
	puertonum=atoi(puerto);
	//comprobamos que el puerto este entre los numeros posibles de puerto
	if(puertonum<0 || puertonum>65535){
		return 1;
	}
	return 0;
}


//manejador de senales para que en caso de que el usuario decida finalizar el servidor, este finalice correctamente
void signal_handler(int signal){
	//comprobacion de que la senal equivale a ctrl. + C
	if(signal==SIGINT){
		//esperamos a que todos los hijos acaben
		while(waitpid(-1,NULL,WNOHANG)>0);
		//finalizamos correctamente
		shutdown(sockfd,SHUT_RDWR);
		printf(MESSAGE_FIN);
		close(sockfd);
		exit(0);
	}
}
