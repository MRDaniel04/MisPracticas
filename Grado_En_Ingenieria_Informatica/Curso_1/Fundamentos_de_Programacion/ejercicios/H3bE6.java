import java.util.Scanner;

public class H3bE6{
	public static void Igualdad(double num1, double num2, double num3){
		if (num1==num2&& num2==num3){
			System.out.println("Los 3 números son iguales.");
		}
		else{
			if (num1==num2){
				System.out.println(num1 + " y " + num2 + " son iguales.");
			}
			if (num2==num3){
				System.out.println(num2 + " y " + num3 + " son iguales.");
			}
			if (num1==num3){
				System.out.println(num1 + " y " + num3 + " son iguales.");
			}
			else {
				System.out.println("Todos los números son distintos");
			}
			}
		}
		public static void main(String args[]){
			double num1, num2, num3;
			Scanner in = new Scanner (System.in);
			num1 = in.nextDouble();
			num2 = in.nextDouble();
			num3 = in.nextDouble();
			Igualdad(num1,num2,num3);
		}
	}