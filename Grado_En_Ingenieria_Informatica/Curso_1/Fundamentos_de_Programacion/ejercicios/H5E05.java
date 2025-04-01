package net.jorgesanchez.z08;

public class H5E05 {
    /**
     * Calcula el máximo común divisor de dos números
     * @param n1 int Primer número
     * @param n2 int Segundo número
     * @return Entero con mayor divisor común
     */
    public static int mcd(int n1, int n2){
        int menor,mayor, divisor;
        if(n1<=0 || n2<=0) return 0;

        //Recolocar el mayor y el menor de los dos números
        if(n1 < n2){
            menor = n1;
            mayor = n2;
        }
        else{
            menor = n2;
            mayor = n1;
        }

        divisor=menor; //comenzamos a comprobar desde el número menor

        //Mientras el divisor no lo sea de ambos números, descendemos
        //al final, puesto ue ambos son positivos, encontraremos seguro
        // un divisor, al menos lo será el número 1
        while(menor % divisor != 0 || mayor % divisor != 0){
            divisor--;
        }
        return divisor;
    }

    /**
     * Indica si dos números son coPrimos
     * @param n1 int Primer número, debe ser mayor que cero
     * @param n2 int Segundo número, debe ser mayor que cero
     * @return Verdadero si ambos son coprimos
     */
    public static boolean esCoprimo(int n1, int n2){
        if( n1 <= 0 || n2 <= 0) return false;
        else return mcd(n1,n2)==1;
    }

    /**
     * Devuelve el número de coprimos por debajo de un número
     * @param n int Número entero positivo del que se desea saber el nº de Euler
     * @return Entero con el número de coprimos
     */
    public static int euler(int n){
        int divisor = n-1, cont = 0;

        while(divisor > 0){
            if(esCoprimo(n,divisor)) cont++;
            divisor--;
        }
        return cont;
    }

    public static void main(String[] args) {
        System.out.println(euler(8));
    }
}
