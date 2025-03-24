#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>
#include <fcntl.h>
#include <ctype.h>


//variables globales
int MAXPARAM = 20;
char error_message[30]="An error has occurred\n";
char prompt[7]="UVash> ";

//funciones del programa
void errorasignarmemoria();
void errorbasico();
int comandovalidoespaces(char *comando);
int comandovalidoseparadores(char *comando);
void ejecutarcomando(char** comando,char* comandosolo);
void ejecutarcd(int i,char** argumentos);
void ejecutarexit(char** argumentos);
char* quitarEspaciosExtra(char* str);

//programa principal
int main(int argc,char *argv[]) {
    //declaración de variables
    int filas, columnas;
    char* comando,*token,**comandodiv,**argumentos;
    size_t size=0;
    int linea=0,columna=0,i=0,sizecomando=0;
    const char delim = ' ';
    int limite=0,contadori=1;
    FILE *fichero,*stream;

    //comprobación de argumentos
    if(argc>=3){
		errorbasico();
	}
	else{
        //Apertura del fichero de lectura en caso de haberlo
		if(argc==2){
			fichero=fopen(argv[1],"r");
			if(fichero==NULL){
				errorbasico();
			}
			stream=fichero;
		}
        //El stream de lectura será la entrada estándar si no es un fichero
		if(argc==1){
			printf("%s",prompt);
			stream=stdin;
		}

        //Comienzo bucle while del shell
        columna=0;
        while(1){
            if((getline(&comando,&size,stream))!=-1){           //Lectura de lineas de la entrada elegida
                i=strlen(comando);
                comando[i]='\0';
                while(comando[i]!='\n'){
                    comando[i]='\0';
                    i--;
                }
                strcpy(comando,quitarEspaciosExtra(comando));   
                contadori=1;
                if(comandovalidoespaces(comando)==0){           //Comprobaciones de que el comando sea válido a partir de número de espacio y otros elementos separadores
                    if(comandovalidoseparadores(comando)==0){
                    if(comando[i-1]=='&'){
                        comando[i-1]='\0';
                    }
                    for(int  j = 0;j<strlen(comando);j++){
                        if(comando[j]=='&'){
                            contadori++;
                        }
                    }
                    // Reservar memoria para la matriz de punteros a cadenas de caracteres (uso para el paralelismo)
                    char ***matriz = (char ***)malloc(MAXPARAM * sizeof(char **));
                    if (matriz == NULL) {
                        errorasignarmemoria();
                    }

                    // Reservar memoria para cada fila de la matriz
                    for (int j = 0; j < contadori; j++) {
                        matriz[j] = (char **)malloc(contadori * sizeof(char *));
                        if (matriz[j] == NULL) {
                            errorasignarmemoria();
                        }
                    }

                    //Reservar memoria auxiliar despues meter el comando dividido en la matriz
                    comandodiv=(char**)malloc(sizeof(char**)*(MAXPARAM));
                    if(comandodiv==NULL){
                        errorasignarmemoria();
                    }
                    i=0;
                    //Comandodiv será un array de punteros a cadenas de caracteres
                    while((token=strsep(&comando,&delim))!=NULL&&i<MAXPARAM-1){
                        comandodiv[i]=token;
                        i++;
                    }
                    comandodiv[i]=NULL;
                    limite=0;
                    columna=0;
                    sizecomando=0;
                    //Rellenar la matriz dependiendo del numero de &s (numero de comandos paralelos)
                    for(int j=0;j<i;j++){
                        if(strcmp(comandodiv[j],"&")==0){
                            matriz[columna][j-limite]=NULL;
                            columna++;
                            limite = j+1;
                        }
                        else{
                            // Reserva espacio para una cadena de hasta 20 caracteres
                            matriz[columna][j-limite] = (char *)malloc(20 * sizeof(char)); 
                            sprintf(matriz[columna][j-limite], "%s", comandodiv[j]); 
                            sizecomando++;
                        }
                    }
                    matriz[columna][i-limite]=NULL;
                    sizecomando++;
                    for (int j=0;j<contadori;j++){
                        //ejecución de comandos
                        if(strcmp(matriz[j][0],"cd")==0){
                            ejecutarcd(i,matriz[j]);
                        }
                        else if(strcmp(matriz[j][0],"exit")==0){												
                            ejecutarexit(matriz[j]);	
                        }
                        else{
                            pid_t pid = fork(); // Creación de un proceso hijo
                            if (pid < 0)
                            { // Error en la creación del proceso
                                errorbasico();
                            }
                            else if (pid == 0){
                                ejecutarcomando(matriz[j],matriz[j][0]);
                            }
                        }
                    }
                    columna=0;   
                    free(matriz);
                    }
                }
                if(argc==1){
                    printf("%s",prompt);
                }
            }
			else{
				break;
			}
            for(int j=0;j<contadori;j++) {
                wait(NULL);
            }
        }
		fclose(stream);
        free(comandodiv);
		free(comando);
		return 0;
    }
}

