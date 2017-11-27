package com.example.christian.sifacmovil;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.christian.sifacmovil.AyudanteCrecionBD.AyudanteCreacionBD;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class IngresarDiaActivity extends AppCompatActivity {
    AyudanteConeccionMySql ayudanteConeccionMySql= new AyudanteConeccionMySql();
    public static int CantidadProductos=0;
    Button ingresardia;

    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_dia);
        setTitle("Ingresar itenerario");

        ingresardia=(Button)findViewById(R.id.btIngresarDia);

        ingresardia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirmacionIniciardia= new AlertDialog.Builder(IngresarDiaActivity.this);
                confirmacionIniciardia.setMessage("Seguro que desea iniciar el dia")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new AyudanteRecogerNumeroProductos().execute("http://192.168.43.199:8080/SifacMyF/DevuelveCuantosRegistros.php");
                                new AyudanteRecogerNumeroClientes().execute("http://192.168.43.199:8080/SifacMyF/DevuelveCuantosRegistrosClientes.php");

                            }
                        }).setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                AlertDialog alertconfirmacionIniciardia=confirmacionIniciardia.create();
                alertconfirmacionIniciardia.setTitle("Error");
                alertconfirmacionIniciardia.show();



            }
        });


    }


    private class AyudanteRecogerNumeroClientes extends AsyncTask<String, Void, String> {
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
                int totalRegistros=ja.length();
                int registro=0;
                String valor;
                valor=ja.getString(Integer.parseInt(String.valueOf(0)));
                registro=Integer.parseInt(valor);
                int calcular=0;
                while(registro>0){
                    new AyudanteListarClientes().execute("http://192.168.43.199:8080/SifacMyF/listarClientes.php?x="+calcular+"&y=5");
                    calcular+=5;
                    registro=registro-5;

                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error\nNO se pudo realizar", Toast.LENGTH_LONG).show();
                return;
            }

        }
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
                String produ="";
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
                String produ="";

                for(int contadorArray=0;contadorArray<totalRegistros;contadorArray++){

                    valor=ja.getString(Integer.parseInt(String.valueOf(contadorArray)));
                    String[] ayudanteRecogeDatos=valor.split(",");
                    ContentValues values=new ContentValues();
                    produ=ayudanteRecogeDatos[0].replace("[","");
                    values.put(AyudanteCreacionBD.CAMPO_CODIGO_PRODUCTO,produ.replace("\"",""));
                    values.put(AyudanteCreacionBD.CAMPO_NOMBRE_PRODUCTO,ayudanteRecogeDatos[1].replace("\"",""));
                    values.put(AyudanteCreacionBD.CAMPO_EXISTENCIA,ayudanteRecogeDatos[2]);
                    values.put(AyudanteCreacionBD.CAMPO_PRECIO_PRODUCTO,ayudanteRecogeDatos[3]);
                    ayudanteExcento=ayudanteRecogeDatos[4].replace("]","");
                    values.put(AyudanteCreacionBD.CAMPO_EXCENTO,ayudanteExcento.replace("\"",""));
                    Long idResultante=db.insert(AyudanteCreacionBD.TABLA_PRODUCTO,null,values);
                    recojevalor+="Se ingreso id: "+produ.replace("\"","")+"\n";
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

    private class AyudanteRecogerNumeroProductos extends AsyncTask<String, Void, String> {
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
                int totalRegistros=ja.length();
                int registro=0;
                String valor;
                valor=ja.getString(Integer.parseInt(String.valueOf(0)));
                registro=Integer.parseInt(valor);
                int calcular=0;
                  while(registro>0){
                      new AyudanteRecogerProductos().execute("http://192.168.43.199:8080/SifacMyF/listarProductosDeRuta.php?x="+calcular+"&y=5");
                      calcular+=5;
                      registro=registro-5;

                  }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error\nNO se pudo realizar", Toast.LENGTH_LONG).show();
                return;
            }

        }
    }


}
