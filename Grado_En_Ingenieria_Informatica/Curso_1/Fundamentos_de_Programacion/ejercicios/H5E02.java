package net.jorgesanchez.z08;
import java.util.Scanner;

public class H5E02 {
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        int nFilas, cont=1;

        System.out.println("Escriba el nº de filas");
        nFilas=in.nextInt();

        for(int i=1;i<=nFilas;i++){
            for(int j=1; j<=i; j++){
                System.out.print(cont+" ");
                cont++;
            }
            System.out.println();
        }


    }
}
