package com.example.christian.sifacmovil;

import android.content.Intent;
import android.database.Cursor;
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

public class IngresarActivity extends AppCompatActivity {
    TextView etNombreUsuario,etContraseña;
    Button btIngresar;
    AyudanteConeccionMySql ayudanteConeccionMySql= new AyudanteConeccionMySql();
    SQLiteDatabase db;
    public static String Contraseña;
    public static String Usuario;
    public static int IdUsuario;
    ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar);
        setTitle("Iniciar Sesión");

        etNombreUsuario=(TextView) findViewById(R.id.etNombreUsuario);
        etContraseña=(TextView) findViewById(R.id.etContraseña);
        btIngresar=(Button) findViewById(R.id.btIngresar);

        btIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db=conn.getReadableDatabase();
                IdUsuario=0;
                Usuario="";
                Contraseña="";

               if(etNombreUsuario.getText().toString().equals("")||etContraseña.getText().toString().equals("")){

                   Toast.makeText(getApplicationContext(),"Por favor ingrese los datos requeridos", Toast.LENGTH_LONG).show();
                   return;
               }else{
                   try {

                       Cursor cursor=db.rawQuery("SELECT * FROM "+
                               AyudanteCreacionBD.TABLA_USUARIO+" WHERE "+AyudanteCreacionBD.CAMPO_NOMBRE_USUARIO+" = '"+etNombreUsuario.getText().toString()+"' AND "+
                               AyudanteCreacionBD.CAMPO_CONTRASEÑA+" = '"+etContraseña.getText().toString()+"';",null);

                       while (cursor.moveToNext()) {
                           IdUsuario=cursor.getInt(0);
                           Usuario=cursor.getString(1);
                           Contraseña=cursor.getString(2);
                       }

                       if((Usuario.equals(etNombreUsuario.getText().toString()))&&(Contraseña.equals(etContraseña.getText().toString()))){
                           Toast.makeText(getApplicationContext(), "Bienvenido a la aplicación: "+Usuario, Toast.LENGTH_LONG).show();
                           try {
                               Intent ListSong = new Intent(getApplicationContext(), PrincipalActivity.class);
                               startActivity(ListSong);

                           }catch (Exception e){
                               Toast.makeText(getApplicationContext(),"No se puede ir a la pantalla principal", Toast.LENGTH_LONG).show();
                           }
                       }else{
                           Toast.makeText(getApplicationContext(),"Datos incorrectos", Toast.LENGTH_LONG).show();
                       }

                   }catch (Exception e){
                       Toast.makeText(getApplicationContext(),"Surgio un error, por favor comuniquese con su usuario adminisrador, o vuelva " +
                               "a intentarlo más tarde", Toast.LENGTH_LONG).show();
                   }

               }

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
            //new AyudanteConeccionLogin().execute("http://10.234.203.12:8080/SifacMyF/login.php?nombreUsuario="+etNombreUsuario.getText().toString()+"&pass="+etContraseña.getText().toString());
            JSONArray ja = null;
            try {
                ja = new JSONArray(result);


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
