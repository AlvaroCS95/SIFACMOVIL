package com.example.christian.sifacmovil;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.christian.sifacmovil.AyudanteCrecionBD.AyudanteCreacionBD;

import java.util.ArrayList;

public class ListarFacturasVentaActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
    ListView listaFacturaVenta;
    Button Actualizarfacturas;
    ArrayList<String> listaFacturas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_facturas_venta);
        setTitle("Faturad de Venta");
        Actualizarfacturas=(Button)findViewById(R.id.ActualizarFacturas);
        listaFacturaVenta = (ListView)findViewById(R.id.ListaFacturasVenta);
        listaFacturas = new ArrayList<>();
        ListarFacturas();
        ArrayAdapter adaptador= new ArrayAdapter(this,android.R.layout.simple_list_item_1,listaFacturas);
        listaFacturaVenta.setAdapter(adaptador);

        Actualizarfacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    finish();
                    Intent ListSong = new Intent(getApplicationContext(), ListarFacturasVentaActivity.class);
                    startActivity(ListSong);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"No se puede ir a listar facturas", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void ListarFacturas(){

        SQLiteDatabase db=conn.getReadableDatabase();
        listaFacturas =new ArrayList<String>();
        int NumeroFactura=0;
        Float Monto;
        String Local="";
        String Factura="";

        Cursor cursor=db.rawQuery("SELECT "+AyudanteCreacionBD.CAMPO_NUMERO_FACTURA+", "+AyudanteCreacionBD.CAMPO_NOMBRE_LOCAL+
                ", "+AyudanteCreacionBD.CAMPO_MONTO_TOTAL+" FROM "+ AyudanteCreacionBD.TABLA_FACTURA_VENTA+" INNER JOIN "+AyudanteCreacionBD.TABLA_CLIENTE+"" +
                " ON "+AyudanteCreacionBD.TABLA_FACTURA_VENTA+"."+AyudanteCreacionBD.CAMPO_ID_CLIENTE_VENTA+"="+AyudanteCreacionBD.TABLA_CLIENTE+"."+AyudanteCreacionBD.CAMPO_ID_CLIENTE,null);
        int Contador=0;

        while (cursor.moveToNext()){

            NumeroFactura=cursor.getInt(0);
            Monto=cursor.getFloat(1);
            Local=cursor.getString(2);
            Factura=NumeroFactura+"\n"+Monto+"\n";
            listaFacturas.add(Factura);
            Contador++;
        }

    }

}
