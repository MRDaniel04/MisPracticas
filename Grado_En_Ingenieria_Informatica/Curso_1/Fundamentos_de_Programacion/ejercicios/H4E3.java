import java.util.Scanner;

public class H4E3{
	public static void main(String args[]){
		Scanner in = new Scanner(System.in);
		int i, acumulador=0,numero;
		System.out.println("Escriba la primera nota");
		numero = in.nextInt();
		acumulador = acumulador+numero;
	for(i=1;i<=11;i++){
		System.out.println("Escriba la siguiente nota");
		numero = in.nextInt();
		acumulador = acumulador+numero;
		}
	System.out.println("La media es: " + acumulador/12);
	}
}