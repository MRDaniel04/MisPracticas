/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ssw.entrega.modelo;

public class Blog {
    private int id;
    // Se pueden agregar más atributos si se definen en el futuro

    public Blog() { }

    public Blog(int id) {
        this.id = id;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }
  
    public void setId(int id) {
        this.id = id;
    }
}
