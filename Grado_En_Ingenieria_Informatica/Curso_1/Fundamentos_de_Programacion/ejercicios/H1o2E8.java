import java.util.Scanner;

public class H1o2E8 {
	public static void main(String args[]){
		Scanner in = new Scanner(System.in);
		int grado, vueltas;
		System.out.println("¿De que angulo quiere saber cuantas vueltas tiene?");
		grado = in.nextInt();
		vueltas = grado/360;
		System.out.println("Su ángulo da: " + vueltas + " vueltas");

	}
}