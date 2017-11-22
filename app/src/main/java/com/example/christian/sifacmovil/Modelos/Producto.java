package com.example.christian.sifacmovil.Modelos;

import java.io.Serializable;

/**
 * Created by Christian on 16/11/2017.
 */

public class Producto implements Serializable{
    String Codigo;
    String Nombre;
    float Existencias;
    float Precio;
    String exento;

    public Producto() {
    }

    public Producto(String Codigo, String Nombre,  float Existencias, float Precio,
                    String exen) {
        this.Codigo = Codigo;
        this.Nombre = Nombre;
        this.Existencias = Existencias;
        this.Precio = Precio;
        this.exento = exen;
    }

    public String getCodigo() {
        return Codigo;
    }

    public String getNombre() {
        return Nombre;
    }

    public float getExistencias() {
        return Existencias;
    }

    public float getPrecio() {
        return Precio;
    }

    public String getExento() {
        return exento;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setExistencias(float existencias) {
        Existencias = existencias;
    }

    public void setPrecio(float precio) {
        Precio = precio;
    }

    public void setExento(String exento) {
        this.exento = exento;
    }
}
