import java.util.Scanner;

public class H6E1{
	public static void main(String args []){
		Scanner in = new Scanner(System.in);
		String frase,x1;
		char x2;
		int a=0;
		System.out.println("Escriba la frase");
		frase = in.nextLine();
		System.out.println("Digame el carácter");
		x1 = in.next();
		x2 = x1.charAt(0);
		for(int i=0;i<frase.length();i++){
			if (frase.charAt(i)==x2)
				a++;
		}
		System.out.println("El carácter " + x2 + " se repite " + a + " veces.");
	}
}