package com.example.christian.sifacmovil;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.ArrayList;

import com.example.christian.sifacmovil.AyudanteCrecionBD.AyudanteCreacionBD;
import com.example.christian.sifacmovil.Modelos.Producto;

public class PrincipalActivity extends AppCompatActivity {

    Button btIngresarCliente, btempezardia, btnuevousuario, btconsultarinventario;

    AyudanteConeccionMySql ayudanteConeccionMySql= new AyudanteConeccionMySql();

    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        setTitle("Menu Inicio");


        btIngresarCliente= (Button) findViewById(R.id.btIngresarClientes);
        btempezardia= (Button) findViewById(R.id.btEmpezarDia);
        btnuevousuario= (Button) findViewById(R.id.btNuevoUsuario);
        btconsultarinventario= (Button) findViewById(R.id.btInventario);

        btIngresarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AyudanteListarClientes().execute("http://192.168.43.199:8080/SifacMyF/listarClientes.php");
                new AyudanteRecogerProductos().execute("http://192.168.43.199:8080/SifacMyF/listarProductosDeRuta.php");
            }
        });

        btempezardia.setOnClickListener(new View.OnClickListener() {
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
        btconsultarinventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent ListSong = new Intent(getApplicationContext(), ListarImventarioActivity.class);
                    startActivity(ListSong);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"No se puede ir a la pantalla principal", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnuevousuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent ListSong = new Intent(getApplicationContext(), NuevoUsuarioActivity.class);
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


    private class AyudanteRecogerProductos extends AsyncTask<String, Void, String> {
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
                String ayudanteExcento="";
                String recojevalor="";
                String[] totalpalabras = new String[totalRegistros];


                for(int contadorArray=0;contadorArray<totalRegistros;contadorArray++){

                    valor=ja.getString(Integer.parseInt(String.valueOf(contadorArray)));
                    String[] ayudanteRecogeDatos=valor.split(",");
                    ContentValues values=new ContentValues();
                    values.put(AyudanteCreacionBD.CAMPO_CODIGO_PRODUCTO,ayudanteRecogeDatos[0].replace("[",""));
                    values.put(AyudanteCreacionBD.CAMPO_NOMBRE_PRODUCTO,ayudanteRecogeDatos[1].replace("\"",""));
                    values.put(AyudanteCreacionBD.CAMPO_EXISTENCIA,ayudanteRecogeDatos[2]);
                    values.put(AyudanteCreacionBD.CAMPO_PRECIO_PRODUCTO,ayudanteRecogeDatos[3]);
                    ayudanteExcento=ayudanteRecogeDatos[4].replace("]","");
                    values.put(AyudanteCreacionBD.CAMPO_EXCENTO,ayudanteExcento.replace("\"",""));
                    Long idResultante=db.insert(AyudanteCreacionBD.TABLA_PRODUCTO,null,values);
                    recojevalor+="Se ingreso id: "+ayudanteRecogeDatos[0].replace("[","")+"\n";
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
