import java.util.Scanner;

public class H4E1c{
	public static void main(String args[]){
		int numero;
		Scanner in = new Scanner(System.in);
		do {
			System.out.println("Escriba el número");
		numero = in.nextInt();
	}
		while(numero<0);
		System.out.println(Math.sqrt((double)numero));
		
	}
}