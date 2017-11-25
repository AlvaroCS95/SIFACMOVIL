package com.example.christian.sifacmovil;

/**
 * Created by Christian on 24/11/2017.
 */

public class AyudanteDetalleFacturaVenta {
    String Codigo = "";
    String Nombre = "";
    Float unidades;
    Float descuento;
    Float totalPagar;

    public AyudanteDetalleFacturaVenta(String codigo, String nombre, Float unidades, Float descuento, Float totalPagar) {
        this.Codigo = codigo;
        this.Nombre = nombre;
        this.unidades = unidades;
        this.descuento = descuento;
        this.totalPagar = totalPagar;
    }

    public AyudanteDetalleFacturaVenta() {
    }

    public String getCodigo() {
        return Codigo;
    }

    public String getNombre() {
        return Nombre;
    }

    public Float getUnidades() {
        return unidades;
    }

    public Float getDescuento() {
        return descuento;
    }

    public Float getTotalPagar() {
        return totalPagar;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setUnidades(Float unidades) {
        this.unidades = unidades;
    }

    public void setDescuento(Float descuento) {
        this.descuento = descuento;
    }

    public void setTotalPagar(Float totalPagar) {
        this.totalPagar = totalPagar;
    }
}
