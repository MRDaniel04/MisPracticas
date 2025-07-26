/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ssw.entrega.modelo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author fredi
 */
public class UsuarioApuesta {
    private String id = UUID.randomUUID().toString();

    private Usuario usuario;

    private Apuesta apuesta;

    private OpcionApuesta opcion;

    private BigDecimal importe;

    private Date fechaApuesta;

    public UsuarioApuesta() {}

    // Getters y Setters

    public String getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Apuesta getApuesta() {
        return apuesta;
    }

    public void setApuesta(Apuesta apuesta) {
        this.apuesta = apuesta;
    }

    public OpcionApuesta getOpcion() {
        return opcion;
    }

    public void setOpcion(OpcionApuesta opcion) {
        this.opcion = opcion;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public Date getFechaApuesta() {
        return fechaApuesta;
    }

    public void setFechaApuesta(Date fechaApuesta) {
        this.fechaApuesta = fechaApuesta;
    }
}