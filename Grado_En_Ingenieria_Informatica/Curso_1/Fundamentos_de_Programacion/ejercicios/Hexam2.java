import java.util.Scanner;

public class Hexam2{
	public static void main(String args[]){
	Scanner in = new Scanner(System.in);
	int numero1, multiplicador, numero2;
	System.out.println("Escriba su numero");
	numero1 = in.nextInt();
	multiplicador = numero1%10;
	numero2 = numero1 * multiplicador;
	System.out.println("Su nuevo número es: " + numero2);
	}
}