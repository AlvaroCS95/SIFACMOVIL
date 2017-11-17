package com.example.christian.sifacmovil.Modelos;

import java.io.Serializable;

/**
 * Created by Christian on 16/11/2017.
 */

public class Devolucion implements Serializable {

    int CedulaUsuario;
    int NumeroFactura;
    float ValorTotal;
    float ReintegroAlCliente;

    public Devolucion( int CedulaUsuario, int NumeroFactura, float ValorTotal, float ReintegroAlCliente) {

        this.CedulaUsuario = CedulaUsuario;
        this.NumeroFactura = NumeroFactura;
        this.ValorTotal = ValorTotal;
        this.ReintegroAlCliente = ReintegroAlCliente;
    }

    public Devolucion(){};


    public int getCedulaUsuario() {
        return CedulaUsuario;
    }

    public int getNumeroFactura() {
        return NumeroFactura;
    }

    public float getValorTotal() {
        return ValorTotal;
    }

    public float getReintegroAlCliente() {
        return ReintegroAlCliente;
    }

    public void setCedulaUsuario(int cedulaUsuario) {
        CedulaUsuario = cedulaUsuario;
    }

    public void setNumeroFactura(int numeroFactura) {
        NumeroFactura = numeroFactura;
    }

    public void setValorTotal(float valorTotal) {
        ValorTotal = valorTotal;
    }

    public void setReintegroAlCliente(float reintegroAlCliente) {
        ReintegroAlCliente = reintegroAlCliente;
    }
}
