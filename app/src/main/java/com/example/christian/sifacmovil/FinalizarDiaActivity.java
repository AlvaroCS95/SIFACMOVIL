package com.example.christian.sifacmovil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import com.example.christian.sifacmovil.AyudanteCrecionBD.AyudanteCreacionBD;
public class FinalizarDiaActivity extends AppCompatActivity {
    AyudanteConeccionMySql ayudanteConeccionMySql= new AyudanteConeccionMySql();
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
    Button finalizardia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_dia);
        setTitle("Finalizar día");
        finalizardia=(Button)findViewById(R.id.btfinalizardia);
        finalizardia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirmacionfinalizardia= new AlertDialog.Builder(FinalizarDiaActivity.this);
                confirmacionfinalizardia.setMessage("Seguro que desea finalizar el dia")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new AyudanteRecogerNumeroMaximoFactura().execute("http://192.168.43.199:8080/Sifacmyf/DevolverUltimoIdFacturaVenta.php");

                            }
                        }).setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                AlertDialog alertconfirmacionfinalizardia=confirmacionfinalizardia.create();
                alertconfirmacionfinalizardia.setTitle("Error");
                alertconfirmacionfinalizardia.show();



            }});
    }

    private class AyudanteRecogerNumeroMaximoFactura extends AsyncTask<String, Void, String> {
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
                SQLiteDatabase dbFactura=conn.getReadableDatabase();
                //SQLiteDatabase dbDetalle=conn.getReadableDatabase();
                Cursor cursorFacura=dbFactura.rawQuery("SELECT * FROM "+AyudanteCreacionBD.TABLA_FACTURA_VENTA, null);
                registro++;
                String refe="";

                while (cursorFacura.moveToNext()){
                    if(cursorFacura.getInt(1)==0){
                        refe=cursorFacura.getString(5);
                        new AyudanteIngresarFacturasDeVenta().execute("http://192.168.43.199:8080/Sifacmyf/IngresarFacturaVenta.php?monto="
                                +cursorFacura.getFloat(6)+"&cliente="+cursorFacura.getInt(3)+"&tpago="+cursorFacura.getInt(4)+"&" +
                                "nrefe="+refe+"&fech="+cursorFacura.getString(7)+"&usuario="+IngresarActivity.IdUsuario+"&nfactura="+cursorFacura.getInt(0)+"-"+IngresarActivity.Usuario);
                    }else if(cursorFacura.getInt(1)==1){

                        new AyudanteIngresarFacturasDeVenta().execute("http://192.168.43.199:8080/Sifacmyf/IngresarFacturaVentaCredito.php?" +
                                "monto="+cursorFacura.getFloat(6)+"&cliente="+cursorFacura.getInt(3)+"&tpago="+cursorFacura.getInt(4)+"&dias="+cursorFacura.getInt(8)+
                                "&usuario="+IngresarActivity.IdUsuario+"&fecha=" +
                                ""+cursorFacura.getString(7)+"&numerofac="+cursorFacura.getInt(0)+"-"+IngresarActivity.Usuario);
                    }

                    Cursor cursorDetalleFacura=dbFactura.rawQuery("SELECT * FROM "+AyudanteCreacionBD.TABLA_DETALLE_FACTURA_VENTA+" WHERE "+AyudanteCreacionBD.CAMPO_NUMERO_FACTURA_DETALLE+"="+cursorFacura.getInt(0), null);
                    while (cursorDetalleFacura.moveToNext()){

                        new AyudanteIngresarDetalleFacturasDeVenta().execute("http://192.168.43.199:8080/Sifacmyf/IngresarDetalleFacturaVenta.php?" +
                                "idprodu="+cursorDetalleFacura.getString(2)+"&cantidad="+cursorDetalleFacura.getInt(3)+
                                "&descu="+cursorDetalleFacura.getFloat(4)+"&preci="+cursorDetalleFacura.getFloat(5));
                    }

                }



            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error\nNO se pudo realizar", Toast.LENGTH_LONG).show();
                return;
            }

        }
    }
    private class AyudanteIngresarFacturasDeVenta extends AsyncTask<String, Void, String> {
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


        }
    }

    private class AyudanteIngresarDetalleFacturasDeVenta extends AsyncTask<String, Void, String> {
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


        }
    }
}
