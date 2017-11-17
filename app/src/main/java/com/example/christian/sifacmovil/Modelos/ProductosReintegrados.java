package com.example.christian.sifacmovil.Modelos;

import java.io.Serializable;

/**
 * Created by Christian on 16/11/2017.
 */

public class ProductosReintegrados implements Serializable{

    String CodigoProducto;
    int IdDevoluciones;
    float CantidadReintegrada;

    public ProductosReintegrados() {
    }

    public ProductosReintegrados(String CodigoProducto, int IdDevoluciones, float CantidadReintegrada) {
        this.CodigoProducto = CodigoProducto;
        this.IdDevoluciones = IdDevoluciones;
        this.CantidadReintegrada = CantidadReintegrada;
    }

    public String getCodigoProducto() {
        return CodigoProducto;
    }

    public int getIdDevoluciones() {
        return IdDevoluciones;
    }

    public float getCantidadReintegrada() {
        return CantidadReintegrada;
    }

    public void setCodigoProducto(String codigoProducto) {
        CodigoProducto = codigoProducto;
    }

    public void setIdDevoluciones(int idDevoluciones) {
        IdDevoluciones = idDevoluciones;
    }

    public void setCantidadReintegrada(float cantidadReintegrada) {
        CantidadReintegrada = cantidadReintegrada;
    }
}
