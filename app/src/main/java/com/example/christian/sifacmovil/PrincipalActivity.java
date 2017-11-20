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
import android.widget.EditText;
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
import com.example.christian.sifacmovil.AyudanteCrecionBD.AyudanteCreacionBD;
public class PrincipalActivity extends AppCompatActivity {

    Button btIngresarCliente, consulta;
    AyudanteConeccionMySql ayudanteConeccionMySql= new AyudanteConeccionMySql();
    EditText edit;
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        setTitle("Listar CLientes Para iniciar ruta");


        btIngresarCliente= (Button) findViewById(R.id.btIngresarClientes);
        consulta= (Button) findViewById(R.id.consulta);
        edit= (EditText) findViewById(R.id.prueba);

        btIngresarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AyudanteListarClientes().execute("http://10.234.203.12:8080/SifacMyF/listarClientes.php");
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


            SQLiteDatabase db=conn.getWritableDatabase();
            JSONArray ja = null;
            try {
                ja = new JSONArray(result);
                int totalRegistros=ja.length();
                String valor="";
                String ayudantedireccion="";
                String recojevalor="";
                String[] totalpalabras = new String[totalRegistros];


                for(int contadorArray=0;contadorArray<totalRegistros;contadorArray++){

                    valor=ja.getString(Integer.parseInt(String.valueOf(contadorArray)));
                    String[] ayudante=valor.split(",");
                    ContentValues values=new ContentValues();
                    values.put(AyudanteCreacionBD.CAMPO_ID_CLIENTE,ayudante[0].replace("[",""));
                    values.put(AyudanteCreacionBD.CAMPO_NOMBRE_LOCAL,ayudante[1].replace("\"",""));
                    values.put(AyudanteCreacionBD.CAMPO_NIVEL,ayudante[2].replace("\"",""));
                    ayudantedireccion=ayudante[3].replace("]","");
                    values.put(AyudanteCreacionBD.CAMPO_DIRECCION,ayudantedireccion.replace("\"",""));
                    Long idResultante=db.insert(AyudanteCreacionBD.TABLA_CLIENTE,null,values);
                    recojevalor+="Se ingreso id: "+ayudante[0].replace("[","")+"\n";
                }



                Toast.makeText(getApplicationContext(),recojevalor+"", Toast.LENGTH_LONG).show();
                //db.close();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error\nNO se pudo realizar", Toast.LENGTH_LONG).show();
                return;
            }

        }
    }



}
