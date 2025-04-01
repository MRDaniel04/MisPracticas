import java.util.Scanner;

public class H3bE4{
	public static void main(String args[]){
	double num1,num2,num3,num4,num5,media,n;
	Scanner in = new Scanner(System.in);
	System.out.println("Escriba el primer numero");
	num1=in.nextDouble();
	System.out.println("Escriba el segundo numero");
	num2=in.nextDouble();
	System.out.println("Escriba el tercer numero");
	num3=in.nextDouble();
	System.out.println("Escriba el cuarto numero");
	num4=in.nextDouble();
	System.out.println("Escriba el quinto numero");
	num5=in.nextDouble();
	n = media(num1,num2,num3,num4,num5);
	System.out.println(n);

}
public static double media(double num1,double num2,double num3,double num4,double num5){
	int i= 0, suma=0,media;
	if(num1%2!=0){
		suma+=num1;
		i++;
	}

	if(num2%2!=0){
		suma+=num2;
		i++;
	}

	if(num3%2!=0){
		suma+=num3;
		i++;
	}

	if(num4%2!=0){
		suma+=num4;
		i++;
	}

	if(num5%2!=0){
		suma+=num5;
		i++;
	}
	media = suma/i;
	return(media);
	}
}
