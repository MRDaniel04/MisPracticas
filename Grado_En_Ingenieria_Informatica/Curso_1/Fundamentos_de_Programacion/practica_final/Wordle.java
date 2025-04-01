import java.util.Scanner;
import java.io.*;

public class WordlePrueba8{
	public static void main(String args[]){
		int nn = 5;
		Scanner in = new Scanner(System.in);
		int acum=0,a=0,w=0,acum2=0,acum3=1,opcion=0,ff, tot, ganadas_ord,ganadas_per,opcion2,cont=0;
		String n,palabra="XXXXX";
		char[]letras=new char[nn];
		String [] lista = new String[99];
		boolean centinela=false;
		Scanner ficheroscan;
		PrintWriter ficherowrite;
		boolean [] palabrasrestantes=new boolean[acum];
		String [] lista2=new String[acum];
		System.out.println("¿Desea resetear las puntuaciones?(1,si;0,no)");
		opcion2 = in.nextInt();
		while(opcion2 != 1 && opcion2 !=0){
			System.out.println("Por favor, ponga un 1 si desea resetearlas, un 0 si no");
			opcion2=in.nextInt();
		}
		if (opcion2==1){
			try{
			FileOutputStream fichero01 = new FileOutputStream("FicheroWordle01.txt");
			DataOutputStream out01 = new DataOutputStream(fichero01);
			tot=0;
			ganadas_ord=0;
			ganadas_per=0;
			out01.writeInt(tot);
			out01.writeInt(ganadas_ord);
			out01.writeInt(ganadas_per);

			out01.close();
			}
			catch ( FileNotFoundException e ) {
				System.out.println("ERROR al crear el fichero");
				return;
			}
			catch ( IOException e ) {
				System.out.println("ERROR al escribir en el fichero");
				return;
			}
		}
		do{
			try{
				ficheroscan = new Scanner(new File("FicheroWordle.txt"));
			}
			catch (IOException e) {
				System.out.println("No se encontró el fichero");
				System.out.println("Error:" + e);
				return;
			}
			ff=0;
			while(ficheroscan.hasNext()){
				lista[ff]=ficheroscan.next();
				ff++;
			}
			ficheroscan.close();
			while(acum<=lista.length && lista[acum]!=null){										
				acum++;
			}
			lista2 = new String[acum];
			palabrasrestantes = new boolean[acum];
			acum2=0;
			acum3=1;
			w=0;
			for(int i=0;i<acum;i++){
				lista2[i]=SegundaLista(lista,acum,lista2)[i];
			}
			for(int i=0;i<lista2.length;i++){
				palabrasrestantes[i]=Vectorbooleanos(palabrasrestantes,acum)[i];
			}
			a = Aleatorio2(a,acum);
			palabra = Palabra(a,lista2,palabra);
			System.out.println("Comencemos");
			in.nextLine();
			System.out.println(palabra);
			n=in.nextLine();
			cont=0;
			for(int i=0;i<n.length();i++){
				if(n.charAt(i)=='0'||n.charAt(i)=='1'||n.charAt(i)=='2'){
					cont++;
				}
			}
			while(n.length()!=nn && cont!=nn){
				System.out.println("Por favor una cadena de (0,1,2) y de 5 números");
				n=in.nextLine();
				cont=0;
				for(int i=0;i<n.length();i++){
					if(n.charAt(i)=='0'||n.charAt(i)=='1'||n.charAt(i)=='2'){
						cont++;
					}
				}
			}
			for(int i=0;i<letras.length;i++){
				letras[i]=VectorPalabra(palabra,letras,nn)[i];
			}
			for(int i=0;i<acum;i++){
				lista2[i]=Comprobacion(lista2,letras,n,acum,palabrasrestantes,nn)[i];					
			}
			while(w<lista2.length&&lista2[w]!=null){
				acum2++;
				w++;
			}
			acum=acum2;
			while(acum3<6 && n.equals("22222")==false){													
				if(Comprobartrue(palabrasrestantes,centinela)==true){
					a = Aleatorio1(a,acum,palabrasrestantes);
				}
				else{
					a = Aleatorio2(a,acum);
				}
				palabra=Palabra(a,lista2,palabra);
				System.out.println(palabra);
				n=in.nextLine();
				cont=0;
				for(int i=0;i<n.length();i++){
					if(n.charAt(i)=='0'||n.charAt(i)=='1'||n.charAt(i)=='2'){
						cont++;
					}
				}
				while(n.length()!=nn && cont!=nn){
					System.out.println("Por favor una cadena de (0,1,2) y de 5 números");
					n=in.nextLine();
					cont=0;
					for(int i=0;i<n.length();i++){
						if(n.charAt(i)=='0'||n.charAt(i)=='1'||n.charAt(i)=='2'){
							cont++;
						}
					}
				}
				for(int i=0;i<letras.length;i++){
					letras[i]=VectorPalabra(palabra,letras,nn)[i];
				}
				for(int i=0;i<acum;i++){
					lista2[i]=Comprobacion(lista2,letras,n,acum,palabrasrestantes,nn)[i];
				}
				acum2=0;
				w=0;
				while(w<lista2.length&&lista2[w]!=null){
					acum2++;
					w++;
				}
				acum=acum2;
				acum3++;
			}
			try{
				DataInputStream in01 = new DataInputStream (new FileInputStream("FicheroWordle01.txt"));
				tot = in01.readInt();
				ganadas_ord = in01.readInt();
				ganadas_per = in01.readInt();
				in01.close();
			}
			catch ( FileNotFoundException e) {
				System.out.println ("ERROR al abrir el fichero" + e);
				return;
			}
			catch ( IOException e) {
				System.out.println ("ERROR al leer del fichero" + e);
				return;
			}
			if(n.equals("22222")==true){
				ganadas_ord++;
				System.out.println("HE GANADO");
			}

			else if (acum3==6){
				ganadas_per++;
				System.out.println("HE PERDIDO!!!!");
				System.out.println("Puedes decirme la palabra que era para que la agregue al diccionario en caso de que no este?");
				palabra=in.nextLine();
				cont=0;
				for(int i=0;i<palabra.length();i++){
					if(palabra.charAt(i)>'A' && palabra.charAt(i)<'Z'){
						cont++;
					}
				}
				while(palabra.length()!=nn && cont!=nn){
					System.out.println("Por favor digame la palabra en mayusculas");
					n=in.nextLine();
					cont=0;
					for(int i=0;i<n.length();i++){
						if(palabra.charAt(i)>'A'&&palabra.charAt(i)<'Z'){
							cont++;
						}
					}
				}
				lista[acum]=palabra;
				try{
					ficherowrite = new PrintWriter("FicheroWordle.txt");
				}
				catch (FileNotFoundException e){
					System.out.println("No se pudo abrir el fichero");
					System.out.println("Error:" + e);
					return;
				}
				lista[acum]=palabra;
				for(int j=0; j<=acum; j++){
					ficherowrite.println(lista[j]);
				}
				ficherowrite.close();
				acum++;
			}
			tot++;
			System.out.println("RESULTADOS");
			System.out.println("PARTIDAS TOTALES:" + tot);
			System.out.println("PARTIDAS GANADAS POR ORDENADOR:" + ganadas_ord);
			System.out.println("PARTIDAS GANADAS POR PERSONA:" + ganadas_per);
			try{
				FileOutputStream fichero01 = new FileOutputStream("FicheroWordle01.txt");
				DataOutputStream out01 = new DataOutputStream(fichero01);
				out01.writeInt(tot);
				out01.writeInt(ganadas_ord);
				out01.writeInt(ganadas_per);
		
				out01.close();
			}
			catch ( FileNotFoundException e ) {
				System.out.println("ERROR al crear el fichero");
				return;
			}
			catch ( IOException e ) {
				System.out.println("ERROR al escribir en el fichero");
				return;
			}
			System.out.println("Desea seguir jugando??(1,Si;0,No)");
			opcion=in.nextInt();
			while(opcion!=1&&opcion!=0){
				System.out.println("Desea seguir jugando??(1,Si;0,No)");
				opcion=in.nextInt();
			}

		}while(opcion==1 && acum<=lista.length);

	}																								

