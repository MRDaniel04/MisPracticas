/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ssw.entrega.modelo;
import java.io.Serializable;
import java.math.BigDecimal;


public class OpcionApuesta implements Serializable {

    private String id;         
    private String apuestaId;   
    private String texto;       
    private BigDecimal cuota;   
    private int votos;         

    public OpcionApuesta() { }

    public OpcionApuesta(String id, String apuestaId,
                  String texto, BigDecimal cuota) {
        this.id = id;
        this.apuestaId = apuestaId;
        this.texto = texto;
        this.cuota = cuota;
        this.votos = 0;
    }

    public OpcionApuesta(String id, String apuestaId, String texto,
                  BigDecimal cuota, int votos) {
        this.id = id;
        this.apuestaId = apuestaId;
        this.texto = texto;
        this.cuota = cuota;
        this.votos = votos;
    }

    public String getId()                 { return id; }
    public void   setId(String id)        { this.id = id; }

    public String getApuestaId()                { return apuestaId; }
    public void   setApuestaId(String apuestaId){ this.apuestaId = apuestaId; }

    public String getTexto()              { return texto; }
    public void   setTexto(String texto)  { this.texto = texto; }

    public BigDecimal getCuota()                 { return cuota; }
    public void       setCuota(BigDecimal cuota) { this.cuota = cuota; }

    public int  getVotos()         { return votos; }
    public void setVotos(int votos){ this.votos = votos; }
    
    @Override
    public String toString() {
        return "Opcion{" +
               "id='" + id + '\'' +
               ", apuestaId='" + apuestaId + '\'' +
               ", texto='" + texto + '\'' +
               ", cuota=" + cuota +
               ", votos=" + votos +
               '}';
    }
}

