import java.util.Scanner;
									/*Terminar*/
public class H3E12{
	public static void main(String args[]){
		double s,area;
		double lado1,lado2,lado3;
		Scanner in = new Scanner(System.in);
		System.out.println("Escriba su primer lado");
		lado1 = in.nextInt();
		System.out.println("Escriba su segundo lado");
		lado2 = in.nextInt();
		System.out.println("Escriba su tercer lado");
		lado3 = in.nextInt();
		if(lado1+lado2<lado3)
			System.out.println("0");
		else
			if(lado2+lado3<lado1)
				System.out.println("0");
			else
				if(lado1+lado3<lado1)
					System.out.println("0");
				else{
					s = (lado1+lado2+lado3)/2;
					area = Math.sqrt(s*(s-lado1)*(s-lado2)*(s-lado3));
					System.out.println(area);
				}
			}
		}