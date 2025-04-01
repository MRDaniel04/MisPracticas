import java.util.Scanner;

public class H4E1d{
	public static void main(String args[]){
		int numero;
		Scanner in = new Scanner(System.in);
		System.out.println("Escriba el número");
		numero = in.nextInt();
		if(numero<0){
			while(numero<0){
				System.out.println("El numero debe ser un entero positivo o nulo");
				numero=in.nextInt();
				}
			System.out.println(Math.sqrt((double)numero));
			}
		else
		System.out.println(Math.sqrt((double)numero));
	}
}