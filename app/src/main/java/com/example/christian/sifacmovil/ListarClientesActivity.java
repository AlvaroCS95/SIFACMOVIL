package com.example.christian.sifacmovil;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.christian.sifacmovil.AyudanteCrecionBD.AyudanteCreacionBD;

public class ListarClientesActivity extends AppCompatActivity {

    Button btnVolver2, btnBuscar2, btnSalir2;
    EditText etId2, etNombres2, etTelefono2;
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_clientes);



        btnVolver2=(Button)findViewById(R.id.btnVolver2);
        btnBuscar2=(Button)findViewById(R.id.btnBuscar2);
        btnSalir2=(Button)findViewById(R.id.btnSalir2);



        etId2=(EditText)findViewById(R.id.etId2);
        etNombres2=(EditText)findViewById(R.id.etNombres2);
        etTelefono2=(EditText)findViewById(R.id.etTelefono2);


        btnBuscar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    SQLiteDatabase db =conn.getReadableDatabase();
                    //especifica cuales columnas de la base usaremos despues de la consulta
                    String[] projection = {
                            AyudanteCreacionBD.CAMPO_NOMBRE_LOCAL,
                            AyudanteCreacionBD.CAMPO_DIRECCION
                    };
                    String[] argsel = { etId2.getText().toString() };
                    //nos consulta compara la coluna id con lo que esta en la caja de texto
                    Cursor c=db.query(AyudanteCreacionBD.TABLA_CLIENTE, projection, AyudanteCreacionBD.CAMPO_ID_CLIENTE+"=?",argsel,null,null,null);

                    c.moveToFirst();
                    etNombres2.setText(c.getString(0));
                    etTelefono2.setText(c.getString(1));



                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"No existe ningún registro con ese id", Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}
