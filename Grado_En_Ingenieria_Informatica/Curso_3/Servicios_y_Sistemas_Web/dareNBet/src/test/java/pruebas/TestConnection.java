/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebas;

import java.sql.Connection;
import uva.ssw.entrega.bd.ConnectionPool;
public class TestConnection {

    public static void main(String[] args) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = null;
        try {
            con = pool.getConnection();
            if (con != null) {
                System.out.println("¡Conexión exitosa a la base de datos!");
            } else {
                System.out.println("La conexión es nula. Revisa la configuración.");
            }
        } catch (Exception e) {
            System.out.println("Error al obtener la conexión:");
            e.printStackTrace();
        } finally {
            if (con != null) {
                pool.freeConnection(con);
            }
        }
    }
}
