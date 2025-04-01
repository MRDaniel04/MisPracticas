import java.util.Scanner; //Sentencias condicionales??

public class H1o2E9if {
	public static void main(String args[]){
		Scanner in = new Scanner(System.in);
		int grados, cociente;

		System.out.println("Escriba los grados");
		grados = in.nextInt();
		cociente = (int)grados/90;
		if(cociente==0){
			System.out.println("1");
		}
		if(cociente==1){
			System.out.println("2");
		}
		if(cociente==2){
			System.out.println("3");
		}
		if(cociente==3){
			System.out.println("4");
		}

	}
}
