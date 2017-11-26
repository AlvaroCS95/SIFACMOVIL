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

import java.util.ArrayList;
import com.example.christian.sifacmovil.AyudanteCrecionBD.AyudanteCreacionBD;

public class ListarImventarioActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
    ListView listaproducto;
    Button Actualizarinventario;
    ArrayList<String> listaProductosInventario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_imventario);
        setTitle("Inventario");
        Actualizarinventario=(Button)findViewById(R.id.ActualizarInventario);
        listaproducto = (ListView)findViewById(R.id.ListaInventario);
        listaProductosInventario = new ArrayList<>();
        ListarProductos();
        ArrayAdapter adaptador= new ArrayAdapter(this,android.R.layout.simple_list_item_1,listaProductosInventario);
        listaproducto.setAdapter(adaptador);

        Actualizarinventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    finish();
                    Intent ListSong = new Intent(getApplicationContext(), ListarImventarioActivity.class);
                    startActivity(ListSong);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"No se puede ir a la pantalla principal", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    void ListarProductos(){

        SQLiteDatabase db=conn.getReadableDatabase();
        listaProductosInventario =new ArrayList<String>();
        String DatosAdicionales="";
        String CodigoProducto="";
        String Producto="";
        String Excento="";
        Cursor cursor=db.rawQuery("SELECT * FROM "+ AyudanteCreacionBD.TABLA_PRODUCTO,null);
        int Contador=0;

        while (cursor.moveToNext()){
            DatosAdicionales="";
            CodigoProducto=cursor.getString(0)+"";
            DatosAdicionales+=cursor.getString(1)+"\n";
            DatosAdicionales+="Existencias: "+cursor.getFloat(2)+"\n";
            DatosAdicionales+="¢ "+cursor.getFloat(3)+"\n";
            Excento=cursor.getString(4);
            if(Excento.equalsIgnoreCase("Null")){
                DatosAdicionales+="No Excento";
            }else{
                DatosAdicionales+="Exento";
            }
            Producto=CodigoProducto+"- "+DatosAdicionales;
            listaProductosInventario.add(Producto);

            Contador++;
        }

    }
}
