package net.jorgesanchez.z08;
import java.util.Scanner;

public class H5E03 {
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        int ultimo, cont=1;
        int i;//controla el número de fila

        System.out.println("Escriba el último número que desea escribir");
        ultimo=in.nextInt();

        i=1;
        while(cont<=ultimo){
            for(int j=1; cont<=ultimo && j<=i; j++){
                System.out.print(cont+" ");
                cont++;
            }
            System.out.println();
            i++;
        }
    }
}
