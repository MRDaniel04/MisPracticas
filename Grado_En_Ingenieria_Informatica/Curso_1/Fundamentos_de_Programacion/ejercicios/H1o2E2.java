import java.util.Scanner;

public class H1o2E2 {
	public static void main(String args[]){
		Scanner in = new Scanner(System.in);
		double deposit, interes;
		System.out.println("Escriba la suma de dinero");
		deposit = in.nextDouble();
		interes = deposit*0.045;
		System.out.println (deposit + interes);
	}
}