import java.util.Scanner;

public class H1o2E6{
	public static void main(String args[]){
		int numero1, numero2, resto, cociente, resultado1, resultado2, resultadofinal;
		System.out.println("Escriba el primer numero de 3 cifras");
		Scanner in = new Scanner(System.in);
		numero1 = in.nextInt();
		System.out.println("Escriba el segundo número de 2 cifras");
		Scanner inp = new Scanner(System.in);
		numero2 = inp.nextInt();
		resto = numero2%10;
		cociente = numero2/10*10;
		resultado1 = numero1*cociente;
		resultado2 = numero1 * resto;
		resultadofinal = resultado1+resultado2;
		System.out.println(resultado1);
		System.out.println(resultado2);
		System.out.println(resultadofinal);
	}
}