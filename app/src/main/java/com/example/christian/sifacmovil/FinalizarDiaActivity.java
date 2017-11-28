package com.example.christian.sifacmovil;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

import com.example.christian.sifacmovil.AyudanteCrecionBD.AyudanteCreacionBD;
public class FinalizarDiaActivity extends AppCompatActivity {
    //bluetooth
    static BluetoothAdapter mBluetoothAdapter;
    static BluetoothSocket mmSocket;
    static BluetoothDevice mmDevice;
    public static OutputStream mmOutputStream;
    public static InputStream mmInputStream;
    public static Thread workerThread;

    public static byte[] readBuffer;
    public  static int readBufferPosition;
    public static volatile boolean stopWorker;
    //bluetooth
    AyudanteConeccionMySql ayudanteConeccionMySql= new AyudanteConeccionMySql();
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
    ConexionSQLiteHelper conn2=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
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
                                String Encabezado
                                        = "\n\t        Distribuidora MyF\n"
                                        + "\t  Aramed de Jesus Sequeira Vega\n"
                                        + "\t\t      Cedula: 5-256-190\n"
                                        + "\t   Tels: 83031359 / 88197499\n"
                                        + "\t  Email: aramedsequeira@yahoo.es\n"
                                        + "\t\t\t       Nicoya, Guanacaste\n"
                                        + "--------------------------------\n";
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                String formattedDate ="Fecha-Hora: "+ df.format(c.getTime())+"\n";
                                String Descuento="--------------------------------\n" +
                                        "              *Fin*              ";
                                String abajoEncabezado="Informe de productos ingresados nuevamente\n" +
                                        "en el inventario."+formattedDate+"\n" +
                                        "--------------------------------\n" +
                                        "Lista de productos";
                                new AyudanteRecogerNumeroMaximoFactura().execute("http://192.168.43.199:8080/Sifacmyf/DevolverUltimoIdFacturaVenta.php");
                                String imprimir= ActualizarInventario();
                                MandarImprimir(Encabezado,abajoEncabezado,imprimir,Descuento);
                                AlertDialog.Builder confirmacionfinal= new AlertDialog.Builder(FinalizarDiaActivity.this);
                                confirmacionfinal.setMessage("Se realizo la accion con exito")
                                        .setCancelable(false)
                                        .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                               VaciasBdSQLite();

                                            }
                                        });
                                AlertDialog alertconfirmacionfinal=confirmacionfinal.create();
                                alertconfirmacionfinal.setTitle("Información");
                                alertconfirmacionfinal.show();

                            }
                        }).setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                AlertDialog alertconfirmacionfinalizardia=confirmacionfinalizardia.create();
                alertconfirmacionfinalizardia.setTitle("Final dia");
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

    private class AyudanteActualizarInventario extends AsyncTask<String, Void, String> {
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

    public String ActualizarInventario() {
        String imprimir="\nCod   Cant     Descr \n";
        SQLiteDatabase db = conn2.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + AyudanteCreacionBD.TABLA_PRODUCTO, null);
        while (cursor.moveToNext()) {

            if (cursor.getFloat(2) > 0) {
                new AyudanteActualizarInventario().execute("http://192.168.43.199:8080/Sifacmyf/ActualizarInventario.php?idproducto=" + cursor.getString(0) + "&cantidad=" + cursor.getFloat(2));
                imprimir+=String.format("%1$-4s",cursor.getString(0))+"  "+cursor.getFloat(2)+"  "+cursor.getString(1)+"\n";
            }
        }

        return imprimir;
    }
    public void VaciasBdSQLite(){
        SQLiteDatabase dbTotal=conn.getWritableDatabase();
        dbTotal.delete(AyudanteCreacionBD.TABLA_PRODUCTO,null,null);
        dbTotal.delete(AyudanteCreacionBD.TABLA_FACTURA_VENTA,null,null);
        dbTotal.delete(AyudanteCreacionBD.TABLA_DETALLE_FACTURA_VENTA,null,null);
        dbTotal.delete(AyudanteCreacionBD.TABLA_CLIENTE,null,null);
    }

    public void MandarImprimir(String Encabezado,  String abajoEncabezado,String imprimir, String Descuento){
        try{
            findBT();
            openBT();
            sendData(Encabezado);
            sendData(abajoEncabezado);
            sendData(imprimir);
            sendData(Descuento);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error\nNo se imprimio", Toast.LENGTH_LONG).show();
        }
    }

    void findBT() {
        // Toast.makeText(null,"se metio fiinbt", Toast.LENGTH_LONG).show();
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
                // myLabel.setText("No bluetooth adapter available");
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals("BlueTooth Printer")) {
                        mmDevice = device;
                        break;
                    }
                }
            }



        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void openBT() throws IOException {
        try {
            //Toast.makeText(null,"se metio openbt", Toast.LENGTH_LONG).show();
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 100;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[102400];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {

                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendData(String data) throws IOException {
        try {

            // the text typed by the user
            String msg = data;
            msg += "\n";

            mmOutputStream.write(msg.getBytes());

            // tell the user data were sent
            //myLabel.setText("Data sent.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            //  myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
