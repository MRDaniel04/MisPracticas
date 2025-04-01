import java.util.Scanner;

public class H3E1{
	public static void main(String args[]){
	Scanner in = new Scanner(System.in);
	double numero, raiz2, raiz3, raiz4, raiz5;
	System.out.println("Escriba su número");
	numero = in.nextDouble();
	raiz2 = Math.sqrt(numero);
	raiz3 = Math.pow(numero, (double)1/3);
	raiz4 = Math.pow(numero, (double)1/4);
	raiz5 = Math.pow(numero, (double)1/5);
	System.out.println("Raiz cuadrada: " + raiz2);
	System.out.println("Raiz cubica: " + raiz3);
	System.out.println("Raiz cuarta: " + raiz4);
	System.out.println("Raiz quinta: " + raiz5);
	}
}