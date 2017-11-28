package com.example.christian.sifacmovil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class InfoActivity extends AppCompatActivity {
  Button btRegresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setTitle("Acerca SIFACMYF");
        btRegresar=(Button) findViewById(R.id.btRegresar);
        btRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent ListSong = new Intent(getApplicationContext(), PrincipalActivity.class);
                    startActivity(ListSong);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"No se puede ir a consultar inventario", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
