import java.util.Scanner;

public class H1o2E10 {
	public static void main(String args[]){
		Scanner in = new Scanner(System.in);
		double grados, radianes;
		System.out.println("Escribe el angulo");
		grados = in.nextDouble();
		radianes = grados/180;
		System.out.println("Su angulo son: " + radianes + "π radianes.");
	}
}