import java.util.Scanner;

public class H1o2E3{
	public static void main(String args[]){
		Scanner in = new Scanner(System.in);
		int valor, unidades, decenas, centenas;
		System.out.println("Escriba un  numero de 3 cifras");
		valor = in.nextInt();
		unidades = valor%10;
		valor = valor/10;
		decenas = valor%10;
		centenas = valor/10;
		System.out.println (unidades + "" + decenas + "" + centenas);
	}
}