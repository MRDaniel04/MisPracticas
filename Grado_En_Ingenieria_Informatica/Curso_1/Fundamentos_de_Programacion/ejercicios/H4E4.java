import java.util.Scanner;

public class H4E4{
	public static void main(String args[]){
		Scanner in = new Scanner(System.in);
		int numero, contador=0, acumulador=0, max, min;
		System.out.println("Escriba el primer número");
		numero=in.nextInt();
		max = numero;
		min = numero;
		while(numero!=0){
			acumulador = acumulador + numero;
			contador++;
			if(numero>max){
				max=numero;
			}
			if(numero<min){
				min=numero;
			}
			System.out.println("Escriba el siguiente número");
			numero=in.nextInt();
		}
		System.out.println("La suma es: " + acumulador);
		System.out.println("El número de números leidos es: " + contador);
		System.out.println("La media es: " + ((double)acumulador)/contador);
		System.out.println("El mayor número es: " + max);
		System.out.println("El menor número es: " + min);
	}
}