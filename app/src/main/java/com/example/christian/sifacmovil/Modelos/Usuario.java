package com.example.christian.sifacmovil.Modelos;

import java.io.Serializable;

/**
 * Created by Christian on 16/11/2017.
 */

public class Usuario implements Serializable {
    public String NombreUsuario;
    public String Contraseña;
    public int Id;

    public Usuario(String NombreUsuario, String Contraseña, int Id) {
        this.Id=Id;
        this.NombreUsuario = NombreUsuario;
        this.Contraseña = Contraseña;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public void setNombreUsuario(String NombreUsuario) {
        this.NombreUsuario = NombreUsuario;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String Contraseña) {
        this.Contraseña = Contraseña;
    }

}
