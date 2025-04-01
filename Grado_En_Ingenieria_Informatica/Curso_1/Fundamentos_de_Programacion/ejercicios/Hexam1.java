import java.util.Scanner;

public class Hexam1{
	public static void main(String args[]){
	Scanner in = new Scanner(System.in);
	int horas, minutos, segundos, segundostot;
	System.out.println("Escriba las horas");
	horas = in.nextInt();
	System.out.println("Escriba los minutos");
	minutos = in.nextInt();
	System.out.println("Escriba los segundos");
	segundos = in.nextInt();
	horas = horas * 3600;
	minutos = minutos * 60;
	segundostot = horas + minutos + segundos;
	System.out.println("El tiempo en segundos sera: " + segundostot + " segundos" );
	}
}