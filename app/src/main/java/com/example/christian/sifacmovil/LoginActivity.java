package com.example.christian.sifacmovil;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    TextView etNombreUsuario,etContraseña;
    Button btIngresar;
    AyudanteConeccionMySql ayudanteConeccionMySql= new AyudanteConeccionMySql();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Iniciar Sesión");



        etNombreUsuario=(TextView) findViewById(R.id.etNombreUsuario);
        etContraseña=(TextView) findViewById(R.id.etContraseña);
        btIngresar=(Button) findViewById(R.id.btIngresar);

        btIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AyudanteConeccionLogin().execute("http://192.168.43.199:8080/SifacMyF/login.php?nombreUsuario="+etNombreUsuario.getText().toString()+"&pass="+etContraseña.getText().toString());
            }
        });

    }
    private class AyudanteConeccionLogin extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return ayudanteConeccionMySql.downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            JSONArray ja = null;
            try {
                ja = new JSONArray(result);

                    Toast.makeText(getApplicationContext(), "Bienvenido a la aplicación", Toast.LENGTH_LONG).show();
                    try {
                        Intent ListSong = new Intent(getApplicationContext(), PrincipalActivity.class);
                        startActivity(ListSong);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"No se puede ir a la pantalla principal", Toast.LENGTH_LONG).show();
                    }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error\nDatos incorrectos", Toast.LENGTH_LONG).show();
                return;
            }

        }
    }



}
