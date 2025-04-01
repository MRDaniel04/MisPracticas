public class H1E4 {
	public static void main(String args[]){ //No tiene sentido. REVISAR!! 3 ERRORES
		int valor = 7532;
		int billetes500 = valor/500;
		valor = valor%500;
		int billetes200 = valor/200;
		valor = valor%200;
		int billetes100 = valor/100;
		valor = valor%100;
		int billetes50 = valor/50;
		valor = valor%50;
		int billetes20 = valor/20;
		valor = valor%20;
		int billetes10 = valor/10;
		valor = valor%10;
		int billetes5 = valor/5;
		valor = valor%5;
		int monedas2 = valor/2;
		valor = valor%2;
		int monedas1 = valor/1;
		valor = valor%1;
		int monedas05 = valor/0.5;
		valor = valor%0.5;
		int monedas02 = valor/0.2;
		valor = valor%0.2;
		int monedas01 = valor/0.1;
		valor = valor%0.1;
		int monedas005 = valor/0.05;
		valor = valor%0.05;
		int monedas002 = valor/0.02;
		valor = valor%0.02;
		int monedas001 = valor/0.01;
		valor = valor%0.01;

		System.out.println( "Billetes: " + billetes500 + "de 500, " + billetes200 + " de 200, " + billetes100 + "de 100, " 
			+ billetes50 + "de 50, " + billetes20 + "de 20, " + billetes10 + "de 10, " + billetes5 + "de 5.");

		System.out.println( "Monedas: " + monedas2 + "de 2, " +monedas1 + "de 1, " + monedas05 + "de 0.5, " + monedas02 "de 0.2, " 
			+ monedas01 + "de 0.1, " + monedas005 + "de 0.05, " + monedas002 + "de 0.02, " + monedas001 + "de 0.01.");
	}
}