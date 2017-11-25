package com.example.christian.sifacmovil;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christian.sifacmovil.AyudanteCrecionBD.AyudanteCreacionBD;

public class ListarClientesActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
    ListView lista;
    View header;
    AyudanteListViewClientes arregloClientes[];
    AdaptadorAyudanteListarClientes adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_clientes);
        lista = (ListView)findViewById(R.id.ListaDeClientes);
        header=(View) getLayoutInflater().inflate(R.layout.list_header_listarclientes,null);
        ListarClientes();
        adapter= new AdaptadorAyudanteListarClientes(this, R.layout.listview_listarclientes,arregloClientes);
        lista.addHeaderView(header);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {@Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView ncliente =(TextView)view.findViewById(R.id.NCliente);
                TextView direccion =(TextView)view.findViewById(R.id.NombreCliente);
                String[] ayudante=direccion.getText().toString().split(",");
            try {
                Intent ListSong = new Intent(getApplicationContext(), FacturaDeVentaActivity.class);
                ListSong.putExtra("NCliente", ncliente.getText().toString());
                ListSong.putExtra("Nombre", ayudante[1].replaceAll("\\s",""));
                startActivity(ListSong);
                finish();
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"No se puede ir a la pantalla principal", Toast.LENGTH_LONG).show();
            }
           }
       });
    }


    void ListarClientes(){

            SQLiteDatabase db=conn.getReadableDatabase();

            String DatosAdicionales="";
            String NCliente="";
            Cursor cursor=db.rawQuery("SELECT * FROM "+AyudanteCreacionBD.TABLA_CLIENTE,null);
            int COntador=0;
            AyudanteListViewClientes DatoDelCliente;
            arregloClientes= new AyudanteListViewClientes[cursor.getCount()];
            while (cursor.moveToNext()){
                DatosAdicionales="";
                NCliente=cursor.getInt(0)+"";
                DatosAdicionales+=cursor.getString(1)+",\n";
                DatosAdicionales+=cursor.getString(2)+",\n";
                DatosAdicionales+=cursor.getString(3)+"\n";
                DatoDelCliente= new AyudanteListViewClientes(R.drawable.ic_launcher,NCliente,DatosAdicionales);
                arregloClientes[COntador]=DatoDelCliente;

                COntador++;
            }

    }
}
