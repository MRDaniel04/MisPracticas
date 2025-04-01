import java.util.Scanner;

public class H6E3.java{
	public static int veces(String texto,String busq){
		int cont=0;
		for(int i=0;i<texto.length();i++){
			if(texto.indexOf(busq,i)!=-1){
				cont++;
				i=texto.indexOf(busq,i);
			}
		}
		return cont;
	}
}
