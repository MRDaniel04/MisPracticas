import java.util.Scanner;

public class H3E11{
	public static void Corregir (int lado1, int lado2, int lado3){
		if(lado1+lado2<lado3)
			System.out.println("False");
		else
			if(lado2+lado3<lado1)
				System.out.println("False");
			else
				if(lado1+lado3<lado1)
					System.out.println("False");
				else
					System.out.println("True");
	}
	public static void main(String args[]){
	int lado1,lado2,lado3;
	Scanner in = new Scanner(System.in);
	System.out.println("Escriba su primer lado");
	lado1 = in.nextInt();
	System.out.println("Escriba su segundo lado");
	lado2 = in.nextInt();
	System.out.println("Escriba su tercer lado");
	lado3 = in.nextInt();
	Corregir(lado1,lado2,lado3);
	}
	}