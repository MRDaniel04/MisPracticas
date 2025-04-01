import java.util.Scanner;

public class H1o2E9{
	public static void main(String args[]){
	Scanner in = new Scanner(System.in);
	int grados, cociente, angulo;
	System.out.println("Escriba los grados");
		grados = in.nextInt();
		angulo = (int)grados%360;
		cociente = (int)angulo/90;
		cociente = cociente + 1;
		System.out.println("Su ángulo está en el cuadrante número " + cociente);
	}
}