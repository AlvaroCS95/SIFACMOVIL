package com.example.christian.sifacmovil.Modelos;

import java.io.Serializable;

/**
 * Created by Christian on 16/11/2017.
 */

public class Cliente implements Serializable {
    public static int Id=0;
    public static int Nivel=0;
    public static String NombreLocal="";
    public static String RazónSocail="";

    public Cliente() {
    }

    public Cliente(int Id, int Nivel, String NombreLocal, String RazónSocail) {
        this.Id=Id;
        this.Nivel=Nivel;
        this.NombreLocal=NombreLocal;
        this.RazónSocail=RazónSocail;
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

    public static String getRazónSocail() {
        return RazónSocail;
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

    public static void setRazónSocail(String razónSocail) {
        RazónSocail = razónSocail;
    }
}
