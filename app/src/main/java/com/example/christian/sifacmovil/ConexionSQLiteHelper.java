package com.example.christian.sifacmovil;

import android.content.Context;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.christian.sifacmovil.AyudanteCrecionBD.AyudanteCreacionBD;

/**
 * Created by Christian on 17/11/2017.
 */

public class ConexionSQLiteHelper extends SQLiteOpenHelper {



    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AyudanteCreacionBD.CREAR_TABLA_USUARIO);
        db.execSQL(AyudanteCreacionBD.CREAR_TABLA_CLIENTE);
        db.execSQL(AyudanteCreacionBD.CREAR_TABLA_DETALLE_FACTURA_VENTA);
        db.execSQL(AyudanteCreacionBD.CREAR_TABLA_FACTURA_VENTA);
        db.execSQL(AyudanteCreacionBD.CREAR_TABLA_PRODUCTO);
        db.execSQL(AyudanteCreacionBD.CREAR_TABLA_TIPO_PAGO);
        db.execSQL(AyudanteCreacionBD.CREAR_TABLA_TIPO_VENTA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS "+AyudanteCreacionBD.TABLA_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS "+AyudanteCreacionBD.TABLA_CLIENTE);
        db.execSQL("DROP TABLE IF EXISTS "+AyudanteCreacionBD.TABLA_DETALLE_FACTURA_VENTA);
        db.execSQL("DROP TABLE IF EXISTS "+AyudanteCreacionBD.TABLA_FACTURA_VENTA);
        db.execSQL("DROP TABLE IF EXISTS "+AyudanteCreacionBD.TABLA_PRODUCTO);
        db.execSQL("DROP TABLE IF EXISTS "+AyudanteCreacionBD.TABLA_TIPO_PAGO);
        db.execSQL("DROP TABLE IF EXISTS "+AyudanteCreacionBD.TABLA_TIPO_VENTA);
        onCreate(db);
    }
}
