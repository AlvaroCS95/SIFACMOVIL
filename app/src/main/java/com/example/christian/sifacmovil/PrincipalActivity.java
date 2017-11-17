package com.example.christian.sifacmovil;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.christian.sifacmovil.AyudanteCrecionBD.AyudanteCreacionBD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class PrincipalActivity extends AppCompatActivity {

    Button btIngresarCliente, consulta;
    AyudanteConeccionMySql ayudanteConeccionMySql= new AyudanteConeccionMySql();

    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        setTitle("Listar CLientes Para iniciar ruta");


        btIngresarCliente= (Button) findViewById(R.id.btIngresarClientes);
        consulta= (Button) findViewById(R.id.consulta);

        btIngresarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AyudanteListarClientes().execute("http://192.168.43.199:8080/SifacMyF/listarClientes.php");
            }
        });

        consulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent ListSong = new Intent(getApplicationContext(), ListarClientesActivity.class);
                    startActivity(ListSong);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"No se puede ir a la pantalla principal", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private class AyudanteListarClientes extends AsyncTask<String, Void, String> {
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
            SQLiteDatabase db =conn.getWritableDatabase();

            JSONArray ja = null;
            try {
                ja = new JSONArray(result);
                //Object jsonObject =JSONValue.parse(ja.toString());
                for(int contadorArray=0;contadorArray<ja.length();contadorArray++){
                    JSONObject json = ja.getJSONObject(contadorArray);

                //Toast.makeText(getApplicationContext(), ja.getString(1)+"\n"+ja.getString(5), Toast.LENGTH_LONG).show();
                    ContentValues valores = new ContentValues();
                    String valor=json.optString("IdCliente");
                    //valores.put(AyudanteCreacionBD.CAMPO_ID_CLIENTE, Integer.parseInt(valor));
                   // valores.put(AyudanteCreacionBD.CAMPO_NIVEL, json.optString("Nivel"));
                  //  valores.put(AyudanteCreacionBD.CAMPO_NOMBRE_LOCAL, json.optString("NombreLocal"));
                   // valores.put(AyudanteCreacionBD.CAMPO_DIRECCION, json.optString("Direccion"));

                   // Long IdInser = db.insert(AyudanteCreacionBD.TABLA_CLIENTE, AyudanteCreacionBD.CAMPO_ID_CLIENTE,valores);
                    //(Utilidades.TABLA_USUARIO,Utilidades.CAMPO_ID,values
                    Toast.makeText(getApplicationContext(), json.get("IdCliente").toString(), Toast.LENGTH_LONG).show();

                }
                //db.close();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error\nNO se pudo realizar", Toast.LENGTH_LONG).show();
                return;
            }

        }
    }



}
