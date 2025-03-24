import java.util.Scanner;

public class WordleValidacion{
	public static void main(String args[]){
		Scanner in = new Scanner(System.in);
		String n;
		char[]letras=new char[5];
		n = in.nextLine();
		String palabra;
		letras=VectorPalabra(palabra);
	}





	public static char[] VectorPalabra(String palabra){						/*Descompone la palabra en cada uno de sus vectores*/
		char [] letras=new char[5];
		for(int j=0;j<5;j++){
			letras[j]=palabra.charAt(j);
		}
		return letras;
	}
}