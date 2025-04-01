import java.util.Scanner;

public class H4E1b{
	public static void main(String args[]){
		int numero;
		Scanner in = new Scanner(System.in);
		System.out.println("Escriba el número");
		numero = in.nextInt();
		if(numero<0){
			System.out.println("La entrada no ha sido la adecuada");
				}
		else
		System.out.println(Math.sqrt((double)numero));
	}
}