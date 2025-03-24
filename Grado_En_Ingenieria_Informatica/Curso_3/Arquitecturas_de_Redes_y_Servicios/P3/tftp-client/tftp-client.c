/**
* Practica Tema 7: TFTP client
*
* San Segundo Alvarez, Francisco Ivan
* Garcia Salinas, Daniel
*
*/

//includes

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <unistd.h>
#include <limits.h>
#include <string.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/ip.h>
#include <netdb.h>
#include <ctype.h>

//declaracion de constantes y mensajes de error

#define TAM_IP 40
#define NUM_PARAM_INVALIDOS "El numero de parametros no es correto\n"
#define ERROR_IP_INVALIDA "La ip no es correcta\n"
#define OPCION_INCORRECTA "Las opciones especificadas por el usuario son incorrectas\n"
#define OPCION_EXTRA_INCORRECTA "Las opciones extras especificadas por el usuario son incorrectas\n"
#define ERROR_CREACION_SOCKET "El socket no se ha podido crear\n"
#define ERROR_BIND_SOCKET "Se ha producido un error en la vinculacion del socket\n"
#define ERROR_ABRIENDO_FICHERO "Se ha producido un error en la apertura del fichero\n"
#define ERROR_RECIBIMIENTO_DATOS "Se ha producido un error en el recibimiento de datos\n"
#define ERROR_ENVIO_DATOS "Ha habido un error en el envio de los datos\n"
#define ERROR_RECIBIMIENTO_DATOS_ORDENADOS "Ha habido un error en la recepcion de los datos en orden\n"
#define ERROR_LECTURA_DATOS_FICHERO "Se ha producido un error en la lectura de los datos del fichero a escribir en el servidor\n"
#define ERROR "05"
#define MODE "octet"
#define MAX_TAM 1000

#define FICHERO_NO_ENCONTRADO "El fichero no se ha podido encontrar en el servidor\n"
#define ACCESS_VIOLATION "No se pudo acceder al servidor\n"
#define ALMACENAMIENTO_LLENO "El servidor tiene el almacenamiento lleno\n"
#define OPERACION_ILEGAL "La operacion estipulada no esta entre las especificadas por el servidor\n"
#define IDENTIFICADOR_DESCONOCIDO "El identificador de la transferencia es incorrecto\n"
#define FICHERO_EXISTE "No se puede crear un fichero que ya existe\n"
#define USER_UNKNOWN "El usuario es desconocido por el servidor\n"

//declaracion de funciones

bool ipValida(char *ip);
bool opcionCorrecta(char *opcion,int i);
void errormensaje(int err1, int err2, char *printear);

//codigo principal

