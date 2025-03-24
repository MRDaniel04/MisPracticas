#include <stdio.h>

int main(int argc, char *argv[]){
	FILE *fichero;
	char caracter,caracter2;
	int numchar=1,iteracion=0,*nulo=NULL;

	if(argc==1){
		fprintf(stdout,"UVazip: file1 [file2 ...]\n"); 
		return 1;
	}
	for(int i=1;i<argc;i++){	
		fichero=fopen(argv[i],"r");
		if(fichero==NULL){
			return 1;
		}
		caracter2=fgetc(fichero);
		do{
			if(caracter==caracter2){
				numchar+=1;	
			}
			else{
				if (iteracion!=0){
					fwrite(&numchar,sizeof(int),1,stdout);
					fwrite(&caracter,sizeof(char),1,stdout);
				}
				caracter=caracter2;
				iteracion+=1;
				numchar=1;
			}
			caracter2=fgetc(fichero);
		}while(caracter2!=EOF);
	}
	fwrite(&numchar,sizeof(int),1,stdout);
	fwrite(&caracter,sizeof(char),1,stdout);
	return 0;
}
