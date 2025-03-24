#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]){
	char d;
	FILE* fichero=NULL;
	
	if(argc==1){
		return 0;
	}

	for(int i=1;i<argc;i++){
		fichero=fopen(argv[i],"r");
		if(fichero==NULL){
			fprintf(stdout,"UVacat: no puedo abrir fichero\n");
			return 1;
		}
		d=fgetc(fichero);
      		while(d!=EOF){
			fprintf(stdout,"%c",d);
			d=fgetc(fichero);
		}
		fclose(fichero);
	}
	return 0;
}
