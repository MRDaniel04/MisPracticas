public class H1E1{
	public static void main(String args[]){
		final int SEG_INIC=75396;
		int segundos = SEG_INIC % 60;
		int minutos = SEG_INIC / 60;
		int horas = minutos/60;
		minutos = minutos % 60;
		System.out.println (SEG_INIC + " segundos serian " + horas + " horas, " + minutos + " minutos y " + segundos + " segundos.");

	}
}