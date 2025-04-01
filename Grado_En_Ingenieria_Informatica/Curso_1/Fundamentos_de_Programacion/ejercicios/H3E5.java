import java.util.Scanner;

	public class H3E5{

		public static void Radianes(double grado1){
			double radian1;
				radian1 = grado1/180;
				System.out.println("Serian: " + radian1 + "π radianes");
			}

		public static void Grados(double radian2){
			double grado2;
			grado2 = radian2*180/3.1416;
			System.out.println("Serian: " + grado2 + " grados");
		}

		public static void main(String args[]){
		Scanner in = new Scanner(System.in);
		double grado1, radian2;
		System.out.println("Escriba los grados:");
		grado1 = in.nextDouble();
		Radianes(grado1);
		System.out.println("Escriba los radianes");
		radian2 = in.nextDouble();
		Grados(radian2);
	}
}
		
