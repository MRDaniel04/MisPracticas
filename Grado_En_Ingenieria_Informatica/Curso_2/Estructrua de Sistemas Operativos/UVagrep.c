#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <stdlib.h>

int contiene (char *linea,char *palabra);

int main(int argc, char *argv[]){
	FILE *fichero;
	char *linea;
	size_t longitud=0;
	int charleidos;
	
	if(argc==1){
		fprintf(stdout,"UVagrep: searchterm [file ...]\n");
		return 1;
	}
	if(argc==2){
		while((charleidos=getline(&linea,&longitud,stdin))!=-1){
			if(contiene(linea,argv[1])){
				fprintf(stdout,"%s",linea);
			}
		}
	}
	else{
		for (int i=2;i<argc;i++){
			fichero=fopen(argv[i],"r");
			if(fichero==NULL){
				fprintf(stdout,"UVagrep: cannot open file\n");
				return 1;
			}
			while((charleidos=getline(&linea,&longitud,fichero))!=-1){
				if(contiene(linea,argv[1])){
					fprintf(stdout,"%s",linea);
				}
			}
		}
		return 0;
	}
}

int contiene(char *linea,char *palabra){
	char caracter;
	int j=0,k=0;
	for (int i=0;i<strlen(linea);i++){
		k=0;
		if(linea[i]==palabra[k]){
			j=i;
			k+=1;
			while(linea[j+=1]==palabra[k]&&linea[j]!='\0'){
				k+=1;
				if(palabra[k]=='\0'){
					return 1;
				}
			}
		}
	}
	return 0;
}

