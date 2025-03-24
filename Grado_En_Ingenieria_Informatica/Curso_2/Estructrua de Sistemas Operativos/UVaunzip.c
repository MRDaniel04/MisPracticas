#include <stdio.h>

int main (int argc, char *argv[]){
	FILE *fichero;
	char caracter;
	int digito,centinela=0,enteroleido;

	if(argc==1){
		fprintf(stdout,"UVaunzip: file1 [file2 ...]\n");
		return 1;	
	}

	for(int i=1;i<argc;i++){
		centinela=0;
		fichero = fopen(argv[i],"r");
		if(fichero==NULL){
			return 1;
		}
		while(centinela==0){
			enteroleido=fread(&digito,sizeof(digito),1,fichero);
			if(enteroleido==1){
				fread(&caracter,sizeof(caracter),1,fichero);
				for(int i=0;i<digito;i++){
					fprintf(stdout,"%c",caracter);
				}
			}
			else{
				if(feof(fichero)){
					centinela=1;
				}
				else{
					printf("Error lectura");
				}
			}
		}
	}
		
}
