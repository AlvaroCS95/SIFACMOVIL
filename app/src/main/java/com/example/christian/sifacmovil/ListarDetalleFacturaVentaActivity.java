package com.example.christian.sifacmovil;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.christian.sifacmovil.AyudanteCrecionBD.AyudanteCreacionBD;

import java.util.ArrayList;

public class ListarDetalleFacturaVentaActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
    ListView listaDetalleFacturaVenta;
    ArrayList<String> listaDetalleFacturas;
    String nFactura;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_detalle_factura_venta);
        Bundle bundle=getIntent().getExtras();
        //text.setText(bundle.getString("NCliente")+" - Nombre Cliente: "+bundle.getString("Nombre"));
        nFactura=bundle.getString("NFactura");
        setTitle("Detalle Factura: "+nFactura);

        listaDetalleFacturaVenta = (ListView)findViewById(R.id.ListaDetalleFactura);
        listaDetalleFacturas = new ArrayList<>();
        ListarDetalleFacturas();
        ArrayAdapter adaptador= new ArrayAdapter(this,android.R.layout.simple_list_item_1,listaDetalleFacturas);
        listaDetalleFacturaVenta.setAdapter(adaptador);

    }

    void ListarDetalleFacturas(){

        SQLiteDatabase db=conn.getReadableDatabase();
        listaDetalleFacturas =new ArrayList<String>();
        String codigoProducto="";
        Float Preio;
        Float cantidad;
        Float montoTotal;
        String DetalleVenta="";

        Cursor cursor=db.rawQuery("SELECT * FROM "+AyudanteCreacionBD.TABLA_DETALLE_FACTURA_VENTA+ " WHERE "+AyudanteCreacionBD.CAMPO_NUMERO_FACTURA_DETALLE+"="+nFactura, null);

        while (cursor.moveToNext()){

            codigoProducto="Codigo: "+cursor.getString(2)+"\n" +
                    "Producto: "+DevuelveNombre(cursor.getString(2))+"\n";
            cantidad=cursor.getFloat(3);
            Preio=cursor.getFloat(5);
            montoTotal=cantidad*Preio;
            DetalleVenta=codigoProducto+"Cantidad: "+cantidad+"\nPrecio Vendido: "+Preio+"\nMonto Total: "+montoTotal;
            listaDetalleFacturas.add(DetalleVenta);
        }

    }

    String DevuelveNombre(String codigo){
        String Nombre="";
        SQLiteDatabase db=conn.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT "+AyudanteCreacionBD.CAMPO_NOMBRE_PRODUCTO+" FROM "+ AyudanteCreacionBD.TABLA_PRODUCTO+" WHERE "+
                AyudanteCreacionBD.CAMPO_CODIGO_PRODUCTO+" = "+codigo+"", null);
        while (cursor.moveToNext()){
            Nombre=cursor.getString(0);

        }
     return Nombre;
    }
}
