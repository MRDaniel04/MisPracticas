import java.util.Scanner;												/*Momento funciones*/			

public class H4E6{
	public static int Divisor(int n,int  cont){
			while(n%cont!=0)
				cont++;
				return cont;
	}

	public static void main(String args[]){
		Scanner in = new Scanner(System.in);
		int n, cont;
		System.out.println("Escriba el número");
		n=in.nextInt();
		cont = 2;
		System.out.println(Divisor(n,cont));
	}

}															