	public static boolean[]Vectorbooleanos(boolean[] palabrasrestantes,int acum){					
		palabrasrestantes = new boolean[acum];
		for(int i=0;i<acum;i++){
			palabrasrestantes[i]=true;
		}
		return palabrasrestantes;
	}
	public static int Aleatorio1(int a,int acum,boolean[] palabrasrestantes){					
		do{
			a=(int)(Math.random()*acum);
		}while(palabrasrestantes[a]!=true);
		return a;
	}
	public static int Aleatorio2(int a,int acum){
		a=(int)(Math.random()*acum);
		return a;
	}
	public static boolean Comprobartrue(boolean[] palabrasrestantes,boolean centinela){
		centinela=false;
		for(int i=0;i<palabrasrestantes.length;i++){
			if(palabrasrestantes[i]==true){
				centinela=true;
			}
		}
		return centinela;
	}
	public static String Palabra(int a,String[]lista2,String palabra){			
		palabra=lista2[a];
		return palabra;
	}
	public static String[] SegundaLista(String[]lista,int acum,String[]lista2){				
		lista2 = new String [acum];
		for(int i=0;i<acum;i++){
			lista2[i]=lista[i];
		}
		return lista2;
	}
	public static char[] VectorPalabra(String palabra, char [] letras,int nn){						
		for(int i=0;i<nn;i++){
			letras[i]=palabra.charAt(i);
		}
		return letras;
	}
	public static boolean[] CambioBooleanArray(boolean[] palabrasrestantes, int j){					
		palabrasrestantes[j]=false;
		return palabrasrestantes;
	}
	
