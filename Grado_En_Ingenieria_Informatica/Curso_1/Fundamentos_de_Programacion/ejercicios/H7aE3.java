public class H7aE3{
	public static void main(String args[]){
		final int TAM = 10;
		int j, k;
		double m;
		double [ ] a = new double [TAM];
		for (int i = 0; i<a.length ; i++){
			a[i]= (double)Math.random()*10;
		}
		k = (int)(Math.random()*TAM);
		m = a[k];
		for (int i = 0 ; i <TAM ; i++){
			a[i] = a[i]/m;
			System.out.println(a[i]);
		}
	}
}
