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

    Button btIngresarCliente, btempezardia, btnuevousuario, btconsultarinventario,btListarFacturas,finalizardiaprincipal,btInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        setTitle("Menu Inicio");


        btIngresarCliente= (Button) findViewById(R.id.btIngresarClientes);
        btempezardia= (Button) findViewById(R.id.btEmpezarDia);
        btnuevousuario= (Button) findViewById(R.id.btNuevoUsuario);
        btconsultarinventario= (Button) findViewById(R.id.btInventario);
        btListarFacturas=(Button) findViewById(R.id.btListarFacturas);
        finalizardiaprincipal=(Button) findViewById(R.id.finalizardiaprincipal);
        btInfo=(Button) findViewById(R.id.btInfo);
        btIngresarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent ListSong = new Intent(getApplicationContext(), IngresarDiaActivity.class);
                    startActivity(ListSong);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"No se puede ir a la pantalla ingresar itenerario", Toast.LENGTH_LONG).show();
                }
            }
        });

        btempezardia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent ListSong = new Intent(getApplicationContext(), ListarClientesActivity.class);
                    startActivity(ListSong);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"No se puede ir a la pantalla empezar dia", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(),"No se puede ir a consultar inventario", Toast.LENGTH_LONG).show();
                }
            }
        });

        btInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent ListSong = new Intent(getApplicationContext(), InfoActivity.class);
                    startActivity(ListSong);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"No se puede ir a consultar inventario", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(),"No se puede ir a lanuevo usuario", Toast.LENGTH_LONG).show();
                }
            }
        });

        btListarFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent ListSong = new Intent(getApplicationContext(), ListarFacturasVentaActivity.class);
                    startActivity(ListSong);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"No se puede ir a listar facturas", Toast.LENGTH_LONG).show();
                }
            }
        });
        finalizardiaprincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent ListSong = new Intent(getApplicationContext(), FinalizarDiaActivity.class);
                    startActivity(ListSong);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"No se puede ir a finalizar dia", Toast.LENGTH_LONG).show();
                }
            }
        });


    }






}
