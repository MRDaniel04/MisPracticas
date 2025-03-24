/**
 * Practica Tema 8: ICMP-TIMESTAMP
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
#include <sys/select.h>
#include <sys/time.h>
#include <netinet/ip.h>
#include <netdb.h>
#include <ctype.h>
#include "ip-icmp.h"
#include <stdbool.h>

//declaracion de constantes y mensajes de error

#define TAM_IP 40
#define TAM_MAX 1000
#define ERROR_IP_INVALIDA "La ip especificada por el usuario no es correcta"
#define OPCION_INCORRECTA "La opcion especificada por el usuario no es correcta"
#define PARAM_INCORRECTOS "El numero de parametros especificado es incorrecto"
#define ERROR_FALLO_SOCKET "Se ha producido un error en la creacion del socket"
#define ERROR_BIND_SOCKET "Se ha producido un error al bindear el socket"
#define ERROR_ENVIO_DATOS "Se ha producido un error en el envio de los datos"
#define ERROR_RECEPCION_DATOS "Se ha producido un error en la recepcion de los datos"

//definicion de funciones

int calcularRtt(TimeStamp timestamp, TimeStampReply timestampReply);
int opcionCorrecta(char *opcion);
unsigned short calcular_checksum(TimeStamp bloquedatos,size_t tamanyo); 


int main(int argc , char *argv[]){
	//Definicion de variables
	bool verbose = false;										//Modo verbose para ir explicando paso a paso el proceso del timestamp
	int sockfd;													//Variable que guarda el descriptor de socket
	struct sockaddr_in miDireccion;								//Definicion de una variable para guardar la direccion origen, osea la del cliente
	struct sockaddr_in direccionServidor;						//Definicion de una variable para guardar la direccion destino, osea la del servidor
	char direccionDestino[TAM_IP];								//Variable para guardar la direccion del servidor 
	struct timeval tv,tv2;										//Estructura para el intervalo que se usa en la funcion gettimeofday()			
	TimeStamp timestamp;										//Estructura del mensaje timestamp
	TimeStampReply timestampReply; 								//Estructura del menssaje timestamp de respuesta
	int tipoRespuesta;											//Variable para guardar el tipo de respuesta 
	int codigoRespuesta;										//Variable para guardar el codigo de respuesta 
	socklen_t server_len;										//Variable para guardar la longitud de la direccion del destino
	fd_set readfds;												//Descriptor de lectura para el timeout
	int retval;													//Timeout para cerrar el socket

	//comprobacion de que en caso de que el numero de parametros sea valido

	if(argc == 2 || argc == 3){
		//comprobamos que la direccion destino sea valida
		if(gethostbyname(argv[1]) == NULL){
			fprintf(stderr,ERROR_IP_INVALIDA);
			exit(-1);
		}
		strcpy(direccionDestino,argv[1]);
		//comprobacion de que en caso de que el numero de parametros sea 3, la opcion sea la correcta
		if(argc == 3){
			if(opcionCorrecta(argv[2])){
				fprintf(stderr,OPCION_INCORRECTA);
				exit(-1);
			}
			verbose=true;
		}		
	}

	//numero de parametros incorrectos
	else{
		fprintf(stderr,PARAM_INCORRECTOS);
		exit(-1);
	}

	//inicializamos el socket
	if ((sockfd = socket(AF_INET, SOCK_RAW, IPPROTO_ICMP)) < 0) {
        fprintf(stderr,ERROR_FALLO_SOCKET);
        exit(-1);
    }
	//inicializamos el sirviente del cliente con los datos que sabemos
	miDireccion.sin_family = AF_INET;
	miDireccion.sin_port = 0;
	miDireccion.sin_addr.s_addr = INADDR_ANY;
	//inicializamos el sirviente del servidor con los datos
	direccionServidor.sin_family=AF_INET;
	direccionServidor.sin_port=0;
	//Convertimos la direccion IP introducida por el usuario en formato punto a formato de red y con ella inicializamos el campo de la direccion del servidor, en caso de que esta conversion de error, mostramos un mensaje
	if((inet_aton(direccionDestino,(struct in_addr *) &(direccionServidor.sin_addr.s_addr))) == 0){
		fprintf(stderr,ERROR_IP_INVALIDA);
		exit(-1);			
	}
	//Vinculamos el socket a una aplicacion de red y en caso de que esto produzca un error, mostramos un mensaje
	if((bind(sockfd, (struct sockaddr *) &(miDireccion), sizeof(miDireccion)))==-1){
		fprintf(stderr,ERROR_BIND_SOCKET);
		exit(-1);
	}

	//Formamos la parte de la cabecera ICMP que pertenece al mensaje del tipo timestamp
	timestamp.icmpHdr.type = 13; // Tipo de mensaje
	timestamp.icmpHdr.code = 0;  // Codigo del mensaje
	timestamp.icmpHdr.checksum=0; // Checksum del mensaje icmp
	//Formamos el resto del mensaje timestamp
	timestamp.pid = (unsigned short) getpid(); // Identificador del proceso
	timestamp.sequence = 0; // Numero de secuencia del mensaje timestamp
	gettimeofday(&tv,NULL);
	timestamp.originate = (int) tv.tv_sec*1000 + tv.tv_usec/1000; //Obtencion del tiempo actual
	timestamp.receive=0; // Timestamp del envio del mensaje al destino
	timestamp.transmit=0; // Timestamp de la recepcion del mensaje enviado en el destino
	timestamp.icmpHdr.checksum = (unsigned short)calcular_checksum(timestamp,sizeof(timestamp)); //Checksum del mensaje timestamp

	// En caso de que se seleccione la opcion verbose mostramos algunos campos del mensaje timestamp
	if(verbose){
		printf("-> Enviando Datagrama ICMP a Destino con IP: %s\n",direccionDestino);
		printf("-> Type: %u\n",timestamp.icmpHdr.type);
		printf("-> Code: %u\n",timestamp.icmpHdr.code);
		printf("-> PID: %hu\n",timestamp.pid);
		printf("-> Seq Number: %hu\n",timestamp.sequence);
		printf("-> Originate: %u\n",timestamp.originate);
		printf("-> Receive: %u\n",timestamp.receive);
		printf("-> Transmit: %u\n",timestamp.transmit);
		printf("-> Tamanyo del Datagrama: %ld bytes\n",sizeof(timestamp));
	}
	fflush(stdout);

	//mandamos el mensaje de tipo timestamp
	if((sendto(sockfd, &timestamp, sizeof(timestamp),0,(struct sockaddr *) &(direccionServidor),sizeof(direccionServidor))) == -1){
		fprintf(stderr,ERROR_ENVIO_DATOS);
		exit(-1);
	}
	printf("-> Timestamp Request enviado correctamente \n");
	//Configuramos el tiempo de espera del servidor
	tv2.tv_sec=5;
	tv2.tv_usec=0;
	//Monitorizar el socket con select
	FD_ZERO(&readfds);
	FD_SET(sockfd,&readfds);
	//Hacemos el select
	retval=select(sockfd+1,&readfds,NULL,NULL,&tv2);
	//En caso de que retval es -1 mostramos un mensaje ya que se ha producido un error en el select
	if(retval==-1){
		fprintf(stderr,"Error en el select");
		exit(-1);
	}
	// Si retval es 0 es que se ha acabado el timeout y cerramos el socket
	else if(retval==0){
		fprintf(stdout,"Cerrando socket");
		close(sockfd);
		exit(-1);
	}
	else{
		//Obtenemos la longitud de la direccion del destino
		server_len=sizeof(direccionServidor);
		//Recibimos el mensaje de respuesta
		if((recvfrom(sockfd,&timestampReply,sizeof(timestampReply),0,(struct sockaddr *)&(direccionServidor),&server_len))==-1){
			fprintf(stderr,ERROR_RECEPCION_DATOS);
			exit(-1);
		}
		printf("\n");
		printf("-> Timestamp Reply recibido desde %s\n",direccionDestino);

		//Obtenemos el tipo y el codigo de respuesta
		tipoRespuesta=(timestampReply.icmpMsg.icmpHdr.type);
		codigoRespuesta=(timestampReply.icmpMsg.icmpHdr.code);
		//En funcion del tipo y codigo de respuesta mostramos un mensaje u otro
		switch (tipoRespuesta){
			case 3:
				switch (codigoRespuesta){
					case 0:
						printf("Destination Unreachable: Net Unreachable (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
					case 1:
						printf("Destination Unreachable: Host Unreachable (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
					case 2:
						printf("Destination Unreachable: Protocol Unreachable (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
					case 3:
						printf("Destination Unreachable: Port Unreachable (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
					case 4:
						printf("Destination Unreachable: Fragmentation Needed (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
					case 5:
						printf("Destination Unreachable: Source Route Failed (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
					case 6:
						printf("Destination Unreachable: Destination Network Unknown (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
					case 7:
						printf("Destination Unreachable: Destination Host Unknown (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
					case 8:
						printf("Destination Unreachable:  Source Host Isolated (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;	
					case 11:
						printf("Destination Unreachable: Destination Network Unreachable for Type of Service (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
					case 12:
						printf("Destination Unreachable: Destination Host Unreachable for Type of Service (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
					case 13:
						printf("Destination Unreachable: Communication Administratively Prohibited (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
					case 14:
						printf("Destination Unreachable: Host Precedence Violation (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
					case 15:
						printf("Destination Unreachable: Precedence Cutoff in Effect (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
				}
				break;

			case 5:
				switch (codigoRespuesta){
					case 1:
						printf("Redirect: Redirect for Destination Host (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
					case 3:
						printf("Redirect: Redirect for Destination Host Based on Type-of-Service (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
				}
				break;

			case 11:
				switch (codigoRespuesta){
					case 0:
						printf("Time Exceeded: Time-To-Live Exceeded in Transit (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
					case 1:
						printf("Time Exceeded: Fragment Reassembly Time Exceeded (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
				}
				break;

			case 12:
				switch (codigoRespuesta){
					case 0:
						printf("Parameter Problem: Pointer indicates the error (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
					case 1:
						printf("Parameter Problem: Missing a Required Option (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
					case 2:
						printf("Parameter Problem: Bad Length (Type %c, Code %c)",tipoRespuesta,codigoRespuesta);
						break;
				}
				break;
			case 14:
				// En caso de que se seleccione la opcion verbose mostramos algunos campos y datos importantes del mensaje de respuesta
				if(verbose){
					printf("\n");
					printf("-> Originate: %u \n",timestampReply.icmpMsg.originate);
					printf("-> Receive: %u \n",timestampReply.icmpMsg.receive);
					printf("-> Transmit: %u \n",timestampReply.icmpMsg.transmit);
					printf("-> RTT: %u \n",calcularRtt(timestamp,timestampReply));
					printf("-> TTL: %u \n",timestampReply.ipHdr.TTL);
					printf("-> Tamanyo del Datagrama: %ld bytes \n",sizeof(timestampReply));
				}
				printf("-> Respuesta Correcta (Type %u, Code %u) \n",tipoRespuesta,codigoRespuesta);
				break;
		}
	}
	//Cerramos el socket ya que el programa ha terminado
	close(sockfd);
	return 0;
}

//Funcion para calcular el rtt 
int calcularRtt(TimeStamp timestamp, TimeStampReply timestampReply){
	struct timeval tvActual;
	//Obtenemos el tiempo actual
	gettimeofday(&tvActual,NULL);
	//Pasamos el tiempo actual a numero
	int actual=(int) tvActual.tv_sec*1000 + tvActual.tv_usec/1000;
	//Devolvemos el resultado de calcular el rtt con los distintos campos de los distintos mensajes
	return (timestampReply.icmpMsg.receive - timestamp.originate)+(actual-timestampReply.icmpMsg.transmit);
}

//Funcion que comprueba si la opcion intoducida por el usuario es la correcta
int opcionCorrecta(char *opcion){

	return (strcmp(opcion,"-v"));

}

//Funcion que calcula el checksum del mensaje timestamp			
unsigned short calcular_checksum(TimeStamp bloquedatos,size_t tamanyo){
    //Variable que guarda el tamaño del datagrama entre 2
	int num_half_words=sizeof(bloquedatos)/2;
	//Variable de tipo puntero que apunta al comienzo del datagrama
    unsigned short *comienzo = (unsigned short*) &bloquedatos;
    //Variable que va acumulando la suma de los distintos campos del datagrama
	unsigned int suma = 0;
    int i;
    //Bucle que va recorriendo el datagrama y calculando la suma de los distintos campos
	for (i=0;i<num_half_words;i++){
	suma+=(unsigned int)*comienzo;
        comienzo++;
    }

	//Sumamos la parte alta del acumulador a la parte baja para que la suma en complemento a uno sea correcto
    suma = (suma >> 16)+(suma & 0x0000ffff);
	//Volvemos a repetir la suma en caso de que se haya producido desbordamiento en la operacion anterior
    suma = (suma >> 16)+(suma & 0x0000ffff);

	//Devolvemos los 16 bits de "menos peso" del checksum
    return (unsigned short)(~suma & 0x0000ffff);
}
