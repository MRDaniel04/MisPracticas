import java.util.Scanner;

public class H4E2{
	public static void main (String args[]){
		Scanner in = new Scanner(System.in);
		int numero, opcion, max=0, min=10;

		do{
			System.out.println("Escriba la nota");
			numero=in.nextInt();
			if(numero>max){
				max=numero;
			}
			if(numero<min){
				min=numero;
			}
			

			System.out.println("¿Desea introducir mas notas?(Escribir 1 si es que si)");
			opcion = in.nextInt();
		}while(opcion == 1);

		System.out.println("La nota más grande es: " + max);
		System.out.println("La nota más pequeña es: " + min);
	}
}