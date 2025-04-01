import java.util.Scanner;

public class H4E5{
	public static void main(String args[]){
		Scanner in = new Scanner (System.in);
		int n;
		System.out.println("Escriba su número");
		n=in.nextInt();
		do{
			if (n%2==0){
				n = n/2;
				System.out.print(n + " , ");
			}
			else{
			n = (n*3)+1;
			System.out.print(n + " , ");
			}
		}
		while(n!=1);
	}
}