	public static String[] Comprobacion(String[]lista2, char[]letras,String n,int acum,boolean[]palabrasrestantes,int nn){
		boolean centinela=false;
		int kk=0;
		for(int i=0;i<nn;i++){
			if(n.charAt(i)=='0'){
				Comprobación0(palabrasrestantes,nn,i,letras,lista2);
			}
			if(n.charAt(i)=='1'){
				Comprobación1(palabrasrestantes,nn,i,letras,lista2,centinela,kk);
			}
			if(n.charAt(i)=='2'){
				Comprobación2(palabrasrestantes,nn,i,letras,lista2);
			}
		}
		return lista2;
	}
	public static String[] Comprobación0(boolean [] palabrasrestantes,int nn,int i,char [] letras,String[]lista2){
		for(int j=0;j<lista2.length;j++){
			if(palabrasrestantes[j]==true){
				for(int k=0;k<nn;k++){
					if (lista2[j].charAt(k)==letras[i]){
						CambioBooleanArray(palabrasrestantes,j);
					}
				}
			}
		}
		return lista2;
	}
	public static String[] Comprobación1(boolean [] palabrasrestantes,int nn,int i,char [] letras,String[]lista2,boolean centinela,int kk){
		for(int j=0;j<lista2.length;j++){
			if(palabrasrestantes[j]==true){
				if (lista2[j].charAt(i) != letras[i]){
					kk=0;
					centinela=false;
					do{
						for(int k=0;k<nn;k++){
							if (lista2[j].charAt(k) == letras[i]){
								centinela=true;
							}
							kk++;
						}
					}while(centinela==false && kk<nn);
					if(centinela==false){
						CambioBooleanArray(palabrasrestantes,j);
					}
				}
				if (lista2[j].charAt(i) == letras[i]){
					CambioBooleanArray(palabrasrestantes,j);
				}
			}
		}
		return lista2;
	}
	public static String[] Comprobación2(boolean [] palabrasrestantes,int nn, int i, char [] letras,String[]lista2){
		for(int j=0;j<lista2.length;j++){
			if(palabrasrestantes[j]==true){
				if(lista2[j].charAt(i)!=letras[i]){
					CambioBooleanArray(palabrasrestantes,j);
				}
			}
		}
		return lista2;
	}
}