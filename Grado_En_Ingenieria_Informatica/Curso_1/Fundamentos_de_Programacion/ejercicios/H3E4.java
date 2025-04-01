import java.util.Scanner;
import java.util.Locale;

public class H3E4 {
	public static double rad(double x,double y){
		double radio;
		radio = Math.sqrt(Math.pow(x,2))+(Math.pow(y,2));
		return radio;
	}
	public static double ang(double x,double y){
		double angulo;
		angulo = Math.atan(x/y);
		return angulo;
	}

	public static void main (String[] args){
																						/* De cartesianas a polares, dim 2 */
	Scanner in = new Scanner (System.in);
	in.useLocale(Locale.US);
	double x, y;

		System.out.print ("Escriba las 2 coordenadas de un punto: ");
		x = in.nextDouble(); y = in.nextDouble();
		System.out.println ("Punto ("+ x + ", " + y + ")");
		System.out.print ("En polares es: ");
		System.out.println ( "Radio= " + rad(x,y) +", ángulo = " + ang (x,y));			// EXPRESIONES
		}
	}