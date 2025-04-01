import java.util.Scanner;

public class H3E9{
	public static void main(String args[]){
	Scanner in = new Scanner(System.in);
	int dia, mes, año, opcion;
	System.out.println("Escriba el dia");
	dia=in.nextInt();
	System.out.println("Escriba el mes");
	mes=in.nextInt();
	System.out.println("Escriba el año");
	año=in.nextInt();
	System.out.println("Elija la opcion D/M/A(1) o M/D/A(2)");
	opcion = in.nextInt();
	if(opcion==1){
		System.out.println(dia + "/"+ mes + "/" + año);
	}
	else
		System.out.println(mes + "/" + dia + "/" +año);
	}
}