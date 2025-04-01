import java.util.Scanner;

public class H3bE3{
	public static void Sum (int num1, int num2, int num3){
		int num;
		if(num1<num2)
			if(num1<num3)
				num = num1;
			else
				num = num3;
		else 
			if (num2<num3)
				num = num2;
			else
				num = num3;
		System.out.println("El mas pequeño es:" + num);
	}
	public static void main(String args[]){
	int num1,num2,num3;
	Scanner in = new Scanner(System.in);
	System.out.println("Escriba su primer numero");
	num1 = in.nextInt();
	System.out.println("Escriba su segundo numero");
	num2 = in.nextInt();
	System.out.println("Escriba su tercer numero");
	num3 = in.nextInt();
	Sum(num1,num2,num3);
	}
	}