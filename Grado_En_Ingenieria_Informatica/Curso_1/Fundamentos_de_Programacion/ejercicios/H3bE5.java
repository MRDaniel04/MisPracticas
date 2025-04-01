import java.util.Scanner;

public class H3bE5{
	public static void main(String args[]){
		double distancia,dias,precio,n;
		Scanner in = new Scanner(System.in);
		System.out.println("Escriba la distancia que desea volar");
		distancia = in.nextDouble();
		System.out.println("Escriba los dias que dure su estancia");
		dias = in.nextDouble();
		System.out.println("Escriba el precio del billete en euros por kilometro");
		precio = in.nextDouble();
		n = precio(distancia,dias,precio);
		System.out.println(n);
		}

	public static double precio(double distancia, double dias, double precio){
		double preciobillete;
		preciobillete = distancia*precio;
		if (distancia>=1000.0){
			preciobillete = preciobillete * 0.7;
		}
		else 
			if (dias>=7){
				preciobillete = preciobillete * 0.7;
			}
		return(preciobillete);
	}

}