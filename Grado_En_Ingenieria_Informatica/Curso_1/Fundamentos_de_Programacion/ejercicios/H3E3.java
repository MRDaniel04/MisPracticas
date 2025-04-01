import java.util.Locale;
import java.util.Scanner;

public class H3E3 {
	public static void EscribirCartesianasDesdePolares(double radio, double angulo){
		double x,y;
		y = Math.sin(angulo)*radio;
		x = Math.cos(angulo)*radio;
		System.out.println(x+ "," + y);
	}
	public static void main (String[] args){
																				/* De polares a cartesianas, dim 2*/
	Scanner in = new Scanner (System.in);
	in.useLocale (Locale.US);
	double x, y; double angulo, radio;
	
	System.out.print ("Escriba un ángulo (en radianes): ");
	angulo = in.nextDouble();
	System.out.print ("y un radio (positivo): ");
	radio = in.nextDouble();
	System.out.println ("Ángulo " + angulo + " , radio " + radio);
	System.out.print ("El punto en cartesianas es ");
	EscribirCartesianasDesdePolares (radio, angulo); // SENTENCIA
	System.out.println();
	}
	}

	