void errorasignarmemoria(){
    fprintf(stderr,"Error al asignar memoria");
    exit(1);
}


//funion que imprime por la salida de errores el error básico
void errorbasico(){
    fprintf(stderr,"%s",error_message);
    exit(1);
}


//funcion que cerciora que el numero de separadores (& o >) sea válido
int comandovalidoseparadores(char *comando){
    for(int j=0;j<strlen(comando);j++){
        if(comando[j]!='&'&&comando[j]!='>'){
            return 0;
        }
    }
    fprintf(stderr,"%s",error_message);
}


//funcion que cerciora que el numero de espacios o de saltos de linea sea valido
int comandovalidoespaces(char *comando){
    for(int j=0;j<strlen(comando);j++){
        if(comando[j]!=' '&&comando[j]!='\n'){
            return 0;
        }
    }
    return 1;
}

//funcion que ejecuta el comando cd
void ejecutarcd(int i,char** argumentos){
	if(i!=2||chdir(argumentos[1])!=0){
		fprintf(stderr,"%s",error_message);
		return;
	}
}


//funcion que ejecuta el comando exit
void ejecutarexit(char** argumentos){
	if(argumentos[1]!=NULL){
		fprintf(stderr,"%s",error_message);
		return;
	}
}


//funcion que dependiendo del comando que otenga, llama a execvp y lo ejecuta
void ejecutarcomando(char** comando,char* comandosolo){
	char **argumentos;
	int i=1,dato,k=0;
	int outputfile;
	argumentos=(char**)malloc(sizeof(char**)*(MAXPARAM));
	if(argumentos==NULL){
		errorasignarmemoria();
	}	
	argumentos[0]=comandosolo;
	while(comando[i]!=NULL){
		argumentos[i]=comando[i];
		i++;
	}
    
    //redireccion y control de errores de redireccion
	for (int j=0;j<i;j++){
		if(strcmp(argumentos[j],">")==0){
			k=1;
			if(j==0||argumentos[j+1]==NULL||argumentos[j+2]!=NULL){
				errorbasico();
			}
			outputfile=open(argumentos[j+1],O_WRONLY|O_CREAT|O_TRUNC,0644);
			if(outputfile==-1){
				errorbasico();
			}
			dup2(outputfile,1);
			dup2(outputfile,2);
		}
		if(k!=0){
			argumentos[j]='\0';
		}
	}
    //ejecución del comando con execvp
	argumentos[i]=NULL;
    dato=execvp(comandosolo,argumentos); 
    if(dato==-1){
        errorbasico();
    }
	dup2(outputfile,1);
	dup2(outputfile,2);
	close(outputfile);
	free(argumentos);
    
}	

//funcion que permite reducir el número de espacios del comando para que este sea válido
char* quitarEspaciosExtra(char* str) {
    int i, j;
    int longitud = strlen(str);
    int centinela = 0;
    
    // Eliminar espacios extra al principio
    while (isspace(str[0])) {
        memmove(str, str + 1, longitud--);
    }
    
    // Eliminar espacios extra al final
    while (longitud > 0 && isspace(str[longitud - 1])) {
        str[--longitud] = '\0';
    }
    
    // Eliminar espacios extra entre palabras
    for (i = 0, j = 0; i < longitud; i++) {
        if (isspace(str[i])) {
            if (centinela) {
                str[j++] = ' ';
                centinela = 0;
            }
        } else {
            str[j++] = str[i];
            centinela = 1;
        }
    }
    str[j] = '\0';
    return str;
}
