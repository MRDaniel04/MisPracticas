package net.jorgesanchez.z08;
import java.util.Scanner;

public class H5E04 {
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        int nFilas;
        int contEspacios;
        int cont;

        System.out.println("Escribe cuantas filas quieres");
        nFilas=in.nextInt();
        contEspacios=nFilas-1;
        for(int i=1;i<=nFilas;i++){
            //bucle de espacios
            for(int j = 1; j <= contEspacios; j++){
                System.out.print(" ");
            }
            // bucle ascendente
            cont = i;
            for(int j = 1; j <= i; j++){
                System.out.print(cont % 10);
                cont++;
            }
            // bucle descendente
            cont -= 2;
            for(int j = 1; j <= i-1; j++){
                System.out.print(cont % 10);
                cont--;
            }
            System.out.println();
            contEspacios--;
        }

    }
}
