package com.example.christian.sifacmovil.AyudanteCrecionBD;

/**
 * Created by Christian on 17/11/2017.
 */

public class AyudanteCreacionBD {

    public static final String TABLA_USUARIO="usuario";
    public static final String CAMPO_ID_USUARIO="id";
    public static final String CAMPO_NOMBRE_USUARIO="nombre";
    public static final String CAMPO_CONTRASEÑA="contraseña";

    public static final String CREAR_TABLA_USUARIO="CREATE TABLE " +
            ""+TABLA_USUARIO+" ("+CAMPO_ID_USUARIO+" " +
            "INTEGER, "+CAMPO_NOMBRE_USUARIO+" TEXT,"+CAMPO_CONTRASEÑA+" TEXT)";

    //Constantes campos tabla Clientes
    public static final String TABLA_CLIENTE="cliente";
    public static final String CAMPO_ID_CLIENTE="id_cliente";
    public static final String CAMPO_NIVEL="nivel_cliente";
    public static final String CAMPO_NOMBRE_LOCAL="nombre_local";
    public static final String CAMPO_DIRECCION="direccion";


    public static final String CREAR_TABLA_CLIENTE="CREATE TABLE " +
            ""+TABLA_CLIENTE+" ("+CAMPO_ID_CLIENTE+" INTEGER, "
            +CAMPO_NIVEL+" TEXT, "+CAMPO_NOMBRE_LOCAL+" TEXT, "+CAMPO_DIRECCION+" TEXT)";

    //Constantes campos tabla Producto
    public static final String TABLA_PRODUCTO="producto";
    public static final String CAMPO_CODIGO_PRODUCTO="codigo_producto";
    public static final String CAMPO_EXISTENCIA="existencia";
    public static final String CAMPO_EXCENTO="excento";


    public static final String CREAR_TABLA_PRODUCTO="CREATE TABLE " +
            ""+TABLA_PRODUCTO+" ("+CAMPO_CODIGO_PRODUCTO+" TEXT PRIMARY KEY, "
            +CAMPO_EXISTENCIA+" FLOAT, "+CAMPO_EXCENTO+" TEXT)";


    //Constantes campos tabla TipoPago
    public static final String TABLA_TIPO_PAGO="tipo_pago";
    public static final String CAMPO_ID_TIPO_PAGO="id_tipo_pago";
    public static final String CAMPO_TIPO_PAGO="tipo_pago";


    public static final String CREAR_TABLA_TIPO_PAGO="CREATE TABLE " +
            ""+TABLA_TIPO_PAGO+" ("+CAMPO_ID_TIPO_PAGO+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_TIPO_PAGO+" TEXT)";

    //Constantes campos tabla TipoVenta
    public static final String TABLA_TIPO_VENTA="tipo_venta";
    public static final String CAMPO_ID_TIPO_VENTA="id_tipo_venta";
    public static final String CAMPO_TIPO_VENTA="tipo_venta";


    public static final String CREAR_TABLA_TIPO_VENTA="CREATE TABLE " +
            ""+TABLA_TIPO_VENTA+" ("+CAMPO_ID_TIPO_VENTA+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_TIPO_VENTA+" TEXT)";

    //Constantes campos tabla FacturaVenta
    public static final String TABLA_FACTURA_VENTA="factura_venta";
    public static final String CAMPO_NUMERO_FACTURA="numero_factura";
    public static final String CAMPO_TIPO_VENTA_VENTA="id_tipo_venta";
    public static final String CAMPO_ID_USUARIO_VENTA="id_usuario";
    public static final String CAMPO_ID_CLIENTE_VENTA="id_cliente";
    public static final String CAMPO_TIPO_PAGO_VENTA="id_tipo_pago";
    public static final String CAMPO_N_REFERENCIA="n_referencia";
    public static final String CAMPO_MONTO_TOTAL="monto_total";
    public static final String CAMPO_FECHA="fecha";


    public static final String CREAR_TABLA_FACTURA_VENTA="CREATE TABLE " +
            ""+TABLA_FACTURA_VENTA+" ("+CAMPO_NUMERO_FACTURA+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_TIPO_VENTA_VENTA+" INTEGER, "+CAMPO_ID_USUARIO_VENTA+" INTEGER, "+CAMPO_ID_CLIENTE_VENTA+" INTEGER, " +
            CAMPO_TIPO_PAGO_VENTA+" INTEGER, "+CAMPO_N_REFERENCIA+" TEXT, "+CAMPO_MONTO_TOTAL+" FLOAT, "+CAMPO_FECHA+" DATETIME)";

    //Constantes campos tabla DetalleFacturaVenta
    public static final String TABLA_DETALLE_FACTURA_VENTA="detalle_factura_venta";
    public static final String CAMPO_ID_DETALLE_FACTURA="id_detalle_factura";
    public static final String CAMPO_NUMERO_FACTURA_DETALLE="numero_factura";
    public static final String CAMPO_ID_PRODUCTO_DETALLE="id_producto";
    public static final String CAMPO_CANTIDAD="cantidad";
    public static final String CAMPO_DESCUENTO="descuento";
    public static final String CAMPO_PRECIO_VENTA="precio_venta";



    public static final String CREAR_TABLA_DETALLE_FACTURA_VENTA="CREATE TABLE " +
            ""+TABLA_DETALLE_FACTURA_VENTA+" ("+CAMPO_ID_DETALLE_FACTURA+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_NUMERO_FACTURA_DETALLE+" INTEGER, "+CAMPO_ID_PRODUCTO_DETALLE+" INTEGER, "+CAMPO_CANTIDAD+" FLOAT, " +
            CAMPO_DESCUENTO+" FLOAT, "+CAMPO_PRECIO_VENTA+" FLOAT)";

}