int main(int argc, char *argv[]){
    //Definimos una variable para guadar el socket que creamos en el cliente
    int sockfd;
	//Definimos una variable para guadar la direccion IP introducida como parametro
    char direccionDestino[TAM_IP];
	//Definicion de una variable para guardar la direccion origen, osea la del cliente
	struct sockaddr_in miDireccion;
	//Definicion de una variable para guardar la direccion destino, osea la del servidor
	struct sockaddr_in direccionServidor;
	//Definimos array para guardar el mensaje que se enviara al servidor
	char buffer[516];
	//Guardamos la opcion que se quiera realizar
	char opcion[9];
	//Guardamos el nombre del fichero que se quiere abrir
	char fichero[MAX_TAM];
	//Desplazamiento a la hora de crear el mensaje
	int desplazamiento;
	//Definimos una variable para guardar si se quiere activar el modo verbose o no
	bool verbose=false;
	//Variable para guardar el numero de bytes que se envian desde el servidor en la operacion de lectura
	int nbytes;
	//Descriptor de fichero
	FILE *ficherofd;
	//Variable para contar iteraciones del bucle do while de lectura
	int contador=0;
	//Variable para guardar la longitud de la direccion IP del servidor
	socklen_t serverLen;
	//Variable para guardar el bloque de datos leido de nuestro fichero cuando se pone la opcion de escritura
	char data[512];
	//Definimos una variable para guardar el numero de bloque recibido
	int numBlock;
	//Definimos una variable para guardar el numero de bloque del ACK recibido
	int numBlockACK;
	//Definimos una variable para guardar el mensaje del error(errstring)
	char stringerror[500];
	//Variable para que en caso de fallo no printee que esta todo bien
	int fallo=0;

    //Comprobamos que el numero de parametros sea el adecuado
    if(argc!=5 && argc!=4){
        fprintf(stderr,NUM_PARAM_INVALIDOS);
        return 1;
    }
    //Comprobamos que la ip sea valida
    if(!ipValida(argv[1])){
        fprintf(stderr,ERROR_IP_INVALIDA);
        return 1;
    }
    //Comprobamos que la opcion que se pasa como parametro esta entre las posibles
    if(!opcionCorrecta(argv[2],1)){
        fprintf(stderr,OPCION_INCORRECTA);
        return 1;
    }
    //Comprobamos que la opcion para el modo verbose sea la correcta
    if(argc==5){
        if(!opcionCorrecta(argv[4],2)){
            fprintf(stderr,OPCION_EXTRA_INCORRECTA);
            return 1;
        }
		verbose=true;
    }
	//Guardamos la opcion de lectura/escritura en la variable correspondiente
	if(strcmp(argv[2],"-r")==0){
		strcpy(opcion ,"lectura");
	}
	else{
		strcpy(opcion,"escritura");
	}
	//Copiamos la cadena compuesta por la direccion IP a una variable especifica
	strcpy(direccionDestino,argv[1]);

	//Copiamos el nombre del fichero a la variable indicada para ello
	strcpy(fichero,argv[3]);

	//Iniciamos el desplazamiento
	desplazamiento=0;

	//Inicializamos el socket
	sockfd=socket(AF_INET,SOCK_DGRAM,0);
    	if(sockfd  == -1){
		fprintf(stderr,ERROR_CREACION_SOCKET);
		return 1;
	}
	//Inicializamos nuestra direccion con los datos que sabemos
	miDireccion.sin_family = AF_INET;
	miDireccion.sin_port = 0;
	miDireccion.sin_addr.s_addr = INADDR_ANY;

	//Inicializamos la direccion del servidor con los datos que sabemos
	direccionServidor.sin_family=AF_INET;
	direccionServidor.sin_port=getservbyname("tftp","udp")->s_port;
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

	//Formamos el primer mensaje para enviar al servidor
	//Introducimos el codigo de operacion
	buffer[0]=0;
	if(strcmp(opcion,"lectura")==0){
		buffer[1]=1;
	}
	else{
		buffer[1]=2;
		if((ficherofd=fopen(fichero,"r"))==NULL){
			fprintf(stderr,ERROR_ABRIENDO_FICHERO);
			exit(-1);
		}
		fclose(ficherofd);
	}
	desplazamiento+=2;
	//Anadimos el fichero
	strcpy(&buffer[desplazamiento],fichero);
	desplazamiento+=(strlen(fichero)+1);
	//Anadimos el modo de transmision
	strcpy(&buffer[desplazamiento],MODE);
	desplazamiento+=(strlen(MODE)+1);
	//Imprimimos en caso de que se indicara la opcion verbose
	if(verbose){
		printf("Enviada solicitud de %s de %s a servidor tftp (%s)\n",opcion,fichero,direccionDestino);
	}
	//Almacenamos el tamano de la direccion del servidor para usarla despues
	serverLen=sizeof(direccionServidor);
	//Mandamos el paquete al servidor y en caso de que este no se mande correctamente y de un error, mostramos un mensaje
	if((nbytes=sendto(sockfd, buffer,desplazamiento,0,(struct sockaddr *) &(direccionServidor),sizeof(direccionServidor))) == -1){
		fprintf(stderr,ERROR_ENVIO_DATOS);
		exit(-1);
	}
	//En caso de que sea una operacion de lectura abrimos el fichero
	if(strcmp(opcion,"lectura")==0){
		//Si el fichero no se ha podido abrir correctamente, mostramos un error
		if((ficherofd=fopen(fichero,"w"))==NULL){
			fprintf(stderr,ERROR_ABRIENDO_FICHERO);
			exit(-1);
		}
		//Mientras que el numero de datos recibidor del servidor no sea menor de 512 bytes continuamos recibiendo bloques de datos
		do{
			//Recibimos el paquete de respuesta del servidor y lo guardamos en la variable buffer, en caso de que de error, mostramos un mensaje
			nbytes=recvfrom(sockfd,buffer,516,0,(struct sockaddr *)&(direccionServidor),&serverLen);
			if(nbytes == -1){
					fprintf(stderr,ERROR_RECIBIMIENTO_DATOS);
					fallo=1;
					break;
			}
			//Aumentamos el contador de bloques recibidos
			contador+=1;
			//Nos aseguramos de que el servidor nos envie ACK y no un mensaje de error
			if(buffer[0]==0&&buffer[1]==5){
				//obtenemos el mensaje de error
				strcpy(stringerror,&buffer[4]);
				//llamamos a la funcion que trata el codigo de error pasandole el numero del error y el mensaje de error
				errormensaje(buffer[2],buffer[3],stringerror);
			}
			//Obtenemos el numero de bloque
			numBlock=0;
			numBlock = (unsigned char) buffer[2]*256;
			numBlock+= (unsigned char) buffer[3];
			//Si el numero de bloque no coincide con el que deberia, mostramos un error
			if(contador==numBlock){
				//Imprimimos en caso de que se indique la opcion
				if(verbose){
					printf("Recibido bloque del servidor tftp\n");
					printf("Es el bloque numero %i\n",contador);
					fflush(stdout);	
				}
				//Escribimos el mensaje enviado en el fichero abierto para almacenar los datos que recibimos del servidor
				fwrite(&buffer[4],1,nbytes-4,ficherofd);
				//Reiniciamos el desplazamiento para enviar un mensaje de ACK
				desplazamiento=0;
				//Formamos el mensaje de ACK
				buffer[0]=0;
				buffer[1]=4;
				desplazamiento+=2;
				desplazamiento += 2;
				//Enviamos el mensaje de ack. Si no se ha podido mandar correctamente, mostramos un error
				if((sendto(sockfd,(void *) buffer,desplazamiento,0,(struct sockaddr *) &(direccionServidor),sizeof(direccionServidor))) == -1){
					fprintf(stderr,ERROR_ENVIO_DATOS);
					fallo=1;
					break;
				}

			}
			else{
				fprintf(stderr,ERROR_RECIBIMIENTO_DATOS);
				fallo=1;
				break;
			}
		}while(nbytes==516);
		//Cuando el numero de datos recibidos es menor que 512, es que era el ultimo bloque y por tanto, cerramos el fichero 
		if(verbose&&fallo==0){
			printf("El bloque %i era el ultimo: cerramos el fichero\n",contador);
		}
		fclose(ficherofd);
	}
	//En caso de que la opcion establecida por el usuario sea la opcion de escritura de un archivo
	else{
		//Abrimos el fichero con la opcion de lectura
		if((ficherofd=fopen(fichero,"r"))==NULL){
			fprintf(stderr,ERROR_ABRIENDO_FICHERO);
			exit(-1);
		}
		//Recibimos el mensaje del servidor tras haberle mandado el mensaje de escritura en el fichero
		if(recvfrom(sockfd,buffer,516,0,(struct sockaddr *) &(direccionServidor),&serverLen)==-1){
			fprintf(stderr,ERROR_RECIBIMIENTO_DATOS);
			exit(-1);
		}
		if(verbose){
					printf("Recibido ACK del servidor a la solicitud de escritura");
					fflush(0);
		}
		//Nos aseguramos de que el servidor nos envie ACK y no un mensaje de error
		if(buffer[0]==0&&buffer[1]==5){
			//obtenemos el mensaje de error
			strcpy(stringerror,&buffer[4]);
			//llamamos a la funcion que trata el codigo de error pasandole el numero de error y el mensaje de error
			errormensaje(buffer[2],buffer[3],stringerror);
		}
		//Obtenemos el numero de bloque
		numBlock=0;
		numBlock= (unsigned char) buffer[2]*256;
		numBlock+= (unsigned char) buffer[3];
		//Comprobamos que el numero de bloque sea correcto
		if(contador==numBlock){
		//comienzo del bucle de envio de mensajes de tipo data y recepcion de ACKs del servidor
			do{
				//Sumamos uno al contador y al numero de bloque para mandar el bloque 1 y recibir el ACK 1
				contador+=1;
				numBlock+=1;
				//Obtenemos el bloque de datos a partir del fichero abierto
				if((nbytes=fread(data,1,512,ficherofd))<0){
					fprintf(stderr,ERROR_LECTURA_DATOS_FICHERO);
					fallo=1;
					break;
				}
				//Creamos el mensaje que se va a enviar al servidor
				desplazamiento=0;
				//Insertamos el codigo de operacion en el bloque que vamos a enviar al servidor
				buffer[0]=0;
				buffer[1]=3;
				desplazamiento+=2;
				//Insertamos el numero de bloque en el bloque que vamos a enviar al servidor
				buffer[2]=(int)(numBlock/256);
				buffer[3]=(int)(numBlock%256);
				//Anadimos los datos del fichero en el bloque que vamos a enviar al servidor
				desplazamiento+=2;
				strcpy(&buffer[4],data);
				//Enviamos el bloque al servidor (el size de lo que se manda equivale a nbytes + 4 que serian los bytes del bloque + codigo de operacion)
				if((sendto(sockfd,buffer,nbytes+4,0,(struct sockaddr *) &(direccionServidor),sizeof(direccionServidor))) == -1){
					fprintf(stderr,ERROR_ENVIO_DATOS);
					fallo=1;
					break;
				}
				//Imprimimos en caso de que se especifique
				if(verbose){
					printf("Enviado el bloque de datos numero %i al servidor TFTP\n",numBlock);
					fflush(0);
				}
				//Esperamos respuesta ACK del servidor
				if((recvfrom(sockfd,buffer,4,0,(struct sockaddr *) &(direccionServidor),&serverLen))==-1){
					fprintf(stderr,ERROR_RECIBIMIENTO_DATOS);
					fallo=1;
					break;
				}
				//Nos aseguramos de que el servidor nos envie ACK y no un mensaje de error
				if(buffer[0]==0&&buffer[1]==5){
					//obtenemos el mensaje de error
					strcpy(stringerror,&buffer[4]);
					//llamamos a la funcion que trata el codigo de error pasandole el numero de error y el mensaje de error
					errormensaje(buffer[2],buffer[3],stringerror);
				}
				//Obtenemos el numero de bloque del ACK
				numBlockACK=0;
				numBlockACK= (unsigned char) buffer[2]*256;
				numBlockACK+= (unsigned char) buffer[3];
				//Comprobamos que el numero de ACK recibido por parte del servidor sea el del bloque que hemos enviado
				if(numBlock!=numBlockACK){
					fprintf(stderr,ERROR_RECIBIMIENTO_DATOS);
					fallo=1;
					break;
				}
				//Imprimimos en caso de que se indique la opcion
				if(verbose){
					printf("Recibido ACK del servidor tftp\n");
					printf("Es el ACK del bloque numero %i\n",contador);
					fflush(stdout);	
				}
			}while(nbytes==512);
			if(verbose&&fallo==0){
				printf("Se ha copiado el archivo\n");
				fflush(stdout);
			}
		fclose(ficherofd);
		}
		//Mensaje de error que indica que hubo un problema en el recibimiento de los datos
		else{
				fprintf(stderr,ERROR_RECIBIMIENTO_DATOS);
				exit(-1);
		}
	}
	//Cerramos el socket ya que hemos terminado
	close(sockfd);
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
bool opcionCorrecta(char *opcion,int i){
	
	bool valida = true;

	//Comparamos la opcion introducida por el usuario con la cadena "-p" para saber si son iguales o no
	if(i==1 && (strcmp(opcion,"-r")!= 0 && strcmp(opcion,"-w") != 0)){
		valida = false;
	}
	//Si queremos comprobar que la segunda opcion sea -v, entramos esta condicion
    if(i==2 && (strcmp(opcion,"-v") != 0)){
		valida = false;
	}
	//devolvemos si la opcion es valida o no
	return valida;
}

//funcion que printea el mensaje de error y sale del programa
void errormensaje(int err1, int err2,char *printear){
	fprintf(stderr,"errcode:%i%i\nerrstring:%s\n",err1,err2,printear);
	exit(-1);
}




