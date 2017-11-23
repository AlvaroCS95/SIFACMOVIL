package com.example.christian.sifacmovil;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christian.sifacmovil.AyudanteCrecionBD.AyudanteCreacionBD;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class NuevoUsuarioActivity extends AppCompatActivity {

    AyudanteConeccionMySql ayudanteConeccionMySql= new AyudanteConeccionMySql();
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
    Button Ingresar;
    TextView etNombreUsuarioNuevo,etContraseñaNuevo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario);
        Ingresar=(Button)findViewById(R.id.btIngresarNuevoUSuario);
        etNombreUsuarioNuevo=(TextView) findViewById(R.id.etNombreUsuarioNuevo);
        etContraseñaNuevo=(TextView)findViewById(R.id.etContraseñaNueva);
        int IdUsuario;
        String Usuario;
        String Contraseña;
        Ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etNombreUsuarioNuevo.getText().toString().equals("")||etContraseñaNuevo.getText().toString().equals("")){

                    Toast.makeText(getApplicationContext(),"Por favor ingrese los datos requeridos", Toast.LENGTH_LONG).show();
                    return;
                }else{

                    new AyudanteNuevoUsuario().execute("http://192.168.43.199:8080/Sifacmyf/IngresarNuevoUsuario.php?nombreUsuario="+etNombreUsuarioNuevo.getText().toString()+"&pass="+etContraseñaNuevo.getText().toString());
                }


            }
        });


    }


    private class AyudanteNuevoUsuario extends AsyncTask<String, Void, String> {
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

                String valor="";
                String ayudanteUsuario="";
                String recojevalor="";

                    //String[] ayudante=valor.split(",");
                    ContentValues values=new ContentValues();
                    values.put(AyudanteCreacionBD.CAMPO_ID_USUARIO,ja.getString(Integer.parseInt(String.valueOf(0))));
                    values.put(AyudanteCreacionBD.CAMPO_NOMBRE_USUARIO,ja.getString(1).replace("\"",""));
                    ayudanteUsuario=ja.getString(2).replace("]","");
                    values.put(AyudanteCreacionBD.CAMPO_CONTRASEÑA,ayudanteUsuario.replace("\"",""));
                    Long idResultante=db.insert(AyudanteCreacionBD.TABLA_USUARIO,null,values);
                    Toast.makeText(getApplicationContext(),"Se ingeso el usuario: "+ja.getString(1), Toast.LENGTH_LONG).show();

                try {
                    Intent ListSong = new Intent(getApplicationContext(), PrincipalActivity.class);
                    startActivity(ListSong);
                    finish();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"No se puede ir a la pantalla principal", Toast.LENGTH_LONG).show();
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
