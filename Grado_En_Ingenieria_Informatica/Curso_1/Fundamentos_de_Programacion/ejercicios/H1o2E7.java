import java.util.Scanner;

public class H1o2E7 {
	public static void main(String args[]){
		Scanner in = new Scanner(System.in);
		double a, b, c;
		System.out.println("Escriba un numero:");
				a = in.nextDouble();

		System.out.println("Escriba otro numero:");
				b = in.nextDouble();

		System.out.println("Escriba otro numero:");
				c = in.nextDouble();

		System.out.println("Si hacemos a*b/c, saldria: " + a*b/c);
		System.out.println("Si hacemos a*(b/c), saldria: " + a*(b/c));
		System.out.println("Luego podemos comprobar que las dos operaciones son equivalentes")

	}
}