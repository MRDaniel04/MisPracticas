import java.util.Scanner;
										/*Terminar*/											

public class H3E7{
	public static void Fecha(String dia1, int dia2, int mes, int año){
		System.out.println(dia1 + ", "+ dia2 + " del " + mes + " de " + año);
	}

public static void main(String args[]){
	Scanner in = new Scanner(System.in);
	int dia2, mes, año;
	String dia1;
	System.out.println("Escriba el dia(numero):");
	dia2 = in.nextInt();
	System.out.println("Escriba el mes:");
	mes = in.nextInt();
	System.out.println("Escriba el año:");
	año = in.nextInt();
	System.out.println("Escriba el dia(nombre):");
	dia1 = in.next();
	Fecha(dia1,dia2,mes,año);
	}
}