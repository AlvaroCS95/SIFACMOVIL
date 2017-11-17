package com.example.christian.sifacmovil.Modelos;

import java.io.Serializable;

/**
 * Created by Christian on 16/11/2017.
 */

public class FacturaVenta implements Serializable {

    String codigoProducto, nuReferencia;
    float MontoDeVenta, cantidadVendida, montoDescuento, precioVendido, montCancelado;
    int idCliente, idTipoDePago, diasPlazo;

    public void CrearFacturaDeVentaContado(float monto, int idCliente, int idTipoPago, String nuReferencia) {
        this.MontoDeVenta = monto;
        this.idCliente = idCliente;
        this.idTipoDePago = idTipoPago;
        this.nuReferencia = nuReferencia;
    }

    public void AgregarDetalleFactura(String codigo, float cantidad, float descuento, float precioVendido) {
        this.codigoProducto = codigo;
        this.cantidadVendida = cantidad;
        this.montoDescuento = descuento;
        this.precioVendido = precioVendido;
    }
    public void CrearFacturaVentaCredito(float monto, int idCliente, int idTipoPago, String nuReferencia, int plazoDias,
                                         float montoCancelado) {
        this.MontoDeVenta = monto;
        this.idCliente = idCliente;
        this.idTipoDePago = idTipoPago;
        this.nuReferencia = nuReferencia;
        this.diasPlazo = plazoDias;
        this.montCancelado = montoCancelado;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public String getNuReferencia() {
        return nuReferencia;
    }

    public float getMontoDeVenta() {
        return MontoDeVenta;
    }

    public float getCantidadVendida() {
        return cantidadVendida;
    }

    public float getMontoDescuento() {
        return montoDescuento;
    }

    public float getPrecioVendido() {
        return precioVendido;
    }

    public float getMontCancelado() {
        return montCancelado;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdTipoDePago() {
        return idTipoDePago;
    }

    public int getDiasPlazo() {
        return diasPlazo;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public void setNuReferencia(String nuReferencia) {
        this.nuReferencia = nuReferencia;
    }

    public void setMontoDeVenta(float montoDeVenta) {
        MontoDeVenta = montoDeVenta;
    }

    public void setCantidadVendida(float cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public void setMontoDescuento(float montoDescuento) {
        this.montoDescuento = montoDescuento;
    }

    public void setPrecioVendido(float precioVendido) {
        this.precioVendido = precioVendido;
    }

    public void setMontCancelado(float montCancelado) {
        this.montCancelado = montCancelado;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setIdTipoDePago(int idTipoDePago) {
        this.idTipoDePago = idTipoDePago;
    }

    public void setDiasPlazo(int diasPlazo) {
        this.diasPlazo = diasPlazo;
    }
}
