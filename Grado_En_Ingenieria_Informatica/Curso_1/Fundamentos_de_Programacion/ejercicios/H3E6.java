import java.util.Scanner;

public class H3E6{
	public static void DiaMesAño(int dia,int mes,int año){
		System.out.println(dia + "/" + mes + "/" + año);
	}

	public static void MesAñoDia(int dia, int mes, int año){
		System.out.println(mes + "/" + año + "/" + dia);
	}
	public static void main(String args[]){
	Scanner in = new Scanner(System.in);
	int dia, mes, año;
	System.out.println("Escriba los dias");
	dia = in.nextInt();
	System.out.println("Escriba los meses");
	mes = in.nextInt();
	System.out.println("Escriba los años");
	año = in.nextInt();
	DiaMesAño(dia,mes,año);
	MesAñoDia(dia,mes,año);
	}
}