import java.util.Scanner;

public class H4E1a{
	public static void main(String args[]){
		int numero;
		Scanner in = new Scanner(System.in);
		System.out.println("Escriba el número");
		numero = in.nextInt();
		if(numero<0){
			System.out.println("");
				}
		else
		System.out.println(Math.sqrt((double)numero));
	}
}