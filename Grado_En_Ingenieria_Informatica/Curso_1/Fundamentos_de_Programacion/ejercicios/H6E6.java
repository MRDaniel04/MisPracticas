import java.util.Scanner;

public class H6E6{
	public static int Cadena(String cad){
		int n,acum=0;
		for(int i=0;i<=cad.length()-1;i++){
			n = (int)cad.charAt(i);
			n = n - (int)'0';
			acum = acum*10+n;
		}
		return acum;
	}

	public static void main(String args[]){
		Scanner in = new Scanner(System.in);
		String cad;
		System.out.println("Escriba la cadena");
		cad = in.nextLine();
		System.out.println("El número escrito es: " + Cadena(cad));
	}
}