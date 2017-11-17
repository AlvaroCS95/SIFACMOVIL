package com.example.christian.sifacmovil.Modelos;

import java.io.Serializable;

/**
 * Created by Christian on 16/11/2017.
 */

public class Cliente implements Serializable {
    public static int Id;
    public static int Nivel;
    public static String NombreLocal;
    public static String Direccion;


    public Cliente() {
    }

    public Cliente(int Id, int Nivel, String NombreLocal, String Direccion) {
        this.Id=Id;
        this.Nivel=Nivel;
        this.NombreLocal=NombreLocal;
        this.Direccion=Direccion;
    }

    public static String getDireccion() {
        return Direccion;
    }

    public static void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public static int getId() {
        return Id;
    }

    public static int getNivel() {
        return Nivel;
    }

    public static String getNombreLocal() {
        return NombreLocal;
    }



    public static void setId(int id) {
        Id = id;
    }

    public static void setNivel(int nivel) {
        Nivel = nivel;
    }

    public static void setNombreLocal(String nombreLocal) {
        NombreLocal = nombreLocal;
    }


}
