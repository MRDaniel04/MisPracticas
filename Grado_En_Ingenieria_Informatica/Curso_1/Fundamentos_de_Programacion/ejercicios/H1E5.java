public class H1E5 {
	public static void main(String args []){
		int grados1, minutos1, segundos1, grados2, minutos2, segundos2, gradosfin, minutosfin, segundosfin;

		grados1=134;
		minutos1=27;
		segundos1=45;
		grados2=345;
		minutos2=52;
		segundos2=21;
		
		segundosfin=(segundos1+segundos2)%60;
		minutosfin=(minutos1+minutos2+((segundos1+segundos2)/60))%60;
		gradosfin=(grados1+grados2+((minutos1+minutos2)/60))%360;

		System.out.println(gradosfin + " grados " + minutosfin + " minutos "  + segundosfin + " segundos.");

	}
}
