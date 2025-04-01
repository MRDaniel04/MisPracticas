public class H7aE1{
	public static double[] Mod1(double[] array1){
		double acum=0;
		for(int i=0;i<=array1.length-1;i++){
			acum = acum + array1[i];
		}
		for(int j=0;j<=array1.length-1;j++){
			array1[j] = array1[j]/acum;
		}
		return array1;
	}

	public static void main(String args[]){
		double [] array;
		array = new double[5];
		for (int k=0;k<=array.length-1;k++){
			array[k]= (double)(Math.random()*100);
		}
		Mod1(array);
		for (int k=0;k<=array.length-1;k++){
			System.out.println(array[k]);
		}

	}
}